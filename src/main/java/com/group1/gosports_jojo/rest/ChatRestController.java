package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.dto.chatroom.GroupChatMessageDTO;
import com.group1.gosports_jojo.dto.chatroom.PrivateChatMessageDTO;
import com.group1.gosports_jojo.dto.chatroom.request.ChatRoomViewRequest;
import com.group1.gosports_jojo.dto.chatroom.response.ChatHistoryResponse;
import com.group1.gosports_jojo.dto.chatroom.response.GroupChatListResponse;
import com.group1.gosports_jojo.dto.chatroom.response.PrivateChatListResponse;
import com.group1.gosports_jojo.service.impl.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/messages")
public class ChatRestController {

    @Autowired
    private ChatService chateService;

    @MessageMapping("/privateChat")
    public void sendPrivateMessage(PrivateChatMessageDTO message) {
        chateService.processPrivateMessage(message);
    }
    @MessageMapping("/groupChat")
    public void sendGroupMessage(GroupChatMessageDTO message) {
        chateService.processGroupMessage(message);
    }
    @MessageMapping("/viewingChatRoom")
    public void updateViewingChatRoom(ChatRoomViewRequest message) {
        // 更新用戶正在查看的"私聊"聊天室
        chateService.updateUserViewingChatRoom(message.getUserId(), message.getRoomName());
    }
    @MessageMapping("/viewingGroup")
    public void updateViewingGroup(ChatRoomViewRequest message) {
        // 更新用戶正在查看的"群聊"聊天室
        chateService.updateUserViewingGroup(message.getUserId(), message.getRoomName());
    }

    @MessageMapping("/private/isUnreadNumber")
    public void isUnreadNumber(@RequestParam Integer userId) {
        chateService.isNumber(userId);
    }

    @MessageMapping("/group/isUnreadNumber")
    public void isGroupUnreadNumber(@RequestParam Integer groupId) {
        chateService.isGroupNumber(groupId);
    }

    @GetMapping("/privateList")
    @Operation(summary = "取得私聊清單")
    public ResponseEntity<PrivateChatListResponse> getPrivateChatList(@RequestParam Integer userId){
        try{
            PrivateChatListResponse privateList = chateService.getPrivateList(userId);
            return ResponseEntity.ok(privateList);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/groupList")
    @Operation(summary = "取得群聊清單")
    public ResponseEntity<List<GroupChatListResponse>> getPrivateChatroom(@RequestParam Integer userId){
        try{
            List<GroupChatListResponse> groupsByUserId = chateService.getGroupsByUserId(userId);
            return ResponseEntity.ok(groupsByUserId);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/privateChatroom")
    @Operation(summary = "取得私聊聊天紀錄")
    public ResponseEntity<ChatHistoryResponse> getPrivateChatroom(@RequestParam Integer userId, @RequestParam Integer friendId){
        try{
            ChatHistoryResponse privateChatroomHistory= chateService.getPrivateChatroom(userId,friendId);
            return ResponseEntity.ok(privateChatroomHistory);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/groupChatroom")
    @Operation(summary = "取得群組聊天紀錄")
    public ResponseEntity<List<GroupChatMessageDTO>> getGroupChatroom(@RequestParam Integer groupId){
        try{
            List<GroupChatMessageDTO> privateChatroomHistory= chateService.getGroupHistory(groupId);
            return ResponseEntity.ok(privateChatroomHistory);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/unreadMessages/chatRoom/{chatRoomId}/field/{userId}")
    @Operation(summary = "消除私聊未讀")
    public ResponseEntity<String> deleteChatRoomUnreadMessages(@PathVariable String chatRoomId, @PathVariable Integer userId) {
        chateService.deleteChatroomUnreadMessage(chatRoomId,userId);
        return ResponseEntity.ok("Unread messages deleted for chatRoom: " + chatRoomId);
    }

    @DeleteMapping("/unreadMessages/group/{groupId}/field/{userId}")
    @Operation(summary = "消除群組未讀")
    public ResponseEntity<String> deleteGroupUnreadMessages(@PathVariable Integer groupId, @PathVariable Integer userId) {
        chateService.deleteGroupUnreadMessage(groupId,userId);
        return ResponseEntity.ok("Unread messages deleted for group: " + groupId);
    }


    @PostMapping("/chatroom")
    @Operation(summary = "新增空的聊天室")
    public ResponseEntity<String> addChatroom(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        try {
            chateService.addNewChatroom(senderId,receiverId);
            return ResponseEntity.ok("成功新增私聊聊天室");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新邀請狀態失敗: " + e.getMessage());
        }
    }


}