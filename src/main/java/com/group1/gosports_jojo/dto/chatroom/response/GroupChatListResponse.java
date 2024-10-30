package com.group1.gosports_jojo.dto.chatroom.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatListResponse {
    private Integer groupId;
    private String groupName;
    private String lastMessage;
    private Timestamp timestamp;
    private Integer unreadNumber;
}
