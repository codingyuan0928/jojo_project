package com.group1.gosports_jojo.dto.chatroom.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatHistoryResponse {
    private List<ChatHistory> chatHistories;
    private Friend friend;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChatHistory{
        private Integer senderId;
        private String content; //訊息內容
        private Timestamp timestamp; //訊息的時間戳
        private Boolean isRead; //是否已讀
        private Boolean isSender; //是否是當前使用者發送的訊息

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Friend{
        private Integer friendId;
        private String friendName;

    }

}
