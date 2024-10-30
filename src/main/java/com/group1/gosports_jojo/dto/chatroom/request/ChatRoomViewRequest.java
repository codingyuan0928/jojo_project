package com.group1.gosports_jojo.dto.chatroom.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomViewRequest {
    private Integer userId;
    private String roomName;
}
