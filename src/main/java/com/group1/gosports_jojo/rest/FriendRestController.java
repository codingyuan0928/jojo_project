package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.dto.chatroom.response.FriendResponse;
import com.group1.gosports_jojo.dto.chatroom.response.UserRelationshipDTO;
import com.group1.gosports_jojo.entity.User;
import com.group1.gosports_jojo.repository.UserRepository;
import com.group1.gosports_jojo.service.impl.FriendService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/friends")

public class FriendRestController {
    @Autowired
    private FriendService friendService;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/invitations")
    @Operation(summary = "新增好友邀請")
    public ResponseEntity<String> sendFriendInvitation(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        try {
            friendService.sendInvitation(senderId, receiverId);

            // 通知被邀請的用戶接收到好友邀請
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(receiverId), // 發送給用戶 B (receiverId)
                    "/queue/notifications/sendFriendInvitation",     // 訂閱的路徑
                    "你收到新的好友邀請 📩" // 通知消息內容
            );

            return ResponseEntity.ok("好友邀請已發送");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發送邀請失敗");
        }
    }

    @PutMapping("/invitations")
    @Operation(summary = "更改好友邀請狀態為接受")
    public ResponseEntity<String> acceptFriendInvitation(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        try {
            String receivedName = friendService.acceptInvitation(senderId, receiverId);

            // 通知被邀請的用戶好友被接受
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(senderId),
                    "/queue/notifications/acceptFriendInvitation",  // 訂閱的路徑
                    receivedName + "成為你的好友🎉" // 通知消息內容
            );
            return ResponseEntity.ok("好友邀請已接受");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新邀請狀態失敗: " + e.getMessage());
        }
    }
    @DeleteMapping("/invitations")
    @Operation(summary = "刪除好友或取消好友邀請")
    public ResponseEntity<String> deleteFriendInvitation(@RequestParam Integer senderId, @RequestParam Integer receiverId) {
        try {
            friendService.deleteInvitation(senderId, receiverId);

            messagingTemplate.convertAndSendToUser(
                    String.valueOf(senderId),
                    "/queue/notifications/deleteFriendInvitation",
                    ""
            );
            messagingTemplate.convertAndSendToUser(
                    String.valueOf(receiverId),
                    "/queue/notifications/deleteFriendInvitation",
                    ""
            );
            return ResponseEntity.ok("好友邀請已刪除");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("刪除好友邀請失敗: " + e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "查詢你的好友 / 你送出的交友邀請 / 對方送出的交友邀請")
    public ResponseEntity<List<FriendResponse>> getFriendsByStatus(
            @RequestParam Integer userId,

            @Parameter(description = "查詢狀態", schema = @Schema(allowableValues = {"accepted", "pending"}))
            @RequestParam String status, // 狀態參數，例如 "accepted" 或 "pending"

            @Parameter(description = "我發起的邀請", schema = @Schema(allowableValues = {"true", "false"}))
            @RequestParam(required = false) Boolean invitationByMe // 邀請類型，例如 "sent" 或 "received"，預設為 null
    ) {
        try {
            List<FriendResponse> friends = new ArrayList<>();

            if ("accepted".equalsIgnoreCase(status)) {
                // 查詢已接受的好友
                friends = friendService.getFriendsWithStatusByUserIdAndStatus(userId,status,null);
            }

            if ("pending".equalsIgnoreCase(status) && invitationByMe.equals(true)) {
                // 查詢用戶自己發起的 pending 邀請
                friends = friendService.getFriendsWithStatusByUserIdAndStatus(userId,status,true);
            }

            if ("pending".equalsIgnoreCase(status) && invitationByMe.equals(false)) {
                // 查詢別人發起的 pending 邀請
                friends = friendService.getFriendsWithStatusByUserIdAndStatus(userId,status,false);
            }

            return ResponseEntity.ok(friends);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserRelationshipDTO>> searchUsers(Integer userId, String username) {
        List<UserRelationshipDTO> users = friendService.searchUsersByName(userId,username);
        return ResponseEntity.ok(users);
    }


    @GetMapping("/users/{id}/profile-picture")
    public ResponseEntity<byte[]> getProfilePicture(@PathVariable Integer id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isPresent() && optionalUser.get().getAvatar() != null) {
            byte[] image = optionalUser.get().getAvatar();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // 根據圖片類型設置 Content-Type
                    .body(image);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{groupId}" )
    @Operation(summary = "群組成員們")
    public ResponseEntity<List<FriendResponse>> groupMember(@PathVariable Integer groupId){
        List<FriendResponse> membersInGroup = friendService.getGroupMembers(groupId);
        return ResponseEntity.ok(membersInGroup);
    }

}

