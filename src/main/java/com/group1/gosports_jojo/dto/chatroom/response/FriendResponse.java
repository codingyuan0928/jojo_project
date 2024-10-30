package com.group1.gosports_jojo.dto.chatroom.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FriendResponse {
    private Integer friendId;
    private String friendName;
}
