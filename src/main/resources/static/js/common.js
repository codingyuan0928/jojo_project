async function logout() {
    try {
        const shouldLogoutGoogle = window.confirm("是否同時登出 Google?");

        // 先進行 Google 登出
        if (shouldLogoutGoogle) {
            const googleLogoutResponse = await fetch('/auth/google/logout', {
                method: 'POST',
                credentials: 'include'
            });

            if (!googleLogoutResponse.ok) {
                throw new Error('Google 登出失敗');
            }

            const googleMessage = await googleLogoutResponse.text();
            window.alert(googleMessage);
        }

        // 再執行本地後端登出
        const response = await fetch('/users/logout', {
            method: 'GET',
            credentials: 'include'
        });

        if (!response.ok) {
            throw new Error('本地登出失敗，請稍後再試');
        }

        const message = await response.text();
        window.alert(message);

        // 完成登出後導航到登入頁面
        window.location = "/login";

    } catch (err) {
        console.error('登出失敗:', err);
        window.alert(err.message);
    }
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
