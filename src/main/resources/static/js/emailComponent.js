export class EmailComponent {
    constructor(emailButtonId, validateButtonId, emailInputId, authCodeInputId, validateIconId) {
        this.emailButton = document.getElementById(emailButtonId);
        this.validateButton = document.getElementById(validateButtonId);
        this.emailInput = document.getElementById(emailInputId);
        this.authCodeInput = document.getElementById(authCodeInputId);
        this.validateIcon = document.getElementById(validateIconId);

        this.initEventListeners();
    }

    initEventListeners() {
        this.emailButton.addEventListener("click", () => this.sendEmail());
        this.validateButton.addEventListener("click", () => this.validateCode());
    }

    sendEmail() {
        const email = this.emailInput.value;
        this.emailButton.innerHTML = `<i class="fa-solid fa-spinner fa-spin"></i> 正在寄送...`;
        this.emailButton.disabled = true;

        fetch("/users/sendAuthCode", {
            method: "POST",
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ email: email })
        })
            .then(res => res.text())
            .then(data => {
                window.alert(data);
                this.emailButton.innerHTML = "我是寄信按鈕";
                this.emailButton.disabled = false;
            });
    }

    validateCode() {
        const email = this.emailInput.value;
        const authCode = this.authCodeInput.value;

        fetch("/users/checkAuthCode", {
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

document.addEventListener("DOMContentLoaded", () => {
    const emailButton = document.getElementById("email-send");
    const validateButton = document.getElementById("validate");
    const emailInput = document.getElementById("email");
    const authCodeInput = document.getElementById("authCode");
    const validateIcon = document.getElementById("email-validate-icon");

    if (emailButton && validateButton && emailInput && authCodeInput && validateIcon) {
        new EmailComponent("email-send", "validate", "email", "authCode", "email-validate-icon");
    }
});