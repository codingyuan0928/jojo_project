package com.group1.gosports_jojo.dto.chatroom;


public interface UserRelationshipDTO {
     Integer getUserId();
     String getUsername();
     byte[] getAvatar();
     String getRelationshipStatus();
     Boolean getInitiatorByMe();
}
