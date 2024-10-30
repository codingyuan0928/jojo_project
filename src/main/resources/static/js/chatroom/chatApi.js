/**
 * DEMO-取得一對一聊天室紀錄
 */
export async function fetchPrivateChatHistory(userId,friendId) {
    const url = `${port}/messages/privateChatroom?userId=${userId}&friendId=${friendId}`;
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        });
}


/**
 * DEMO-取得群組聊天室紀錄
 */
export async function fetchgroupChatHistory(fakeRoomId){
    const url = `${port}/messages/groupChatroom?groupId=${fakeRoomId}`;
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        });
}

/**
 * DEMO-取得一對一聊天室清單
 */
export async function privateChatList(userId){
    const url = `${port}/messages/privateList?userId=${userId}`;
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        });
}

/**
 * DEMO-取得群組聊天清單
 */
export async function groupChatList(fakUserId){
    const url = `${port}/messages/groupList?userId=${fakUserId}`;
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        });
}

/**
 * DEMO-刪除私聊未讀提醒
 */
export function deletePrivateUnread(chatRoom, userId){
    const url = `${port}/messages/unreadMessages/chatRoom/${chatRoom}/field/${userId}`;
    return fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .then(data => {
            // 隱藏 unread-badge
            const unreadBadge = document.querySelector(`.unread-badge[data-chatroom-id="${chatRoom}"]`);

            if (unreadBadge) {
                unreadBadge.style.visibility = 'hidden'; // 隱藏特定的未讀標記
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

/**
 * DEMO-刪除群聊未讀提醒
 */
export function deleteGroupUnread(groupId, userId){
    const url = `${port}/messages/unreadMessages/group/${groupId}/field/${userId}`;
    return fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .then(data => {
            // 隱藏 unread-badge
            const unreadBadge = document.querySelector(`.unread-badge[data-room-id="${groupId}"]`);

            if (unreadBadge) {
                unreadBadge.style.visibility = 'hidden'; // 隱藏特定的未讀標記
            }
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

/**
 * DEMO-新增空的聊天室
 */
export function addChatroom(senderId, receiverId){
    const url = `${port}/messages/chatroom?senderId=${senderId}&receiverId=${receiverId}`;
    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .catch(error => {
            console.error('There was a problem with the fetch operation:', error);
        });
}

/**
 * 群組成員清單
 */
export function getGroupMember(groupId){
    const url = `${port}/friends/${groupId}`;
    return fetch(url)
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok ' + response.statusText);
        }
        return response.json();
    })
    .catch(error => {
        console.error('There was a problem with the fetch operation:', error);
    })
}


