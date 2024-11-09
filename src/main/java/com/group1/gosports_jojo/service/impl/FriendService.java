package com.group1.gosports_jojo.service.impl;


import com.group1.gosports_jojo.dto.chatroom.response.FriendResponse;
import com.group1.gosports_jojo.dto.chatroom.response.UserRelationshipDTO;
import com.group1.gosports_jojo.entity.Friend;
import com.group1.gosports_jojo.entity.User;
import com.group1.gosports_jojo.repository.FriendRepository;
import com.group1.gosports_jojo.repository.MemberListRepository;
import com.group1.gosports_jojo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;


@Service
@Slf4j
public class FriendService {
    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MemberListRepository memberListRepository;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void sendInvitation(Integer senderId, Integer receiverId) throws Exception {
        // 確認發送者與接收者是否存在
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new Exception("發送者不存在"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new Exception("接收者不存在"));


        // 創建好友邀請實體
        Friend invitation = new Friend();
        invitation.setUserId(sender);
        invitation.setFriendId(receiver);
        invitation.setStatus("PENDING"); // 設定邀請狀態
        invitation.setRequestedAt(new Timestamp(System.currentTimeMillis()));

        // 保存邀請到資料庫
        friendRepository.save(invitation);
    }

    public String acceptInvitation(Integer senderId, Integer receiverId) throws Exception {
        // 查詢發送者和接收者的實體
        User userId = userRepository.findById(senderId)
                .orElseThrow(() -> new Exception("發送者不存在"));
        User friendId = userRepository.findById(receiverId)
                .orElseThrow(() -> new Exception("接收者不存在"));

        // 查詢對應的好友邀請
        Friend invitation = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .or(() -> friendRepository.findByUserIdAndFriendId(friendId, userId)) // 若第一個查詢無結果，則嘗試反向查詢
                .orElseThrow(() -> new Exception("好友邀請不存在"));


        // 更新邀請狀態為已接受
        invitation.setStatus("ACCEPTED");
        invitation.setRespondedAt(new Timestamp(System.currentTimeMillis()));
        friendRepository.save(invitation);

        // 返回接受邀請的使用者名字
        if (invitation.getUserId().equals(userId)) {
            return friendId.getUsername();
        } else {
            return userId.getUsername();
        }
    }

    public void deleteInvitation(Integer senderId, Integer receiverId) throws Exception {
        // 查詢發送者和接收者的實體
        User userId = userRepository.findById(senderId)
                .orElseThrow(() -> new Exception("發送者不存在"));
        User friendId = userRepository.findById(receiverId)
                .orElseThrow(() -> new Exception("接收者不存在"));

        // 查詢對應的好友邀請，任一匹配即可
        Friend invitation = friendRepository.findByUserIdAndFriendId(userId, friendId)
                .or(() -> friendRepository.findByUserIdAndFriendId(friendId, userId)) // 若第一個查詢無結果，則嘗試反向查詢
                .orElseThrow(() -> new Exception("好友邀請不存在"));

        // 刪除邀請
        friendRepository.delete(invitation);
    }

    public List<FriendResponse> getFriendsWithStatusByUserIdAndStatus(Integer id, String status, Boolean isSent) throws Exception {
        return friendRepository.findByUserIdOrFriendIdAndStatus(id, status)
                .stream()
                .filter(f -> {
                    if(isSent == null){
                        return true;
                    }
                    else if (isSent) {
                        return f.getUserId().getUserId().equals(id);
                    } else {
                        return f.getFriendId().getUserId().equals(id);
                    }
                })
                .map(f -> {
                    Integer friendId = getFriendId(f, isSent,id);
                    //User referenceById = userRepository.getReferenceById(friendId);
                    User referenceById = userRepository.findById(friendId).orElse(new User()); // 當找不到對象時，返回一個默認的 User 對象
                    String friendUsername = referenceById.getUsername();

                    return new FriendResponse(
                            friendId,
                            friendUsername
                    );
                })
                .toList();

    }
    public Integer getFriendId(Friend f, Boolean isSent,Integer id) {
        if(isSent == null){
            return f.getUserId().getUserId().equals(id)? f.getFriendId().getUserId():f.getUserId().getUserId();
        }
        else if(isSent){
            return f.getFriendId().getUserId();
        }
        else{
            return f.getUserId().getUserId();
        }
    }

    public List<UserRelationshipDTO> searchUsersByName(Integer userId, String username) {
        List<com.group1.gosports_jojo.dto.chatroom.UserRelationshipDTO> users = userRepository.findUserRelationshipsByUsername(userId,username);
        return users.stream().map(user -> {
            return new UserRelationshipDTO(
                    user.getUserId(),
                    user.getUsername(),
                    user.getRelationshipStatus(),
                    user.getInitiatorByMe()
            );
        }).collect(Collectors.toList());
    }

    public List<FriendResponse> getGroupMembers(Integer groupId) {
        return memberListRepository.findByGroupId_GroupId(groupId)
                .stream()
                .map(row -> new FriendResponse((Integer)row[0],(String)row[1]))
                .collect(Collectors.toList());
    }
}
