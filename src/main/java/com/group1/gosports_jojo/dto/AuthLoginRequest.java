package com.group1.gosports_jojo.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class AuthLoginRequest {
    @Size(max = 50, message = "Email 長度不能超過 50 字元")
    @NotBlank(message = "Email不能空白")
    @Email(message = "Email格式不正確")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9!@#\\$%\\^&\\*]{6,20}$", message = "密碼必須是6到20碼的英文字母、數字或特殊符號")
    @NotBlank(message = "密碼不能空白")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
