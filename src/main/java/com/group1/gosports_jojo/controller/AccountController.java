package com.group1.gosports_jojo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {

    @GetMapping("/account-suspended")
    public String accountSuspended(Model model) {
        model.addAttribute("message", "您的帳戶已被停權。如有疑問，請聯絡客服。");
        return "account-suspended";
    }
}
