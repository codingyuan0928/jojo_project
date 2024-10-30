package com.group1.gosports_jojo.dto.chatroom.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {
    private Long id;
    private Long userId;
    private Long friendId;
    private String status; // PENDING (待定), ACCEPTED (已接受), REJECTED (已拒絕)
    private Timestamp requestedAt;
    private Timestamp respondedAt;
    private Timestamp createdAt;
}