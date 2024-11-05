export class EmailComponent {
    constructor({ emailButtonId, validateButtonId, emailInputId, authCodeInputId, validateIconId }) {
        // 使用參數來綁定不同的元素
        this.emailButton = document.getElementById(emailButtonId);
        this.validateButton = document.getElementById(validateButtonId);
        this.emailInput = document.getElementById(emailInputId);
        this.authCodeInput = document.getElementById(authCodeInputId);
        this.validateIcon = document.getElementById(validateIconId);

        // 檢查所有元素是否存在
        if (this.emailButton && this.validateButton && this.emailInput && this.authCodeInput && this.validateIcon) {
            this.initEventListeners();
        } else {
            console.warn("某些必要的元素未找到，請確認傳遞的 ID 是否正確。");
        }
    }

    initEventListeners() {
        this.emailButton.addEventListener("click", () => this.sendEmail());
        this.validateButton.addEventListener("click", () => this.validateCode());
    }

    sendEmail() {
        const email = this.emailInput.value;
        this.emailButton.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> 正在寄送...`;
        this.emailButton.disabled = true;

        fetch("/auth/sendAuthCode", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        })
            .then(res => res.text())
            .then(data => {
                window.alert(data);
                this.emailButton.innerHTML = "寄送驗證信";
                this.emailButton.disabled = false;
            })
            .catch(err => {
                console.error("發送驗證碼時出現錯誤：", err);
                window.alert("發送失敗，請稍後再試。");
                this.emailButton.innerHTML = "寄送驗證信";
                this.emailButton.disabled = false;
            });
    }

    validateCode() {
        const email = this.emailInput.value;
        const authCode = this.authCodeInput.value;

        fetch("/auth/checkAuthCode", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email, authCode: authCode })
        })
            .then(res => {
                if (res.ok) {
                    this.validateIcon.innerHTML = `<i class="fa-solid fa-check" style="color: #63E6BE;"></i>`;
                    this.emailInput.readOnly = true;
                    this.emailButton.disabled = true;
                    this.authCodeInput.disabled = true;
                    this.validateButton.disabled = true;
                } else if (res.status === 401) {
                    this.validateIcon.innerHTML = `<i class="fa-solid fa-xmark" style="color: #fe0000;"></i>`;
                }
                return res.text();
            })
            .then(data => {
                window.alert(data);
            });
    }
}

// 使用時傳遞不同的 ID 參數
document.addEventListener("DOMContentLoaded", () => {
    new EmailComponent({
        emailButtonId: "custom-email-send",
        validateButtonId: "custom-validate",
        emailInputId: "custom-email",
        authCodeInputId: "custom-authCode",
        validateIconId: "custom-email-validate-icon"
    });
});
