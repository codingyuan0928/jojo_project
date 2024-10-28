// window.addEventListener("DOMContentLoaded", () => {
//   const pageContainer = document.querySelector(".page-container");
//   pageContainer.style.display = "flex";
//   pageContainer.style.flexDirection = "column";
//   pageContainer.style.minHeight = "100vh";
//   const main = document.querySelector(".main");
//   main.style.flexGrow = "1";
//   console.log(123);
//   console.log("我應該要執行")
// });

function logout() {

    fetch('/users/logout', {
        method: 'GET',
        credentials: 'include'
    }).then(window.alert("登出成功!!!"))
    .catch(err => console.error('登出失敗:', err));
}

//navbar確認登入狀態
// window.addEventListener("load", async () => {
//   try {
//     const response = await fetch('/users/status', {
//       method: "GET",
//       credentials: "include", // 允許帶上 cookie
//     });
//
//     if (response.ok) {
//       const username = await response.text(); // 獲取純文本的用戶名
//
//       // 獲取 avatar 圖片
//       const avatarResponse = await fetch(
//         '/users/current/avatar',
//         {
//           method: "GET",
//           credentials: "include", // 允許帶上 cookie
//         }
//       );
//       const avatarBlob = await avatarResponse.blob();
//       const avatarUrl = URL.createObjectURL(avatarBlob);
//
//       // 打包成一個 object
//       const userData = {
//         username: username,
//         avatar: avatarUrl,
//       };
//
//       console.log("User Data:", userData);
//       updateNavbar(userData); // 使用API回傳的資料來更新navbar
//     } else if (response.status === 401) {
//       console.error("未授權，請先登入。");
//       alert("未授權，請先登入。");
//     }
//   } catch (err) {
//     console.error("Error during login status:", err);
//     alert("登入狀態異常，請稍後再試。");
//   }
//
//   // 設定登出行為
//   // 定義通用的登出邏輯
//   function bindLogoutButtons() {
//     const logoutBtn = document.getElementById("logout");
//     const logoutMD = document.getElementById("logout-md");
//
//     const handleLogout = async () => {
//       try {
//         const response = await fetch('/users/logout', {
//           method: "GET",
//           credentials: "include",
//         });
//
//         if (response.ok) {
//           alert("已登出");
//           updateNavbar(false); // 更新導覽列狀態
//           window.location.href = "/index.html";
//         } else {
//           alert("登出失敗，請稍後再試");
//         }
//       } catch (err) {
//         console.error("Error during logout:", err);
//         alert("登出異常，請稍後再試。");
//       }
//     };
//
//     if (logoutBtn) logoutBtn.addEventListener("click", handleLogout);
//     if (logoutMD) logoutMD.addEventListener("click", handleLogout);
//   }
//
//   async function checkLoginStatus() {
//     try {
//       const response = await fetch('/users/status', {
//         method: "GET",
//         credentials: "include", // 允許帶上 cookie
//       });
//
//       if (response.ok) {
//         const username = await response.text(); // 獲取純文本的用戶名
//
//         // 獲取 avatar 圖片
//         const avatarResponse = await fetch(
//           '/users/current/avatar',
//           {
//             method: "GET",
//             credentials: "include", // 允許帶上 cookie
//           }
//         );
//         const avatarBlob = await avatarResponse.blob();
//         const avatarUrl = URL.createObjectURL(avatarBlob);
//
//         const userData = {
//           username,
//           avatar: avatarUrl,
//         };
//
//         console.log("User Data:", userData);
//         updateNavbar(userData);
//       } else if (response.status === 401) {
//         console.error("未授權，請先登入。");
//       } else {
//         console.error("登入狀態錯誤", response.status);
//       }
//     } catch (err) {
//       console.error("Error during login status:", err);
//     }
//   }
//
//   function updateNavbar(userData) {
//     const loginBtn = document.getElementById("login");
//     const registerBtn = document.getElementById("register");
//     const notifyBtn = document.getElementById("notify");
//     const loginMD = document.getElementById("login-md");
//     const registerMD = document.getElementById("register-md");
//     const logoutBtn = document.getElementById("logout");
//     const userProfileBtn = document.getElementById("user-profile");
//     const logoutMD = document.getElementById("logout-md");
//     const userProfileMD = document.getElementById("user-profile-md");
//     if (userData) {
//       // 使用者已登入
//       loginBtn.textContent = "登出";
//       loginBtn.id = "logout";
//       loginBtn.href = "javascript:void(0);"; // 防止跳轉
//       registerBtn.innerHTML = `
//         <img src="${userData.avatar}" alt="User Avatar" class="login-user-avatar">
//         <span>${userData.username}</span>
//       `;
//       registerBtn.removeAttribute("data-bs-toggle", "modal");
//       registerBtn.removeAttribute("data-bs-target", "#staticBackdrop");
//
//       registerBtn.id = "user-profile";
//       registerBtn.href = "/user_profile.html";
//
//       notifyBtn.classList.remove("d-none");
//       notifyBtn.href = "/user_profile.html?section=notifications";
//
//       // RWD
//       loginMD.innerHTML = `<img src='/images/common/navbar-logout.svg' alt="logout icon"><span>登出</span>`;
//       loginMD.id = "logout-md";
//       loginMD.href = "javascript:void(0);";
//       registerMD.innerHTML = `
//         <img src="${userData.avatar}" alt="User Avatar" class="login-user-avatar" style="margin-left:0">
//         <span>${userData.username}</span>
//       `;
//       registerMD.removeAttribute("data-bs-toggle", "modal");
//       registerMD.removeAttribute("data-bs-target", "#staticBackdrop");
//       registerMD.id = "user-profile-md";
//       registerMD.href = "/user_profile.html?section=profile";
//
//       bindLogoutButtons(); // 綁定登出按鈕
//     } else {
//       // 使用者未登入
//       logoutBtn.textContent = "登入";
//       logoutBtn.id = "login";
//       logoutBtn.href = "/login.html";
//       userProfileBtn.textContent = "註冊";
//       userProfileBtn.setAttribute("data-bs-toggle", "modal");
//       userProfileBtn.setAttribute("data-bs-target", "#staticBackdrop");
//       notifyBtn.classList.add("d-none");
//
//       logoutMD.innerHTML = `<img src='/images/common/navbar-login.svg' alt="login icon"><span>登入</span>`;
//       logoutMD.id = "login-md";
//       logoutMD.href = "/login.html";
//       userProfileMD.textContent = "註冊";
//       userProfileMD.setAttribute("data-bs-toggle", "modal");
//       userProfileMD.setAttribute("data-bs-target", "#staticBackdrop");
//     }
//   }
//
//   // 當頁面加載時檢查登入狀態
//   window.addEventListener("load", checkLoginStatus);
// });
