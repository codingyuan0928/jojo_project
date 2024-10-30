window.port = 'http://localhost:8083';
window.stompClient = null;
window.socket = null;
window.fakeUserId  = sessionStorage.getItem("userId");
console.log('從 sessionStorage 取得 userId:',fakeUserId);
window.fakeReceiver = null;
window.currentChatId = null;  // 用來記錄當前正在查看的聊天ID
window.currentSelectId = null;  // 用來記錄當前點擊的使用者 (用於加好友功能)
window.isPrivateChate = true;
window.chatGroupId = null; //正在的群聊聊天室
window.fakUserIdInGroups = []; // 當前使用者所在哪些群組;