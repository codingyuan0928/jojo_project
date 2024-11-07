package com.group1.gosports_jojo.service.impl;

import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleAuthService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.redirect.uri}")
    private String redirectUri;

    private static final String AUTH_URI = "https://accounts.google.com/o/oauth2/auth";
    private static final String TOKEN_URI = "https://oauth2.googleapis.com/token";
    private static final String USERINFO_URI = "https://openidconnect.googleapis.com/v1/userinfo";

    @Autowired
    private UserService userService;

    // 構建 Google 授權 URL
    public String getGoogleAuthURL() {
        return AUTH_URI +
                "?client_id=" + clientId +
                "&redirect_uri=" + redirectUri +
                "&response_type=code" +
                "&scope=openid%20profile%20email"+
                "&access_type=offline";
    }

    // 交換授權碼並儲存用戶資訊和 Token 到資料庫
    public UserVO exchangeCodeForToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("client_secret", clientSecret);
        params.put("redirect_uri", redirectUri);
        params.put("code", code);
        params.put("grant_type", "authorization_code");

        ResponseEntity<Map> response = restTemplate.postForEntity(TOKEN_URI, params, Map.class);
        Map<String, Object> tokenData = response.getBody();

        if (tokenData != null) {
            String accessToken = (String) tokenData.get("access_token");
            String refreshToken = (String) tokenData.get("refresh_token");
            Integer expiresIn = (Integer) tokenData.get("expires_in");

            // 取得用戶資訊
            Map<String, Object> userInfo = getUserInfo(accessToken);

            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");
            String avatarUrl = (String) userInfo.get("picture");

            // 儲存或更新用戶資訊和 Token 到 users 表
         return saveOrUpdateUserWithToken(email, name, avatarUrl, accessToken, refreshToken, expiresIn);
        }
        return null;
    }

    // 使用 accessToken 取得用戶資訊
    private Map<String, Object> getUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(USERINFO_URI, HttpMethod.GET, entity, Map.class);

        return response.getBody();
    }

    // 儲存或更新用戶和 Token 資訊到 users 表
    private UserVO saveOrUpdateUserWithToken(String email, String username, String avatarUrl, String accessToken, String refreshToken, Integer expiresIn) {
        UserVO user = userService.findByEmail(email);

        // 計算到期時間
        Timestamp accessTokenExpiry = new Timestamp(System.currentTimeMillis() + expiresIn * 1000L);
        Timestamp refreshTokenExpiry = new Timestamp(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000); // 設定Refresh Token有效30天

        if (user == null) {
            // 如果用戶不存在，使用addUser方法新增用戶
          return   userService.addUser(username, "123456", downloadAvatarAsBytes(avatarUrl), email, 1, "Google", accessToken, refreshToken, accessTokenExpiry, refreshTokenExpiry, 1, 0, 2);
        } else {
            // 如果用戶已存在，僅更新Token資訊及Provider Name
            user.setAccessToken(accessToken);
            user.setRefreshToken(refreshToken);
            user.setAccessTokenExpiry(accessTokenExpiry);
            user.setRefreshTokenExpiry(refreshTokenExpiry);
            user.setProviderName("Google");

         return userService.updateUser(user.getUserId(),user.getUsername(),user.getPassword(),user.getAvatar(),user.getEmail(),user.getEnabled(),user.getProviderName(),user.getAccessToken(),user.getRefreshToken(),user.getAccessTokenExpiry(),user.getRefreshTokenExpiry(),user.getNewsletterSubscriptionConsentField(),user.getGroupPoints(),user.getInterestsTag());
        }
    }

    // 假設有一個方法來下載並轉換 Avatar URL 成 byte[] 格式
    private byte[] downloadAvatarAsBytes(String avatarUrl) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            // 使用 RestTemplate 發送 GET 請求，取得圖片的 InputStream
            ResponseEntity<byte[]> response = restTemplate.getForEntity(avatarUrl, byte[].class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;  // 若發生錯誤，返回 null
        }
    }


}
