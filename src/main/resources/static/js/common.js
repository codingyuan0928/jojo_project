function logout() {
    fetch('/users/logout', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                return response.text(); // 取得後端回傳的純文字
            } else {
                throw new Error('登出失敗，請稍後再試');
            }
        })
        .then(message => {
            window.alert(message); // 使用後端返回的文字作為提示訊息
            window.location.href = '/login'; // 可選，登出後導向登入頁面
        })
        .catch(err => {
            console.error('登出失敗:', err);
            window.alert(err.message); // 顯示錯誤訊息
        });
}

function vendorLogout() {
    fetch('/vendors/logout', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                return response.text(); // 取得後端回傳的文字
            } else {
                throw new Error('登出失敗，請稍後再試');
            }
        })
        .then(message => {
            window.alert(message); // 顯示後端回傳的訊息
            window.location.href = '/login'; // 可選，登出後導向登入頁面
        })
        .catch(err => {
            console.error('登出失敗:', err);
            window.alert(err.message);
        });
}
