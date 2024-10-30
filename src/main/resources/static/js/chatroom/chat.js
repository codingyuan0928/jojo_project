import {
    groupChatList,
    fetchgroupChatHistory,
    privateChatList,
    fetchPrivateChatHistory,
    deletePrivateUnread,
    deleteGroupUnread,
    getGroupMember
} from './chatApi.js';

import {sendMessage} from './websocket.js'

/**
 * DEMO-隱藏開始聊天吧字樣
 */
function hideChatPlaceholder() {
    const chatPlaceholder = document.getElementById('chatPlaceholder');
    if (chatPlaceholder) {
        chatPlaceholder.style.display = 'none';  // 隱藏 "開始聊天吧" 提示
    }
    const chatWindow = document.getElementById('chatWindow');
    if (chatWindow) {
        chatWindow.style.display = 'block';  // 顯示聊天內容
    }
}

/**
 * DEMO-顯示開始聊天吧字樣
 */
function showChatPlaceholder() {
    const chatPlaceholder = document.getElementById('chatPlaceholder');
    if (chatPlaceholder) {
        chatPlaceholder.style.removeProperty('display');  // 顯示 "開始聊天吧" 提示
    }
    const chatWindow = document.getElementById('chatWindow');
    if (chatWindow) {
        chatWindow.style.display = 'none';  // 隱藏聊天內容
    }
}


/**
 * DEMO-監聽 Esc
 */
document.addEventListener('keydown', function(event) {
    if (event.key === "Escape") {
        showChatPlaceholder();
        document.querySelectorAll('.chatItem.active').forEach(item => {
            item.classList.remove('active');
        });
        document.querySelectorAll('.groupChatItem.active').forEach(item => {
            item.classList.remove('active');
        });


        currentChatId = null;
        chatGroupId = null;

        var message1 = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: null   // 當前查看的聊天室 ID
        };
        stompClient.send("/app/viewingGroup", {}, JSON.stringify(message1));



        var message2 = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: null   // 當前查看的聊天室 ID
        };
        stompClient.send("/app/viewingChatRoom", {}, JSON.stringify(message2));

    }
});



/**
 * DEMO-時間格式轉換
 */
export function formatTimestamp(timestamp) {
    const date = new Date(timestamp);
    const now = new Date();

    const isSameDay = date.getDate() === now.getDate() &&
        date.getMonth() === now.getMonth() &&
        date.getFullYear() === now.getFullYear();

    const isSameYear = date.getFullYear() === now.getFullYear();

    if (isSameDay) {
        // 如果是當天，返回 "時:分"
        const hours = date.getHours().toString().padStart(2, '0');
        const minutes = date.getMinutes().toString().padStart(2, '0');
        return `${hours}:${minutes}`;
    } else if (isSameYear) {
        // 如果是同一年但不是當天，返回 "月-日"
        const month = (date.getMonth() + 1).toString().padStart(2, '0'); // getMonth() 從 0 開始
        const day = date.getDate().toString().padStart(2, '0');
        return `${month}-${day}`;
    } else {
        // 如果不是當年，返回 "年-月-日"
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        return `${year}-${month}-${day}`;
    }
}


/**
 * DEMO-將 timestamp 轉換為日期 (例如：2024-10-11)
 */
function formatTimestampToDate(timestamp) {
    const date = new Date(timestamp);
    const now = new Date();

    const year = date.getFullYear();
    const currentYear = now.getFullYear(); // 獲取當前年份
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    // 如果年份與當前年份相同，則不顯示年份
    if (year === currentYear) {
        return `${month}-${day}`; // 只顯示月日
    } else {
        return `${year}-${month}-${day}`; // 顯示年-月-日
    }
}

/**
 * DEMO-將 timestamp 轉換為時間 (例如：10:02)
 */
export function formatTimestampToTime(timestamp) {
    const date = new Date(timestamp);
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${hours}:${minutes}`;
}

/**
 * DEMO-私聊聊天列表
 */
export async function privateList(userId) {
    try {
        const res = await privateChatList(userId);  // 取得 API 資料
        let activeItemId= document.querySelector('.chatItem.active')?.dataset.friendId;

        if (res.privateChatList && res.privateChatList.length > 0) {
            createPrivateChatItem(res);  // 將資料傳遞給 createPrivateChatItem 進行渲染

            //在渲染完列表後，重新設置 active 項目
            if (activeItemId) {
                let newActiveItem = document.querySelector(`[data-friend-id="${activeItemId}"]`); //TODO:錯誤
                if (newActiveItem) {
                    newActiveItem.classList.add('active');
                }
            }
        }
    } catch (error) {
        console.error('Error fetching private chat list:', error);
    }
}

/**
 * DEMO-私聊列表元件
 */
function createPrivateChatItem(res) {
    const{privateChatList} = res
    let isUnRead = false;
    document.getElementById('chatList').innerHTML='';


    // 遍歷 privateChatList，解構每個聊天項目的屬性
    privateChatList.forEach(chat => {
        const {friendId, friendName, friendAvatar, content, timestamp, unreadNumber} = chat;
        const formattedTime = formatTimestamp(timestamp);
        const chatRoomId = Math.min(friendId,fakeUserId) + "." + Math.max(friendId,fakeUserId);
        if(0 !== unreadNumber && unreadNumber != null){
            isUnRead = true;
        }

        // 創建列表項目
        const element = document.createElement('div');
        element.setAttribute('data-friend-id', friendId);
        element.classList.add('chatItem', 'list-group-item', 'd-flex', 'align-items-center');

        if(content.endsWith('.png') || content.endsWith('.jpg')){
            element.innerHTML = `
            <div class="user-avatar me-3">
                <img src="${port}/friends/users/${friendId}/profile-picture" alt="" class="rounded-circle">
            </div>
            <div>
                <div class="fw-bold">${friendName}</div>
                <small class="text-muted">貼圖</small>
            </div>
            <small class="ms-auto text-muted">${formattedTime}</small>
            <span data-chatroom-id="${chatRoomId}" class="badge bg-danger ms-3 unread-badge" style="visibility: ${unreadNumber > 0 ? 'visible' : 'hidden'};">
            ${unreadNumber > 0 ? `${unreadNumber}` : ''}
            </span>`;
        }
        else{
            element.innerHTML = `
            <div class="user-avatar me-3">
                <img src="${port}/friends/users/${friendId}/profile-picture" alt="" class="rounded-circle">
            </div>
            <div>
                <div class="fw-bold">${friendName}</div>
                <small class="text-muted">${content}</small>
            </div>
            <small class="ms-auto text-muted">${formattedTime}</small>
            <span data-chatroom-id="${chatRoomId}" class="badge bg-danger ms-3 unread-badge" style="visibility: ${unreadNumber > 0 ? 'visible' : 'hidden'};">
            ${unreadNumber > 0 ? `${unreadNumber}` : ''}
            </span>
        `;
            console.log('創建新的私聊清單項目');
        }


        // 綁定點擊事件來觸發私聊
        element.addEventListener('click', function(){
            currentChatId = element.dataset.friendId;
            console.log(`Receiver ID: ${currentChatId}`);
            // 更新 currentChatId 並加載對應聊天記錄
            openPrivateChatWindow(currentChatId);  // 更新 currentChatId 並加載聊天記錄

            // 選擇所有群組聊天項目
            const privateChatItems = document.querySelectorAll('.chatItem');
            // 移除所有項目的 active 類
            privateChatItems.forEach(i => {
                if (i) {  // 確保 i 不為 undefined 或 null
                    i.classList.remove('active');
                }
            });
            // 為當前點擊的項目添加 active 類
            this.classList.add('active');

        });

        // 將聊天項目添加到聊天列表中
        const privateChatListContainer = document.getElementById('chatList');
        privateChatListContainer.appendChild(element);



    });
    // 更新 Tab 綠點狀態
    console.log(isUnRead);
    if(isUnRead){
        document.getElementById('private-chat-tab').classList.remove('hide-dot')
    }
}

/**
 * DEMO-打開私聊聊天室
 */
export async function openPrivateChatWindow(friendId) {
    isPrivateChate = true;
    var message = {
        userId: fakeUserId,  // 當前用戶的 ID
        roomName: null   // 當前查看的聊天室 ID
    };
    stompClient.send("/app/viewingGroup", {}, JSON.stringify(message));
    hideChatPlaceholder();
    document.querySelectorAll('.groupChatItem.active').forEach(groupChatItem => {
        groupChatItem.classList.remove('active');
    });
    currentChatId = friendId;  // 更新全局變數
    const room = Math.min(fakeUserId,currentChatId) + "." + Math.max(fakeUserId,currentChatId);

    if (stompClient && stompClient.connected) {
        var message = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: room   // 當前查看的聊天室 ID
        };
        stompClient.send("/app/viewingChatRoom", {}, JSON.stringify(message));
        console.log("Viewing chatroom: ", message);
        // 調用 API 獲取聊天記錄
        await getPrivateChatHistory(fakeUserId,currentChatId);


        // 是否目前私聊有未讀訊息存在
        stompClient.send("/app/private/isUnreadNumber", {}, JSON.stringify(fakeUserId));


    } else {
        console.error("Not connected to WebSocket");
    }
}

/**
 * DEMO-私聊聊天紀錄
 */
async function getPrivateChatHistory(userId,friendId){
    // 更改 User Info Header
    document.getElementById('userInfo').style.display = 'block';
    document.getElementById('userInfoHeader-name').style.marginLeft = '';
    document.getElementById('userInfoHeader-name').classList.add('fw-bold');
    document.getElementById('viewMembersButton').style.display = 'none';

    // 刪除未讀提醒
    const roomId = Math.min(userId, friendId) + "." + Math.max(userId, friendId);
    deletePrivateUnread(roomId,fakeUserId)

    try {
        const res = await fetchPrivateChatHistory(userId,friendId);
        console.log('Response:', res);
        const{chatHistories,friend} = res;

        document.getElementById('userInfoHeader-avatar').setAttribute('src', `${port}/friends/users/${friendId}/profile-picture`);
        const userName = friend.friendName;  // 後端返回的用戶名稱


        // 更新用戶名稱
        const userNameElement = document.getElementById('userInfoHeader-name');
        userNameElement.textContent = userName;

        if (chatHistories && chatHistories.length > 0) {
            // 將資料傳遞給 createPrivateChatHistoryItem 進行渲染
            createPrivateChatHistoryItem(res);
            // 滾動到底部
            scrollToBottom();


        } else {
            console.log('No private chat list available.');
        }
    } catch (error) {
        console.error('Error fetching private chat list:', error);
    }
}

/**
 * DEMO-私聊聊天紀錄項目
 */
export function createPrivateChatHistoryItem(res) {
    const{ chatHistories,friend } = res;
    const chatContent = document.getElementById('chatContent');
    chatContent.innerHTML='';

    let previousDate = ''; // 用於記錄上一條訊息的日期

    chatHistories.forEach(message => {
        const timestamp = formatTimestampToTime(message.timestamp);
        const messageDate = formatTimestampToDate(message.timestamp); // 獲取日期 (不包含時間)

        // 如果當前訊息的日期與前一條訊息的日期不同，插入日期標籤
        if (messageDate !== previousDate) {
            const dateElement = document.createElement('div');
            dateElement.classList.add('date-divider'); // 添加一個樣式來顯示日期
            dateElement.innerHTML = `<span>${messageDate}</span>`; // 將日期包裝在 span 中
            chatContent.appendChild(dateElement);

            // 更新 previousDate 為當前訊息的日期
            previousDate = messageDate;
        }


        // 檢查是否是 "開始聊天吧!"，如果是則應用特殊樣式
        const isStartChatMessage = message.content === "開始聊天吧!";

        // 創建列表項目
        const element = document.createElement('div');
        if(message.senderId != fakeUserId && isStartChatMessage == false &&
            (message.content.endsWith('.gif') || message.content.endsWith('.png'))){

            element.classList.add('d-flex', 'mb-3');
            element.innerHTML =
                `<div class="user-avatar me-3">
                        <img src="${port}/friends/users/${friend.friendId}/profile-picture" alt="" class="rounded-circle">
            
                </div>
        <div class="d-flex flex-column">
            <div class="p-2 rounded">
                <img src="${message.content}" alt="sticker" width="100px" height="100px">
            </div>
            <small class="text-muted">${timestamp}</small>
        </div>`;
        }
        else if (message.senderId != fakeUserId && isStartChatMessage == false) {

            element.classList.add('d-flex', 'mb-3');
            element.innerHTML =
                `<div class="user-avatar me-3">
                <img src="${port}/friends/users/${friend.friendId}/profile-picture" alt="" class="rounded-circle">
            </div>
            <div class="d-flex flex-column">
                <div class="bg-light p-2 rounded chat-bubble">
                    <p class="mb-0">${message.content}</p>
                </div>
                <small class="text-muted">${timestamp}</small> <!-- 時間戳記 -->
            </div>`;
        }
        else if(message.senderId == fakeUserId && isStartChatMessage == false &&
            (message.content.endsWith('.gif') || message.content.endsWith('.png'))){

            element.classList.add('d-flex', 'justify-content-end', 'mb-3');
            element.innerHTML =
                `<div class="d-flex flex-column align-items-end">
                <div class="p-2 rounded">
                    <img src="${message.content}" alt="sticker" width="100px" height="100px">
                </div>
                <small class="text-muted">${timestamp}</small> <!-- 顯示時間戳 -->
                </div>`;
        }
        else if(message.senderId == fakeUserId && isStartChatMessage == false) {
            element.classList.add('d-flex', 'justify-content-end', 'mb-3');
            element.innerHTML =
                `<div class="d-flex flex-column align-items-end">
                    <div class="bg-primary text-white p-2 rounded chat-bubble">
                        <p class="mb-0">${message.content}</p>
                    </div>
                    <small class="text-muted">${timestamp}</small> <!-- 時間戳記 -->
                    
                </div>`;
        }

        else {
            element.classList.add('date-divider');
            element.innerHTML = `<span>${message.content}</span>`
        }
        // 將聊天項目添加到聊天列表中
        chatContent.appendChild(element);
    })
}

/**
 * DEMO-群組聊天列表
 */
export async function groupList(fakeUserId){
    const res = await groupChatList(fakeUserId);
    let activeItemId= document.querySelector('.groupChatItem.active')?.dataset.roomId;
    document.getElementById('groupChatList').innerHTML = '';

    let isExistUnReadNumber = false;
    res.forEach(item => {
        const {groupName, groupId,lastMessage, timestamp, unreadNumber} = item;
        // 是否有 unReadNumber
        // 如果有未讀
        if(unreadNumber !== null){
            isExistUnReadNumber = true;
        }


        // 檢查 groupId 是否已經存在，避免重複加入
        if (!fakUserIdInGroups.includes(groupId)) {
            fakUserIdInGroups.push(groupId);
        }
        console.log(fakeUserId, "在群組", fakUserIdInGroups);

        createGroupChatItem(item);

        //在渲染完列表後，重新設置 active 項目
        if (activeItemId) {
            let newActiveItem = document.querySelector(`[data-room-id="${activeItemId}"]`); //TODO:錯誤
            if (newActiveItem) {
                newActiveItem.classList.add('active');
            }
        }

    });

    console.log('是否還有未讀',isExistUnReadNumber);
    // 如果存在未讀訊息
    if(isExistUnReadNumber){
        // 顯示 green dot，也就是移除 hide-dot
        document.getElementById('group-chat-tab').classList.remove('hide-dot')
    }
    else{
        document.getElementById('group-chat-tab').classList.add('hide-dot')
    }
}

/**
 * DEMO-群組聊天列表元件
 */
function createGroupChatItem(item) {
    const {groupName, groupId,lastMessage, timestamp, unreadNumber} = item;
    const formattedTimestamp = timestamp != null ? formatTimestamp(timestamp) : null;

    const element = document.createElement('div');
    element.setAttribute('data-room-id', groupId);
    element.classList.add('groupChatItem', 'list-group-item', 'd-flex', 'align-items-center');

    if(lastMessage.endsWith('.png') || lastMessage.endsWith('.jpg')){
        element.innerHTML = `<div>
                            <div class="fw-bold">${groupName}</div>
                            <small class="text-muted">貼圖</small>
                         </div>
                            <small class="ms-auto text-muted" style="visibility: ${formattedTimestamp != null ? 'visible' : 'hidden'};">
                             ${formattedTimestamp}
                            </small>
                            <span data-room-id="${groupId}" class="badge bg-danger ms-3 unread-badge" style="visibility: ${unreadNumber != null ? 'visible' : 'hidden'};">
                            ${unreadNumber}
                            </span>
                           `;
    }
    else{
        element.innerHTML = `<div>
                            <div class="fw-bold">${groupName}</div>
                            <small class="text-muted">${lastMessage}</small>
                         </div>
                            <small class="ms-auto text-muted" style="visibility: ${formattedTimestamp != null ? 'visible' : 'hidden'};">
                             ${formattedTimestamp}
                            </small>
                            <span data-room-id="${groupId}" class="badge bg-danger ms-3 unread-badge" style="visibility: ${unreadNumber != null ? 'visible' : 'hidden'};">
                            ${unreadNumber}
                            </span>
                           `;
    }



    // 綁定事件
    element.addEventListener('click', function () {
        // 移除所有項目的 active 類
        document.querySelectorAll('.groupChatItem').forEach(i => i.classList.remove('active'));

        // 為當前點擊的項目添加 active 類
        this.classList.add('active');

        // 取得聊天紀錄
        openGroupChatWindow(groupId,groupName)

        // 這裡你可以加入更多的邏輯，比如打開聊天窗口或顯示該群組的聊天記錄
        console.log(`Chat Room ID: ${this.dataset.roomId}`); // 可用於後續處理，例如打開該群組的聊天記錄


    })


    const groupChatList = document.getElementById('groupChatList');

    groupChatList.appendChild(element);
}


/**
 * DEMO-打開群聊聊天室
 */
function openGroupChatWindow(groupId,groupName) {
    isPrivateChate = false;
    deleteGroupUnread(groupId,fakeUserId)


    hideChatPlaceholder();


    document.querySelectorAll('.chatItem.active').forEach(chatItem  => {
        chatItem.classList.remove('active');
    });

    // 正在的群聊聊天室
    chatGroupId = groupId;


    if (stompClient && stompClient.connected) {
        var message1 = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: groupId   // 當前查看的聊天室 ID
        };
        stompClient.send("/app/viewingGroup", {}, JSON.stringify(message1));



        var message2 = {
            userId: fakeUserId,  // 當前用戶的 ID
            roomName: null   // 當前查看的聊天室 ID
        };
        stompClient.send("/app/viewingChatRoom", {}, JSON.stringify(message2));
        // 調用 API 獲取聊天記錄
        // 取得聊天紀錄
        groupHistory(chatGroupId,groupName);

        groupList(fakeUserId);

    } else {
        console.error("Not connected to WebSocket");
    }
}


/**
 * DEMO-群組聊天紀錄
 */
async function groupHistory(roomId,groupName) {


    document.getElementById('chatContent').innerHTML='';
    document.getElementById('userInfo').style.display = 'none';
    document.getElementById('userInfoHeader-name').style.marginLeft = '20px';
    document.getElementById('userInfoHeader-name').textContent = groupName;
    document.getElementById('viewMembersButton').style.display = 'inline-block';
    document.getElementById('viewMembersButton').setAttribute('data-group-id', roomId);

    const res = await fetchgroupChatHistory(roomId);
    let previousDate = ''; // 用於記錄上一條訊息的日期

    res.forEach(item => {
        const {senderId, groupId, receiverIds, content, timestamp, read} = item;

        const messageDate = formatTimestampToDate(timestamp); // 獲取日期 (不包含時間)

        // 如果當前訊息的日期與前一條訊息的日期不同，插入日期標籤
        if (messageDate !== previousDate) {
            const dateElement = document.createElement('div');
            dateElement.classList.add('date-divider'); // 添加一個樣式來顯示日期
            dateElement.innerHTML = `<span>${messageDate}</span>`; // 將日期包裝在 span 中
            chatContent.appendChild(dateElement);

            // 更新 previousDate 為當前訊息的日期
            previousDate = messageDate;
        }


        if (senderId == fakeUserId) {
            creatMyMessageElement(item);
        } else {
            creatMemberMessageElement(item);
        }
        scrollToBottom();

    });
}

/**
 * DEMO-群組聊天紀錄項目
 */
function creatMyMessageElement(item) {
    const {senderId, groupId, receiverIds, content, timestamp, read} = item;
    const formattedTimestamp = formatTimestampToTime(timestamp);
    const element = document.createElement('div');


    if(content.endsWith('.png') || content.endsWith('.jpg')){
        element.classList.add('d-flex', 'justify-content-end', 'mb-3');
        element.innerHTML =
            `<div class="d-flex flex-column align-items-end">
                <div class="p-2 rounded">
                    <img src="${content}" alt="sticker" width="100px" height="100px">
                </div>
                <small class="text-muted">${formattedTimestamp}</small> <!-- 顯示時間戳 -->
                </div>`;
    }
    else{
        element.classList.add('d-flex', 'justify-content-end', 'mb-3');
        element.innerHTML = `<div class="d-flex flex-column align-items-end">
            <div class="bg-primary text-white p-2 rounded chat-bubble">
                <p class="mb-0">${content}</p>
            </div>
            <small class="text-muted">${formattedTimestamp}</small>
        </div>`;
    }


    document.getElementById('chatContent').appendChild(element);
}

function creatMemberMessageElement(item) {
    const {senderId, groupId, receiverIds, content, timestamp, read} = item;
    const formattedTimestamp = formatTimestampToTime(timestamp);
    const element = document.createElement('div');

    if(content.endsWith('.gif') || content.endsWith('.png')) {

        element.classList.add('d-flex', 'mb-3');
        element.innerHTML =
            `<div class="user-avatar me-3">
                        <img src="${port}/friends/users/${senderId}/profile-picture" alt="" class="rounded-circle">
                
                </div>
        <div class="d-flex flex-column">
            <div class="p-2 rounded">
                <img src="${content}" alt="sticker" width="100px" height="100px">
            </div>
            <small class="text-muted">${formattedTimestamp}</small>
        </div>`;
    }
    else{
        element.classList.add('d-flex', 'mb-3');
        element.innerHTML = `
    <div class="user-avatar me-3">
    <img src="${port}/friends/users/${senderId}/profile-picture" alt="Avatar" class="rounded-circle me-3">
            
             </div>
            <div class="d-flex flex-column">
                <div class="bg-light p-2 rounded chat-bubble">
                    <p class="mb-0">${content}</p>
                </div>
                <small class="text-muted">${formattedTimestamp}</small>
            </div>`;

    }

    document.getElementById('chatContent').appendChild(element);
}

/**
 * 查看群組成員
 */

export async function members(groupId){
    const res = await getGroupMember(groupId)
    const div = document.getElementById('members')
    div.innerHTML = '';
    res.forEach(item => {
        const{friendId, friendName} = item;
        const element = document.createElement('li');
        element.classList.add('list-group-item','d-flex','align-items-center');
        element.innerHTML = `
            <div class="user-avatar me-2">
                <img src="${port}/friends/users/${friendId}/profile-picture" alt="Avatar" class="rounded-circle me-3">
            </div>
            <div>
                <div class="user-name">${friendName}</div>
            </div>
        `
        div.appendChild(element);
    })

}


/**
 * DEMO-滾動到底部的功能
 */

export function scrollToBottom() {
    const chatContent = document.getElementById('chatContent');
    chatContent.scrollTop = chatContent.scrollHeight; // 將 scrollTop 設置為 scrollHeight
}



/**
 * DEMO-貼圖
 */
document.querySelectorAll('.sticker-panel img').forEach(function (sticker) {
    sticker.addEventListener('click', function () {
        const stickerUrl = sticker.src;  // 取得貼圖的 URL
        const pathOnly = stickerUrl.substring(stickerUrl.indexOf('/img')); // 取得 /img 開始的字串
        sendMessage(pathOnly);  // 調用 sendMessage 函數，傳遞貼圖的 URL
        document.getElementById('stickerPanel').style.display = 'none'; // 隱藏貼圖面板
    });
});


// 點擊笑臉按鈕切換貼圖面板
document.getElementById('emojiBtn').addEventListener('click', function (event) {
    event.stopPropagation();
    const stickerPanel = document.getElementById('stickerPanel');
    stickerPanel.style.display = (stickerPanel.style.display === 'none' || stickerPanel.style.display === '') ? 'flex' : 'none';
});

// 點擊頁面其他地方隱藏貼圖面板
document.addEventListener('click', function (event) {
    const stickerPanel = document.getElementById('stickerPanel');
    const emojiBtn = document.getElementById('emojiBtn');

    if (!stickerPanel.contains(event.target) && !emojiBtn.contains(event.target)) {
        stickerPanel.style.display = 'none';
    }
});

/**
 * Modal 彈出群組成員
 */
// TODO
document.querySelector('button[data-bs-target="#groupMembersModal"]').addEventListener('click', async function () {
    const groupId = this.getAttribute('data-group-id');
    console.log(groupId);
    await members(groupId);
});






