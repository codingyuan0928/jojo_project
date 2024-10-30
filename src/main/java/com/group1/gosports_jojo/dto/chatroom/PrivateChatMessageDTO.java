package com.group1.gosports_jojo.dto.chatroom;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L; // 避免反序列化錯誤
    private Integer senderId;
    private Integer receiverId;
    private String content;
    private Timestamp timestamp;
    private Boolean read;
}
