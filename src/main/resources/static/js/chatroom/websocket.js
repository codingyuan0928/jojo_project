import {privateList, formatTimestampToTime, groupList, scrollToBottom} from './chat.js'
import {getFriendList, getFriendInvitationByMeList, getPendingFriendRequests} from "./friend.js";
import {groupChatList} from "./chatApi.js";

connectWebSocket();
/**
 * DEMO-WebSocket 連接
 */
export async function connectWebSocket() {
    if (stompClient !== null && stompClient.connected) {
        // 如果已經有連線，先斷開
        console.log("已有 WebSocket 連線，斷開舊的連線");
        stompClient.disconnect(() => {
            console.log('舊的 WebSocket 連線已斷開');
            initiateWebSocketConnection();  // 重新建立連線
        });
    } else {
        // 沒有連線時直接建立新的連線
        initiateWebSocketConnection();
    }
}


function initiateWebSocketConnection() {
    socket = new SockJS(`/ws`);
    stompClient = Stomp.over(socket);

    stompClient.connect({"user-id": 1}, async function (frame) {
        console.log('Connected: ', frame.headers);

        try {
            // 取得群組聊天列表並訂閱群組主題
            const res = await groupChatList(fakeUserId);
            console.log(res);
            let groupIds = [];
            res.forEach(item => {
                const {groupName, groupId, lastMessage, timestamp, unreadNumber} = item;
                console.log(groupId);
                groupIds.push(groupId);
            });

            // 訂閱每個群組的主題
            groupIds.forEach(groupId => {
                subscribeToTopic(groupId);  // 每次傳入不同的 groupId
            });

            // 訂閱自己的通道
            await subscribeToQueue(fakeUserId);

            // 訂閱收到加好友
            await subscribeToSendFriendInvitation(fakeUserId);

            // 訂閱交友邀請被接受
            await subscribeToAcceptFriendInvitation(fakeUserId);

            // 訂閱取消邀請/拒絕/刪除
            await subscribeToDeleteFriendInvitation(fakeUserId);

            // 訂閱是否私聊還有未讀訊息
            await subscribeToPrivateUnreadNumber(fakeUserId);


            // 訂閱是否群聊還有未讀訊息
            await subscribeToGroupUnreadNumber(fakeUserId);


            // 確保所有訂閱完成後再進行列表請求
            await privateList(fakeUserId);
            await groupList(fakeUserId);
            await getFriendList(fakeUserId);
            await getFriendInvitationByMeList(fakeUserId);
            await getPendingFriendRequests(fakeUserId);

        } catch (error) {
            console.error('訂閱過程出現錯誤: ', error);
        }

    }, function (error) {
        console.log('WebSocket 連接失敗: ', error);
        disconnect();
    });
}


function disconnect() {
    if (stompClient !== null) {
        // 在斷線之前發送 WebSocket 訊息，通知伺服器用戶已經離開聊天室
        var message1 = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: null   // 用戶離開聊天室時，設置 roomName 為 null
        };
        stompClient.send("/app/viewingGroup", {}, JSON.stringify(message1));

        var message2 = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: null   // 用戶離開聊天室時，設置 roomName 為 null
        };
        stompClient.send("/app/viewingChatRoom", {}, JSON.stringify(message2));

        // 主動斷開 WebSocket 連線
        stompClient.disconnect(function(frame) {
            console.log('斷開離線');
            // 確保重新連線
            setTimeout(reconnect, 100);
        });
    }
}
function reconnect() {
    console.log("重新連線...");
    initiateWebSocketConnection();
}

window.addEventListener('beforeunload', function (event) {
    var message1 = {
        userId: fakeUserId,
        roomName: null
    };
    stompClient.send("/app/viewingGroup", {}, JSON.stringify(message1));

    var message2 = {
        userId: fakeUserId,
        roomName: null
    };
    stompClient.send("/app/viewingChatRoom", {}, JSON.stringify(message2));

    // 清理訊息或進行其他操作
    stompClient.disconnect();
});


/**
 * DEMO-訂閱群組聊天通道
 */

function subscribeToTopic(groupId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/topic/groupChat/${groupId}`, function (message) {
            const chatMessage = JSON.parse(message.body);
            const {senderId, groupId, content, timestamp, read} = chatMessage
            console.log('收到訊息: ', chatMessage);
            playNotificationSound();

            const element = document.createElement('div');

            if (chatGroupId == groupId && senderId != fakeUserId && isPrivateChate !== true) {

                console.log('目前正在聊天對象:', currentChatId);
                console.log('目前在的群聊聊天室:', chatGroupId);
                console.log('是否在私聊中:', isPrivateChate);
                if (content.endsWith('.gif') || content.endsWith('.png')) {
                    // 如果是圖片訊息
                    element.classList.add('d-flex', 'mb-3');
                    element.innerHTML =
                        `<div class="user-avatar me-3">
                        <img src="${port}/friends/users/${senderId}/profile-picture" alt="" class="rounded-circle">
                    </div>
                    <div class="d-flex flex-column">
                        <div class="p-2 rounded">
                            <img src="${content}" alt="sticker" width="100px" height="100px">
                        </div>
                    <small class="text-muted">${formatTimestampToTime(timestamp)}</small>
                    </div>`;
                } else {
                    // 如果是文字訊息
                    element.classList.add('d-flex', 'mb-3');
                    element.innerHTML =
                        `<div class="user-avatar me-2">
                        <img src="${port}/friends/users/${senderId}/profile-picture" alt="" class="rounded-circle">
                     </div>
                    <div class="d-flex flex-column mt-2">
                        <div class="bg-light p-2 rounded chat-bubble">
                            <p class="mb-0">${content}</p>
                        </div>
                    <small class="text-muted">${formatTimestampToTime(timestamp)}</small>
                    </div>`;
                }
                document.getElementById('chatContent').appendChild(element);
                document.getElementById('chatContent').scrollTop = chatContent.scrollHeight;

            }


            //更新群聊清單
            groupList(fakeUserId);
            scrollToBottom();
        })


    } else {
        console.log('尚未連接 WebSocket');
    }
}

/**
 * DEMO-訂閱私人聊天通道
 */
function subscribeToQueue(userId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/user/${fakeUserId}/queue/message`, function (message) {
            const chatMessage = JSON.parse(message.body);
            const {chatHistories, friend} = chatMessage;
            console.log("收到私人訊息: ", chatMessage);
            playNotificationSound();

            const element = document.createElement('div');
            element.classList.add('d-flex', 'mb-3');


            if (isPrivateChate
                && currentChatId == friend.friendId
                && (chatHistories[0].content.endsWith('.gif') || chatHistories[0].content.endsWith('.png'))) {
                element.innerHTML =
                    `
            <div class="user-avatar me-3">
                <img src="${port}/friends/users/${friend.friendId}/profile-picture" alt="" class="rounded-circle">
            </div>
                <div class="d-flex flex-column">
                    <div class="p-2 rounded">
                        <img src="${chatHistories[0].content}" alt="sticker" width="100px" height="100px">
                    </div>
                    <small class="text-muted">${formatTimestampToTime(chatHistories[0].timestamp)}</small>
                </div>`;
            }
            else if (isPrivateChate && currentChatId == friend.friendId) {


                element.innerHTML = `
                <div class="user-avatar me-2">
                <img src="${port}/friends/users/${friend.friendId}/profile-picture" alt="" class="rounded-circle">
                </div>
                <div class="d-flex flex-column mt-2">
                    <div class="bg-light p-2 rounded chat-bubble">
                        <p class="mb-0">${chatHistories[0].content}</p>
                    </div>
                    <small class="text-muted">${formatTimestampToTime(chatHistories[0].timestamp)}</small> <!-- 時間戳記 -->
                </div>`;

            }
            document.getElementById('chatContent').appendChild(element);
            document.getElementById('chatContent').scrollTop = chatContent.scrollHeight;

            // 更新私聊清單
            privateList(fakeUserId);
        })
    } else {
        console.log('尚未連接 WebSocket');
    }
}

/**
 * DEMO-訂閱通收到加好友
 */
function subscribeToSendFriendInvitation(fakeUserId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/user/${fakeUserId}/queue/notifications/sendFriendInvitation`, function (message) {
            const notification = message.body;
            console.log('收到好友邀請通知:', notification);
            getPendingFriendRequests(fakeUserId);
            showBootstrapAlert(notification);
            scrollToBottom();
        });
    } else {
        console.log('尚未連接 WebSocket');
    }
}


/**
 * DEMO-訂閱接受好友通知
 */
function subscribeToAcceptFriendInvitation(fakeUserId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/user/${fakeUserId}/queue/notifications/acceptFriendInvitation`, function (message) {
            const notification = message.body;
            console.log(notification, '接受好友邀請');
            getFriendList(fakeUserId);
            getFriendInvitationByMeList(fakeUserId);
            showBootstrapAlert(notification);
        });
    } else {
        console.log('尚未連接 WebSocket');
    }
}

/**
 * DEMO-訂閱取消邀請/拒絕/刪除
 */
function subscribeToDeleteFriendInvitation(fakeUserId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/user/${fakeUserId}/queue/notifications/deleteFriendInvitation`, function (message) {
            const notification = message.body;
            console.log(notification, '接受好友邀請');
            getFriendList(fakeUserId)
            getFriendList(fakeUserId);
            getFriendInvitationByMeList(fakeUserId);
        });
    } else {
        console.log('尚未連接 WebSocket');
    }
}

/**
 * DEMO-訂閱取得私人聊天 是否有未讀訊息
 */
function subscribeToPrivateUnreadNumber(fakeUserId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/user/${fakeUserId}/queue/isUnreadNumber`, function (message) {
            const isUnread = message.body === 'true'; // 將字串轉換成布林值
            if (isUnread) {
                document.getElementById('private-chat-tab').classList.remove('hide-dot');
            } else {
                document.getElementById('private-chat-tab').classList.add('hide-dot');
            }
        });
    } else {
        console.log('尚未連接 WebSocket');
    }
}

function subscribeToGroupUnreadNumber(fakeUserId) {
    if (stompClient && stompClient.connected) {
        stompClient.subscribe(`/user/${fakeUserId}/queue/group/isUnreadNumber`, function (message) {
            const isUnread = message.body === 'true'; // 將字串轉換成布林值
            if (isUnread) {
                document.getElementById('group-chat-tab').classList.remove('hide-dot');
            } else {
                document.getElementById('group-chat-tab').classList.add('hide-dot');
            }
        });
    } else {
        console.log('尚未連接 WebSocket');
    }
}

/**
 * DEMO-發送訊息
 */
export function sendMessage(messageContent) {
    const chatMessage = {
        content: messageContent, senderId: fakeUserId,  // 或其他使用者名稱
        receiverId: currentChatId, timestamp: new Date().toISOString()
    };

    const groupMessage = {
        content: messageContent, senderId: fakeUserId,  // 或其他使用者名稱
        groupId: chatGroupId, timestamp: new Date().toISOString()
    }

    if (!isPrivateChate) {
        console.log('群組聊天')

        // 群組聊天
        stompClient.send(`/app/groupChat`, {}, JSON.stringify(groupMessage));

        // 更新 Sender 的畫面
        const element = document.createElement('div');
        if (messageContent.endsWith('.gif') || messageContent.endsWith('.png')) {
            element.classList.add('d-fle', 'justify-content-end', 'mb-3');
            element.innerHTML = `<div class="d-flex flex-column align-items-end">
                <div class="p-2 rounded">
                    <img src="${messageContent}" alt="sticker" width="100px" height="100px">
                </div>
                <small class="text-muted">${formatTimestampToTime(chatMessage.timestamp)}</small>
                </div>`;
        } else {
            element.classList.add('d-flex', 'justify-content-end', 'mb-3');
            element.innerHTML = `<div class="d-flex flex-column align-items-end">
                    <div class="bg-primary text-white p-2 rounded chat-bubble">
                        <p class="mb-0">${messageContent}</p>
                    </div>
                    <small class="text-muted">${formatTimestampToTime(chatMessage.timestamp)}</small> <!-- 時間戳記 -->
                </div>`;
        }

        document.getElementById('chatContent').appendChild(element);
        // 滾動到底部以顯示最新訊息
        chatContent.scrollTop = chatContent.scrollHeight;
        // 更新群聊清單
        groupList(fakeUserId);

        // 更新 Tab 綠點
        stompClient.send("/app/group/isUnreadNumber", {}, JSON.stringify(chatGroupId));
    }

    if (isPrivateChate) {

        console.log(' 一對一聊天')

        // 一對一聊天
        stompClient.send(`/app/privateChat`, {}, JSON.stringify(chatMessage));

        if (messageContent.endsWith('.gif') || messageContent.endsWith('.png')) {
            // 如果 messageContent 是 GIF 圖片，使用 img 標籤顯示
            const element = document.createElement('div');
            element.classList.add('d-flex', 'justify-content-end', 'mb-3');
            element.innerHTML = `<div class="d-flex flex-column align-items-end">
                <div class="p-2 rounded">
                    <img src="${messageContent}" alt="sticker" width="100px" height="100px">
                </div>
                <small class="text-muted">${formatTimestampToTime(chatMessage.timestamp)}</small>
            </div>`;
            document.getElementById('chatContent').appendChild(element);
        } else {
            // 更新 Sender 的畫面
            const element = document.createElement('div');
            element.classList.add('d-flex', 'justify-content-end', 'mb-3');
            element.innerHTML = `<div class="d-flex flex-column align-items-end">
                    <div class="bg-primary text-white p-2 rounded chat-bubble">
                        <p class="mb-0">${messageContent}</p>
                    </div>
                    <small class="text-muted">${formatTimestampToTime(chatMessage.timestamp)}</small> <!-- 時間戳記 -->
                </div>`;
            document.getElementById('chatContent').appendChild(element);
        }

        // 滾動到底部以顯示最新訊息
        chatContent.scrollTop = chatContent.scrollHeight;
        // 更新私聊清單
        privateList(fakeUserId);
        // 更新 Tab 綠點
        stompClient.send("/app/private/isUnreadNumber", {}, JSON.stringify(fakeUserId));
    }

}


/**
 * DEMO-發送訊息事件
 */
document.getElementById('sendMessageBtn').addEventListener('click', function () {
    // 獲取輸入框中的訊息內容
    const messageInput = document.getElementById('messageInput');
    const messageContent = messageInput.value.trim();

    // 確保訊息不是空的
    if (messageContent) {

        // 調用發送訊息的函數
        sendMessage(messageContent);

        // 清空輸入框
        messageInput.value = '';
    }
});

/**
 * DEMO-播放音效的函數
 */
function playNotificationSound() {
    const audio = new Audio('/mp3/message.mp3'); // 替換成你的音效檔案路徑
    audio.volume = 0.5;
    audio.play();
}


/**
 * DEMO-Alert
 */
function showBootstrapAlert(message) {
    const customAlert = document.getElementById('customAlert');

    // 建立 Bootstrap Alert 元素
    const alertHTML = `
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    `;

    // 將 Alert 插入到頁面中
    customAlert.innerHTML = alertHTML;

    // 設定 Alert 自動消失（可選）
    setTimeout(() => {
        customAlert.innerHTML = '';
    }, 8000); // 8秒後自動消失
}
