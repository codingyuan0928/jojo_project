// import {connectWebSocket} from "./websocket.js";
//
// document.getElementById('confirm').addEventListener('click', function () {
//
//     // 獲取 input 欄位中的值
//     const userId = document.getElementById('currentUserId').value;
//
//     // 確認 userId 是否有效
//     if (!userId) {
//         document.getElementById('loginResult').innerHTML = '<p>請輸入有效的使用者 ID。</p>';
//         return;
//     }
//
//     // 使用 fetch 呼叫後端 API
//     fetch(`/login?userId=${userId}`, {
//         method: 'GET'
//     })
//         .then(response => response.json()) // 解析返回的 JSON 數據
//         .then(user => {
//             // 處理成功的登入響應
//             if (user) {
//                 document.getElementById('loginResult').innerHTML = `
//                 <p>登入成功！使用者名稱: ${user.username}</p>
//             `;
//                 fakeUserId = userId;
//                 connectWebSocket();
//
//             } else {
//                 document.getElementById('loginResult').innerHTML = '<p>找不到此使用者。</p>';
//             }
//         })
//         .catch(error => {
//             // 處理錯誤
//             console.error('Error:', error);
//             document.getElementById('loginResult').innerHTML = '<p>登入失敗，請稍後再試。</p>';
//         });
//
// });