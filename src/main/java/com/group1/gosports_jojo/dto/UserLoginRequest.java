package com.group1.gosports_jojo.dto;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserLoginRequest {

    @NotBlank(message = "Email不能空白")
    @Email(message = "Email格式不正確")
    private String email;

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
