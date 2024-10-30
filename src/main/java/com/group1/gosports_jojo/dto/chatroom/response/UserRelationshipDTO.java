package com.group1.gosports_jojo.dto.chatroom.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRelationshipDTO {
    Integer userId;
    String username;
    String relationshipStatus;
    Boolean initiatorByMe;
}
