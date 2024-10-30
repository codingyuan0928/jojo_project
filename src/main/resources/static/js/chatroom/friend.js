import {
    fetchFriendList,
    acceptFriendInvitation,
    deleteFriendInvitation,
    putInvitation,
    searchUsers,
} from './friendApi.js';
import {openPrivateChatWindow, privateList} from './chat.js'
import {addChatroom} from "./chatApi.js";
/**
 * 聊天
 */
export async function chatWithFriend(friendId){
    isPrivateChate = true;
    console.log("切換到私聊: ", isPrivateChate);

    const privateChatTab = document.querySelector("#private-chat-tab");

    // 使用 Bootstrap 的 Tab API 切換到 "邀請" tab
    const tab = new bootstrap.Tab(privateChatTab);
    tab.show(); // 顯示目標 tab




    let newActiveItem = document.querySelector(`[data-friend-id="${friendId}"]`);
    if (newActiveItem) {
        document.querySelectorAll('.chatItem.active').forEach(chatItem => {
            chatItem.classList.remove('active');
        });
        document.querySelectorAll('.groupChatItem.active').forEach(chatItem => {
            chatItem.classList.remove('active');
        });
        newActiveItem.classList.add('active');
        openPrivateChatWindow(friendId);
    }
    else{

        await addChatroom(fakeUserId,friendId);
        await privateList(fakeUserId);
        await openPrivateChatWindow(friendId);

        document.querySelectorAll('.chatItem.active').forEach(chatItem => {
            chatItem.classList.remove('active');
        });
        document.querySelector(`[data-friend-id="${friendId}"]`).classList.add('active');


        isPrivateChate = true;
        currentChatId = friendId;

    }



}

/**
 * 取消邀請
 */
export async function handleCancelFriend(){
    currentSelectId = this.dataset.userId; // 從當前按鈕中獲取 data-user-id
    console.log(`準備取消邀請好友: ${currentSelectId}`);
    await deleteFriendInvitation(fakeUserId, currentSelectId)

    // 重新渲染
    await getFriendList(fakeUserId);
    await getFriendInvitationByMeList(fakeUserId);
    await getPendingFriendRequests(fakeUserId);

    // 重新搜尋
    const query = document.querySelector('.modalSearchInput').value.trim(); // 去除空格
    searchUsers(query); // 呼叫查詢函數

    // 更改搜尋視窗中按鈕 取消邀請 ->加好友
    changBtnToAdd(this);
}

/**
 * 加好友
 */
export async function handleAddFriend(){
    const userId = this.dataset.userId;
    try {
        // 調用加好友的 API，並等待其成功執行
        await putInvitation(fakeUserId, userId);

        // 重新選染
        getFriendList(fakeUserId);
        getFriendInvitationByMeList(fakeUserId);
        getPendingFriendRequests(fakeUserId);

        // 加好友 -> 取消邀請
        changBtnToCancel(this);
    } catch (error) {
        console.error('加好友失敗:', error);
    }
}

/**
 * 接受好友
 */
export async function handleAcceptedFriend(){
    currentSelectId = this.dataset.userId; // 從當前按鈕中獲取 data-user-id
    console.log(`準備接受好友: ${currentSelectId}`);
    try {
        // 調用接受的 API，並等待其成功執行
        await acceptFriendInvitation(currentSelectId, fakeUserId);

        // 重新選染
        getFriendList(fakeUserId);
        getFriendInvitationByMeList(fakeUserId);
        getPendingFriendRequests(fakeUserId);

    } catch (error) {
        console.error('接受失敗:', error);
    }

}

/**
 * 拒絕邀請
 */
export async function handleRejectFriend(){
    currentSelectId = this.dataset.userId; // 從當前按鈕中獲取 data-user-id
    console.log(`準備拒絕好友: ${currentSelectId}`);
    // 顯示 Toast
    const rejectToast = new bootstrap.Toast(document.getElementById('rejectToast'));
    rejectToast.show();
}

/**
 * 刪除好友
 */
export function handleDeleteFriend(){
    currentSelectId = this.dataset.userId; // 從當前按鈕中獲取 data-user-id
    console.log(`準備刪除好友: ${currentSelectId}`);

    // 顯示 Toast
    const unfriendToast = new bootstrap.Toast(document.getElementById('unfriendToast'));
    unfriendToast.show();
}

/**
 * 刪除(提醒視窗)
 */
document.getElementById('unfriendAction').addEventListener('click', async function () {
    await deleteFriendInvitation(fakeUserId, currentSelectId)
    // 關閉 Toast
    const unfriendToast = new bootstrap.Toast(document.getElementById('unfriendToast'));
    unfriendToast.dispose();


    // 重新渲染
    await getFriendList(fakeUserId);
    await getFriendInvitationByMeList(fakeUserId);
    await getPendingFriendRequests(fakeUserId);

    // 重新搜尋
    const query = document.querySelector('.modalSearchInput').value.trim(); // 去除空格
    searchUsers(query); // 呼叫查詢函數

    // 更改搜尋視窗中按鈕 刪除/聊天->加好友
    changBtnToAdd(this);
});

/**
 * 拒絕(提醒視窗)
 */
document.getElementById('rejectAction').addEventListener('click', async function () {
    await deleteFriendInvitation(fakeUserId, currentSelectId)
    // 關閉 Toast
    const rejectToast = new bootstrap.Toast(document.getElementById('rejectToast'));
    rejectToast.dispose();


    // 重新渲染
    await getFriendList(fakeUserId);
    await getFriendInvitationByMeList(fakeUserId);
    await getPendingFriendRequests(fakeUserId);

    // 重新搜尋
    const query = document.querySelector('.modalSearchInput').value.trim(); // 去除空格
    searchUsers(query); // 呼叫查詢函數

    // 更改搜尋視窗中按鈕 拒絕->加好友
    changBtnToAdd(this);
});


/**
 * 更改按鈕 加好友 -> 取消邀請
 */
export function changBtnToCancel(button) {
    console.log(button.textContent);

    // 更改按鈕樣式和文本
    button.classList.remove('btn-primary');
    button.classList.add('btn-outline-secondary');
    button.textContent = '取消邀請';

    button.removeEventListener('click', handleAddFriend);
    button.addEventListener('click',handleCancelFriend);

}

/**
 * 更改按鈕成 刪除/聊天 -> 加好友
 */
export function changBtnToAdd(button) {

    // 更改按鈕樣式和文本
    button.classList.remove('btn-outline-secondary');
    button.classList.add('btn-primary');
    button.classList.add('addBtn');
    button.textContent = '加好友';

}

/**
 *  好友列表
 */
export async function getFriendList(id) {
    if (!id) {
        console.error('無法取得當前用戶名');
        return;
    }

    try {
        const data = await fetchFriendList(id,'accepted',null);
        const friendsList = document.querySelector('.friendsList');
        friendsList.innerHTML = '';
        if(data.length === 0){
            // 當沒有好友時，顯示提示訊息
            const noFriendMessage = document.createElement('p');
            noFriendMessage.className = 'no-friend-message';
            noFriendMessage.textContent = '快去添加你的第一位好友吧！';
            friendsList.appendChild(noFriendMessage);
        }else{
            data.forEach(item => {
                console.log(item);
                const{ friendAvatar, friendId, friendName } = item;
                friendsList.appendChild(createFriendListElement(friendAvatar, friendId, friendName));
            });
        }

    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
    }
}

/**
 * 好友列表元件
 */
function createFriendListElement(friendAvatar, friendId, friendName) {
    const element = document.createElement('div');
    element.className = 'list-group-item d-flex align-items-center';
    element.innerHTML = `
        <img src="${port}/friends/users/${friendId}/profile-picture" alt="Avatar" class="rounded-circle me-3" style="width: 40px; height: 40px;">        
        <div class="user-name me-2">
            <div class="user-name me-2">${friendName}</div>
        </div>
        <button data-user-id="${friendId}" class="unfriendToastTrigger btn btn-warning btn-sm ms-auto deleteBtn me-3">
            刪除
        </button>
        <button class="btn btn-primary btn-sm ms-auto chatButton" data-user-id="${friendId}">聊天</button>
    `;

    element.querySelector('.chatButton').addEventListener('click', function() {
        chatWithFriend(friendId);
    });


    element.querySelector('.deleteBtn').addEventListener('click',handleDeleteFriend);

    return element;
}

/**
 * 你送出的交友邀請
 */
export async function getFriendInvitationByMeList(id) {
    if (!id) {
        console.error('無法取得當前用戶名');
        return;
    }

    try {
        const data = await fetchFriendList(id,'pending','true');
        const myInvitationList = document.getElementById('myInvitationList');
        myInvitationList.innerHTML = '';

        if(data.length === 0){
            // 當沒有送出任何邀請時，顯示提示訊息
            const noInviteMessage = document.createElement('p');
            noInviteMessage.className = 'no-invite-message';
            noInviteMessage.textContent = '還沒送出邀請，去交個朋友！';
            myInvitationList.appendChild(noInviteMessage);
        }else{
            data.forEach(item => {
                console.log(item);
                const{ friendAvatar, friendId, friendName} = item;
                myInvitationList.appendChild(createFriendInvitationByMeElement(friendId, friendName));
            });
        }

    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
    }
}

/**
 * 你送出的交友邀請元件
 */
function createFriendInvitationByMeElement(friendId, friendName) {
    const element = document.createElement('div');
    element.className = 'list-group-item d-flex align-items-center';
    element.innerHTML = `
        <div class="user-avatar me-2">
            <img src="${port}/friends/users/${friendId}/profile-picture" alt="Avatar" class="rounded-circle me-3">
           
        </div>
        <div>
            <div class="user-name">${friendName}</div>
        </div>
        <button data-user-id="${friendId}" class="btn btn-outline-secondary btn-sm ms-auto cancelBtn">取消邀請</button>
    `;

    // 將事件綁定到「取消邀請」按鈕
    const cancelBtn = element.querySelector('.cancelBtn');
    if (cancelBtn) {
        cancelBtn.addEventListener('click', handleCancelFriend);
    }

    return element;
}

/**
 * 邀請名單
 */
export async function getPendingFriendRequests(id) {
    if (!id) {
        console.error('無法取得當前用戶名');
        return;
    }

    try {
        const data = await fetchFriendList(id,'pending','false');
        const friendInvite = document.getElementById('friendInvite');
        friendInvite.innerHTML = '';


        if (data.length === 0) {
            // 當沒有收到任何邀請時，顯示提示訊息
            const noInviteMessage = document.createElement('p');
            noInviteMessage.className = 'no-invite-message';
            noInviteMessage.textContent = '目前還沒有邀請，不如主動出擊？';
            friendInvite.appendChild(noInviteMessage);
        } else {
            // 有邀請時，插入邀請項目
            data.forEach(item => {
                console.log(item);
                const { friendAvatar, friendId, friendName } = item;
                friendInvite.appendChild(createFriendInvitationElement(friendAvatar, friendId, friendName));
            });
        }

    } catch (error) {
        console.error('There was a problem with the fetch operation:', error);
    }
}

/**
 * 邀請名單元件
 */
function createFriendInvitationElement(friendAvatar, friendId, friendName) {
    const element = document.createElement('div');
    element.className = 'list-group-item d-flex align-items-center';
    element.innerHTML = `
        <div class="user-avatar me-2">
            <img src="${port}/friends/users/${friendId}/profile-picture" alt="Avatar" class="rounded-circle me-3">
                
        </div>
        <div class="user-name">
            <div class="user-name">${friendName}</div>
        </div>
        <button data-user-id="${friendId}" class="btn btn-warning btn-sm ms-auto rejectBtn me-3">拒絕</button>
        <button data-user-id="${friendId}" class="btn btn-success btn-sm ms-auto acceptBtn">接受</button>
    `;

    // 將事件綁定到「拒絕」按鈕
    const rejectBtn = element.querySelector('.rejectBtn');
    if (rejectBtn) {
        rejectBtn.addEventListener('click', handleRejectFriend);
    }

    // 將事件綁定到「接受」按鈕
    const acceptBtn = element.querySelector('.acceptBtn');
    if (acceptBtn) {
        acceptBtn.addEventListener('click', handleAcceptedFriend);
    }

    return element;
}


/**
 * 搜尋使用者
 */
document.querySelectorAll('.search-input').forEach(input => {
    input.addEventListener('click', function() {
        var myModal = new bootstrap.Modal(document.querySelector('.searchModal'));
        myModal.show();
    });
});


// 監聽模態框關閉事件
const searchModal = document.querySelector('.searchModal');
searchModal.addEventListener('hidden.bs.modal', function () {
    // 清空 searchResult 的內容
    const searchResults = document.getElementById('searchResults');

    if (searchResults) {
        searchResults.innerHTML = ''; // 清空內容
    } else {
        console.log('無法找到 searchResults 元素');
    }
    searchResults.innerHTML = '<p class="no-search">No recent searches</p>';

    // 清空搜索輸入框
    const modalSearchInput = document.querySelector('.modalSearchInput');
    modalSearchInput.value = '';  // 清空輸入框的值
});
document.addEventListener("DOMContentLoaded", function () {
    // 取得目標 tab
    //const friendsRequestTab = document.querySelector("#friends-request-tab");
    const privateChatTab = document.querySelector("#private-chat-tab");

    // 取得自訂按鈕
    const customButton = document.querySelector("#custom-button");

    // 監聽自訂按鈕的點擊事件
    if (customButton) {
        customButton.addEventListener("click", function () {
            const tab = new bootstrap.Tab(privateChatTab);
            tab.show();
        });
    }
});




