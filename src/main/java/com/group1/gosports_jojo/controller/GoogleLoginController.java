package com.group1.gosports_jojo.controller;

import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.service.impl.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class GoogleLoginController {

    @Autowired
    private GoogleAuthService googleAuthService;


    @GetMapping("/auth/google")
    public String googleLogin(HttpSession session) {
        UserVO user = (UserVO) session.getAttribute("userAccount");
        if (user != null && user.getRefreshToken() != null) {
            System.out.println("還有refreshTOken");
            return "redirect:/";
        }

        String url = googleAuthService.getGoogleAuthURL();
        return "redirect:" + url;
    }

    @GetMapping("/auth/google/callback")
    public String googleCallback(@RequestParam("code") String code, HttpSession session) {
       UserVO user = googleAuthService.exchangeCodeForToken(code);
        if (user != null) {
            session.setAttribute("userAccount", user); // 存入 SESSION
        }
        return "redirect:/"; // 登入後的頁面
    }


}