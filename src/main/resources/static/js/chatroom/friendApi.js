import {handleAddFriend, handleDeleteFriend, handleCancelFriend, handleRejectFriend,chatWithFriend} from './friend.js'

/**
 * DEMO-加好友 API
 */
export async function putInvitation(senderId, receiverId) {
    const url = `${port}/friends/invitations?senderId=${encodeURIComponent(senderId)}&receiverId=${encodeURIComponent(receiverId)}`;


    try {
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error('Network response was not ok: ' + response.statusText);
        }

        return response.text();
    } catch (error) {
        console.error('Error:', error);
        throw error; // 拋出錯誤以便上層處理
    }
}

/**
 * DEMO-模糊查詢
 */
let debounceTimer; // 用於防抖的計時器

// 防抖函數，延遲發送請求
function debounce(func, delay) {
    return function(...args) {
        clearTimeout(debounceTimer);
        debounceTimer = setTimeout(() => func.apply(this, args), delay);
    };
}
export async function searchUsers(query) {
    const input = document.querySelector('.modalSearchInput').value;

    if (!query || query.trim() === '') {
        console.log('請輸入查詢條件');
        return;
    }

    const url = `${port}/friends/search?userId=${fakeUserId}&username=${input}`;


    try {

        const response = await fetch(url);


        // 將回傳的 JSON 轉為物件
        const users = await response.json();

        // 清空現有的結果
        const searchResults = document.getElementById('searchResults');
        searchResults.innerHTML = '';
        console.log(searchResults);
        if (searchResults) {
            searchResults.innerHTML = ''; // 清空內容
        } else {
            console.log('無法找到 searchResults 元素');
        }

        // 將查詢結果顯示在頁面上
        users.forEach(user => {

const{userId,username,avatar,relationshipStatus,initiatorByMe} = user;

const element = document.createElement('div');
            element.className = 'mt-3 list-group-item d-flex align-items-center';

            let customBtn = null;

            switch(relationshipStatus) {
                case 'PENDING':
                    if (initiatorByMe) {
                        customBtn = `<button data-user-id=${userId} class="cancelBtn btn btn-outline-secondary btn-sm ms-auto">取消邀請</button>`;
                    } else {
                        customBtn = `<button class="btn btn-warning btn-sm ms-auto rejectBtn me-3" data-user-id=${userId}>拒絕</button>
                                     <button class="btn btn-success btn-sm ms-auto acceptBtn" data-user-id=${userId}>接受</button>`
                    }
                    break;
                case 'ACCEPTED':
                    customBtn =`<button data-user-id=${userId} class="btn btn-warning btn-sm ms-auto deleteBtn me-3">
                                    刪除
                                </button>
                                <button class="btn btn-primary btn-sm ms-auto chatButton" data-user-id=${userId}>聊天</button>
                                `
                    break;

                case 'NO RELATION':
                    customBtn = `<button data-user-id=${userId} class="addBtn btn btn-primary btn-sm ms-auto">加好友</button>`

                    break;
            }

            element.innerHTML = `
        <img src="${port}/friends/users/${userId}/profile-picture" alt="Avatar" class="rounded-circle me-3" style="width: 40px; height: 40px;">        
        <div class="user-name me-2">
            <div class="user-name me-2">${username}</div>
        </div>
        ${customBtn}
    `;


            // 根據不同的按鈕綁定不同的事件處理器

            // 處理加好友按鈕
            const addBtn = element.querySelector('.addBtn');
            if (addBtn) {
                addBtn.addEventListener('click', handleAddFriend);
            }
            //處理刪除邀請按鈕
            const deleteBtn = element.querySelector('.deleteBtn');
            if (deleteBtn) {
                deleteBtn.addEventListener('click', handleDeleteFriend);
            }
            //處理取消邀請按鈕
            const cancelBtn = element.querySelector('.cancelBtn');
            if (cancelBtn) {
                cancelBtn.addEventListener('click', handleCancelFriend);
            }

            //處理取拒絕按鈕
            const rejectBtn = element.querySelector('.rejectBtn');
            if (rejectBtn) {
                rejectBtn.addEventListener('click', handleRejectFriend);
            }

            //處理聊天按鈕
            const chatButton = element.querySelector('.chatButton');
            if (chatButton) {
                chatButton.addEventListener('click', function (){
                    chatWithFriend(userId)
                });
            }


            searchResults.appendChild(element);

        });
    } catch (error) {
        console.error('查詢失敗:', error);
    }
}


// 綁定輸入事件，並使用防抖來減少請求次數
document.querySelector('.modalSearchInput').addEventListener('input', debounce(function() {
    const query = this.value.trim(); // 去除空格
    searchUsers(query); // 呼叫查詢函數
}, 300)); // 300ms 延遲發送請求




/**
 * DEMO-好友 API
 */
export async function fetchFriendList(currentUserId, status = 'accepted', invitationByMe = null) {
    let url = `${port}/friends?userId=${encodeURIComponent(currentUserId)}&status=${encodeURIComponent(status)}`;
    // 根據 invitationByMe 值判斷是否添加此參數
    if (invitationByMe !== null) {
        url += `&invitationByMe=${encodeURIComponent(invitationByMe)}`;
    }
    const response = await fetch(url);

    if (!response.ok) {
        throw new Error('Network response was not ok ' + response.statusText);
    }


    return response.json();
}

/**
 * DEMO-接受好友邀請 API
 */
export function acceptFriendInvitation(userId, currentUserId) {
    const url = `${port}/friends/invitations?senderId=${userId}&receiverId=${currentUserId}`;
    return fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            const contentType = response.headers.get("content-type");
            if (contentType && contentType.includes("application/json")) {
                return response.json();  // 如果是 JSON 格式，解析 JSON
            } else {
                return response.text();  // 否則解析為文字
            }
        });
}

/**
 * DEMO-刪除/取消邀請/拒絕 API
 */
export function deleteFriendInvitation(senderId, receiverId){
    const url = `${port}/friends/invitations?senderId=${senderId}&receiverId=${receiverId}`;
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
        });
}

/**
 * DEMO-所有好友狀態
 */
export function allFriendsStatus(){
    const url = `${port}/friend`;
    return fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        });
}