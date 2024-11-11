package com.group1.gosports_jojo.controller;

import com.group1.gosports_jojo.entity.User;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.service.impl.GoogleAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

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

    @PostMapping("/auth/google/logout")
    public ResponseEntity<String> logoutGoogle(HttpSession session) {
        // 從 session 中取得 userAccount，這裡包含 accessToken 或 refreshToken
        UserVO userAccount = (UserVO) session.getAttribute("userAccount");

        if (userAccount == null) {
            System.out.println("未找到用戶資訊");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未找到用戶資訊");
        }

        // 從 userAccount 中獲取 accessToken
        String accessToken = userAccount.getAccessToken();

        if (accessToken == null) {
            System.out.println("未找到 Google 登入令牌");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("未找到 Google 登入令牌");
        }

        String revokeUrl = "https://oauth2.googleapis.com/revoke?token=" + accessToken;

        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl, null, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok("Google 登出成功");
            } else {
                System.out.println("Google 登出失敗");
                return ResponseEntity.status(response.getStatusCode()).body("Google 登出失敗");
            }
        } catch (Exception e) {
            System.out.println("登出過程中出錯");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("登出過程中出錯");
        }
    }

}

