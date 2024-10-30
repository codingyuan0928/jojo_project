package com.group1.gosports_jojo.dto.chatroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatMessageDTO {
    private Integer senderId;
    private Integer groupId;
    private List<Integer> receiverIds; // 群組中的接收者列表
    private String content;
    private Timestamp timestamp;
    private boolean isRead;
}
