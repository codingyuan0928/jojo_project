package com.group1.gosports_jojo.dto.chatroom.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrivateChatListResponse {

    private List<PrivateChatItem> privateChatList;

    // 添加 PrivateChatItem 的方法
    public void add(PrivateChatItem privateChatItem) {
        if (this.privateChatList == null) {
            this.privateChatList = new ArrayList<>();
        }
        this.privateChatList.add(privateChatItem);
    }

    // 靜態內部類
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PrivateChatItem {
        private Integer friendId;
        private String friendName;
        private String content;
        private Timestamp timestamp;
        private Integer unreadNumber;

    }
}

