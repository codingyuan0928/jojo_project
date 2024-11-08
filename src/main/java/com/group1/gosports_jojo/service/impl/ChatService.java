package com.group1.gosports_jojo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.group1.gosports_jojo.dto.chatroom.GroupChatMessageDTO;
import com.group1.gosports_jojo.dto.chatroom.PrivateChatMessageDTO;
import com.group1.gosports_jojo.dto.chatroom.response.ChatHistoryResponse;
import com.group1.gosports_jojo.dto.chatroom.response.GroupChatListResponse;
import com.group1.gosports_jojo.dto.chatroom.response.PrivateChatListResponse;
import com.group1.gosports_jojo.entity.User;
import com.group1.gosports_jojo.repository.MemberListRepository;
import com.group1.gosports_jojo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberListRepository memberListRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 更新用戶正在查看的私聊聊天室
    public void updateUserViewingChatRoom(Integer userId, String roomName) {
        // 如果 roomName 為 null 或空字串，刪除該 userId 在 Redis 中的記錄
        if (roomName == null || roomName.isEmpty()) {
            // 從 Redis 的 Hash 結構中刪除對應的 userId
            redisTemplate.opsForHash().delete("userViewingRoom", userId.toString());
        } else {
            // 如果 roomName 有值，則更新或插入
            redisTemplate.opsForHash().put("userViewingRoom", userId.toString(), roomName);
        }
    }

    // 更新用戶正在查看的群聊聊天室
    public void updateUserViewingGroup(Integer userId, String roomName) {
        // 如果 roomName 為 null 或空字串，刪除該 userId 在 Redis 中的記錄
        if (roomName == null || roomName.isEmpty()) {
            // 從 Redis 的 Hash 結構中刪除對應的 userId
            redisTemplate.opsForHash().delete("userViewingGroup", userId.toString());
        } else {
            // 如果 roomName 有值，則更新或插入
            redisTemplate.opsForHash().put("userViewingGroup", userId.toString(), roomName);
        }
    }

    public void processPrivateMessage(PrivateChatMessageDTO message){
        Integer sender = message.getSenderId();
        Integer receiver = message.getReceiverId();
        String content = message.getContent();
        Timestamp timestamp = message.getTimestamp();
        Boolean read = message.getRead();

        String chatRoomId = Math.min(sender,receiver) + "." + Math.max(sender,receiver);

        // 檢查接收者是否正在查看聊天室
        boolean isViewingChatRoom = isViewingChatRoom(receiver, chatRoomId);

        if (!isViewingChatRoom) {
            saveChatMessage(chatRoomId, message);
            incrementUnreadMessages(chatRoomId,message.getReceiverId());

        }
        else {
            message.setRead(true);
            saveChatMessage(chatRoomId, message);
        }

        ChatHistoryResponse chatHistoryResponse = new ChatHistoryResponse();

        //設置 ChatHistories
        ChatHistoryResponse.ChatHistory chatHistory = new ChatHistoryResponse.ChatHistory();
        List<ChatHistoryResponse.ChatHistory> list = new ArrayList<>();
        chatHistory.setContent(content);
        chatHistory.setTimestamp(timestamp);
        chatHistory.setSenderId(sender);
        chatHistory.setIsRead(read);
        list.add(chatHistory);
        chatHistoryResponse.setChatHistories(list);


        // 設置 friend
        ChatHistoryResponse.Friend friend = new ChatHistoryResponse.Friend();
        User referenceById = userRepository.findById(sender).orElse(null);
        friend.setFriendId(sender);
        chatHistoryResponse.setFriend(friend);

        messagingTemplate.convertAndSendToUser(String.valueOf(message.getReceiverId()), "/queue/message", chatHistoryResponse);


    }

    public void processGroupMessage(GroupChatMessageDTO message) {
        Integer sender = message.getSenderId();
        Integer groupId = message.getGroupId();
        String chatRoomId = "groupChat:" + groupId;

        redisTemplate.opsForList().rightPush(chatRoomId, message);

        //
        List<Object[]> membersInGroup = memberListRepository.findByGroupId_GroupId(groupId);
        //Map<Integer, Integer> map = new HashMap<>(); // 記錄

        membersInGroup.forEach(member -> {

            // member 正在查看的聊天室
            String viewingRoomStr = (String) redisTemplate.opsForHash().get("userViewingGroup", member[0].toString());

            // member 正在查看的聊天室。如果沒有查看任何聊天室，則設 viewingRoom null
            Integer viewingRoom = (viewingRoomStr != null) ? Integer.valueOf(viewingRoomStr) : null;

            // 將資料放入 map 中
            //map.put((Integer)object[0], viewingRoom);
            if(viewingRoom == null){
                redisTemplate.opsForHash().increment("unreadMessages:" + chatRoomId, member[0].toString(), 1);
            }

        });

        // 廣播消息給訂閱該群組的所有使用者
        messagingTemplate.convertAndSend("/topic/groupChat/" + groupId, message);


    }

    public PrivateChatListResponse getPrivateList(Integer userId){
        PrivateChatListResponse result = new PrivateChatListResponse();

        // 查找所有 key 包含 例如；"1" 的 chatRoom
        Set<String> chatRoomKeys = redisTemplate.keys("chatRoom:*" + userId + "*");
        // 如果沒有找到任何相關的 chatRoomKeys，直接返回空的結果
        if (chatRoomKeys == null || chatRoomKeys.isEmpty()) {
            return result; // 返回空的 PrivateChatListResponse
        }

        for (String chatRoomKey : chatRoomKeys) {
            // 從 Redis 取出聊天室內最後一筆資料
            PrivateChatMessageDTO lastMessage = (PrivateChatMessageDTO) redisTemplate.opsForList().index(chatRoomKey, -1);
            Integer senderId = lastMessage.getSenderId();
            Integer receiverId = lastMessage.getReceiverId();
            String message = lastMessage.getContent();
            Timestamp timestamp = lastMessage.getTimestamp();

            // 確認 friendId 與 friend 其他資訊
            Integer friendId = senderId.equals(userId) ? receiverId : senderId;
            User referenceById = userRepository.getReferenceById(friendId);
            String friendName = referenceById.getUsername();
            Integer unreadMessages = (Integer)redisTemplate.opsForHash().get("unreadMessages:" + chatRoomKey,userId.toString());


            PrivateChatListResponse.PrivateChatItem privateChatItem = new PrivateChatListResponse.PrivateChatItem(
                    friendId,
                    friendName,
                    message,
                    timestamp,
                    unreadMessages != null ? unreadMessages : 0
            );
            result.add(privateChatItem);
        }
        // 按時間倒序排序，最近的消息在最前面
        result.getPrivateChatList().sort(Comparator.comparing(PrivateChatListResponse.PrivateChatItem::getTimestamp).reversed());

        return result;

    }

    public List<GroupChatListResponse> getGroupsByUserId(Integer userId) {
        List<Object[]> groups = memberListRepository.findGroupByUserIdNative(userId);


        // 返回物件
        List<GroupChatListResponse> groupChatListResponseList = new ArrayList<>();


        for (Object[] group : groups) {

            GroupChatListResponse groupChatListResponse = new GroupChatListResponse();
            // 設定返回物件
            groupChatListResponse.setGroupId((Integer) group[0]);
            groupChatListResponse.setGroupName((String) group[1]);
            GroupChatMessageDTO lastMessage = (GroupChatMessageDTO)redisTemplate.opsForList().index("groupChat:" + group[0], -1);
           if(null == lastMessage){
               groupChatListResponse.setTimestamp(null);
               groupChatListResponse.setLastMessage("大家還在潛水呢，丟顆訊息炸彈吧！");
           }
           else {
               groupChatListResponse.setTimestamp(lastMessage.getTimestamp());
               groupChatListResponse.setLastMessage(lastMessage.getContent());
               Integer unreadMessages = (Integer)redisTemplate.opsForHash().get("unreadMessages:groupChat:" + group[0], userId.toString());
               groupChatListResponse.setUnreadNumber(unreadMessages);
           }

            groupChatListResponseList.add(groupChatListResponse);
        }

        groupChatListResponseList.sort(Comparator.comparing(
                GroupChatListResponse::getTimestamp,
                Comparator.nullsLast(Comparator.reverseOrder())
        ));

        return groupChatListResponseList;
    }

    // 儲存私人聊天記錄
    public void saveChatMessage(String chatRoomId, PrivateChatMessageDTO message) {
        redisTemplate.opsForList().rightPush("chatRoom:" + chatRoomId, message);
    }

    // 儲存群聊聊天記錄
    public void saveChatMessage(String chatRoomId, GroupChatMessageDTO message) {
        redisTemplate.opsForList().rightPush(chatRoomId, message);
    }
    // 增加某個使用者在某個聊天室的未讀訊息數
    public void incrementUnreadMessages(String chatRoomId, Integer userId) {
        redisTemplate.opsForHash().increment("unreadMessages:chatRoom:" + chatRoomId, userId.toString(), 1);
    }


    // 檢查用戶是否正在查看"私聊"聊天室
    public boolean isViewingChatRoom(Integer userId, String chatRoomId) {
        if (userId == null) {
            throw new IllegalArgumentException("查無此使用者");
        }
        if (chatRoomId == null) {
            throw new IllegalArgumentException("查無此聊天室");
        }
        String viewingRoom = (String)redisTemplate.opsForHash().get("userViewingRoom", userId.toString());
        return chatRoomId.equals(viewingRoom);
    }


    public ChatHistoryResponse getPrivateChatroom(Integer userId, Integer friendId) {
        // 確定 Redis 的 chatRoom key
        String key;
        if (userId.compareTo(friendId) < 0) {
            key = "chatRoom:" + userId + "." + friendId;
        } else {
            key = "chatRoom:" + friendId + "." + userId;
        }

        // 提前查詢 friendInfo 和 myInfo，只需查詢一次
        User friendInfo = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("好友不存在"));
        User myInfo = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用戶不存在"));

        // 設置 Friend 對象
        ChatHistoryResponse.Friend friend = new ChatHistoryResponse.Friend();
        friend.setFriendId(friendInfo.getUserId());
        friend.setFriendName(friendInfo.getUsername());

        // 處理 Redis 列表中的每一條訊息，並將其轉換為 ChatHistory 對象列表
        List<ChatHistoryResponse.ChatHistory> chatHistories = redisTemplate.opsForList().range(key, 0, -1)
                .stream()
                .map(item -> {
                    // 假設 item 是 JSON 字串，解析出需要的字段
                    ObjectMapper objectMapper = new ObjectMapper();
                    PrivateChatMessageDTO chatMessageDTO;

                    try {
                        chatMessageDTO = objectMapper.convertValue(item, PrivateChatMessageDTO.class);
                    } catch (IllegalArgumentException e) {
                        throw new RuntimeException("無法將 JSON 物件轉換為 ChatMessageDTO: " + item, e);
                    }

                    Integer senderId = chatMessageDTO.getSenderId();
                    String content = chatMessageDTO.getContent();
                    Timestamp timestamp = chatMessageDTO.getTimestamp();
                    Boolean isRead = chatMessageDTO.getRead();

                    // 設置 ChatHistory 內容
                    ChatHistoryResponse.ChatHistory chatHistory = new ChatHistoryResponse.ChatHistory();
                    chatHistory.setSenderId(senderId);
                    chatHistory.setContent(content);
                    chatHistory.setTimestamp(timestamp);
                    chatHistory.setIsRead(isRead);
                    chatHistory.setIsSender(userId.equals(senderId));

                    return chatHistory;
                })
                .collect(Collectors.toList());  // 將所有 ChatHistory 對象收集為列表


        // 建立最終的 ChatHistoryDTO
        ChatHistoryResponse result = new ChatHistoryResponse();
        result.setChatHistories(chatHistories);  // 設置聊天記錄列表
        result.setFriend(friend);                // 設置 friend

        return result;
    }


    public List<GroupChatMessageDTO> getGroupHistory(Integer groupId){

        List<Object> range = redisTemplate.opsForList().range("groupChat:" + groupId.toString(), 0, -1);

        // 直接將 Object 類型的數據轉換為 GroupChatMessageDTO
        return range.stream()
                .map(obj -> (GroupChatMessageDTO) obj)
                .collect(Collectors.toList());

    }

    public void deleteChatroomUnreadMessage(String chatRoomId, Integer userId) {
        // Redis Key 前綴
        String key = "unreadMessages:chatRoom:" + chatRoomId;
        // 刪除 Redis HASH 的指定 field
        redisTemplate.opsForHash().delete(key, userId.toString());
    }

    public void deleteGroupUnreadMessage(Integer groupId, Integer userId) {
        // Redis Key 前綴
        String key = "unreadMessages:groupChat:" + groupId;
        // 刪除 Redis HASH 的指定 field
        redisTemplate.opsForHash().delete(key, userId.toString());
    }

    public void addNewChatroom(Integer senderId, Integer receiverId) {
        String room = Math.min(senderId,receiverId) + "." + Math.max(senderId,receiverId);
        String key = "chatRoom:" + room;

        // 檢查 Redis 中的第一條訊息是否為 "開始聊天吧!"
        PrivateChatMessageDTO firstMessage = (PrivateChatMessageDTO) redisTemplate.opsForList().index(key, 0);

        if (firstMessage != null && "開始聊天吧!".equals(firstMessage.getContent())) {
            // 第一條訊息已經是 "開始聊天吧!"，直接返回
            return;
        }

        PrivateChatMessageDTO privateChatMessageDTO = new PrivateChatMessageDTO(
                senderId,
                receiverId,
                "開始聊天吧!",
                new Timestamp(System.currentTimeMillis()),
                false);
        redisTemplate.opsForList().rightPush(key, privateChatMessageDTO);
    }

    public void isNumber(Integer userId) {
        Set<String> chatRoomKeys = redisTemplate.keys("unreadMessages:chatRoom:*" + userId + "*");

        boolean hasUnreadMessages = false; // 用來追蹤是否有未讀訊息

        if (chatRoomKeys != null && !chatRoomKeys.isEmpty()) {
            for (String chatRoomKey : chatRoomKeys) {
                // 檢查 Redis 中是否有對應的 Key
                if (redisTemplate.hasKey(chatRoomKey)) {
                    // 從 Redis Hash 結構中取得對應的未讀訊息
                    Object unreadMessages = redisTemplate.opsForHash().get(chatRoomKey, userId.toString());

                    if (unreadMessages != null) {
                        hasUnreadMessages = true; // 找到未讀訊息，設定為 true
                        messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/queue/isUnreadNumber", true);
                        break; // 已經找到未讀訊息，可以直接跳出迴圈
                    }
                }
            }
        }

        // 如果沒有未讀訊息，發送 false 給用戶
        if (!hasUnreadMessages) {
            messagingTemplate.convertAndSendToUser(String.valueOf(userId), "/queue/isUnreadNumber", false);
        }
    }

    public void isGroupNumber(Integer groupId){ // groupId 前端原本傳 userId 要改

        // 這個群組有哪些人 convertAndSend 給他們是否更新 Tab 綠點
        List<Object[]> members = memberListRepository.findByGroupId_GroupId(groupId);
        Object memberId = null;
        for(Object[] member: members) {
            memberId = member[0];
            //Integer number = (Integer)redisTemplate.opsForHash().get("unreadMessages:groupChat:" + groupId, memberId.toString());

            // 使用者正在查看的群組
            Integer number = (Integer)redisTemplate.opsForHash().get("userViewingGroup", memberId.toString());

            // 如果使用者正在查看的群組，是組員正在傳送訊息的群組
            if(Objects.requireNonNull(number).equals(groupId)){
                //hasUnreadMessages = true;
                //messagingTemplate.convertAndSendToUser("/topic/isUnreadNumber", true); // 前端要改
                messagingTemplate.convertAndSendToUser(memberId.toString(), "/queue/group/isUnreadNumber", true);
            }
            else{
                messagingTemplate.convertAndSendToUser(memberId.toString(), "/queue/group/isUnreadNumber", false);
            }
        };
    }

}