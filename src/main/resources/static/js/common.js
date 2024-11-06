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
            window.alert(message);
            window.location.href = '/login';
        })
        .catch(err => {
            console.error('登出失敗:', err);
            window.alert(err.message);
        });
}

function vendorLogout() {
    fetch('/api/vendors/logout', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('登出失敗，請稍後再試');
            }
        })
        .then(message => {
            window.alert(message);
            window.location.href = '/login';
        })
        .catch(err => {
            console.error('登出失敗:', err);
            window.alert(err.message);
        });
}

function adminLogout() {
    fetch('/api/admin/logout', {
        method: 'GET',
        credentials: 'include'
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('登出失敗，請稍後再試');
            }
        })
        .then(message => {
            window.alert(message);
            window.location.href = '/admin/administrator_login';
        })
        .catch(err => {
            console.error('登出失敗:', err);
            window.alert(err.message);
        });
}
