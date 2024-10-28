package com.group1.gosports_jojo.rest;


import com.group1.gosports_jojo.dto.UserLoginRequest;
import com.group1.gosports_jojo.dto.UserProfileResponse;
import com.group1.gosports_jojo.dto.UserProfileUpdateRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.exception.EmailSendFailedException;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.service.UserService;
import com.group1.gosports_jojo.util.ImageTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;



    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @ModelAttribute UserRegisterRequest userRegisterRequest, BindingResult result) {
        // 創建一個 Map 來收集所有錯誤訊息
        Map<String, String> errors = new HashMap<>();

        // 收集自動驗證的字段錯誤
        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        }

        // 手動檢查頭像是否為空，並將錯誤訊息加入到 errors 中
        if (userRegisterRequest.getAvatar() == null || userRegisterRequest.getAvatar().isEmpty()) {
            errors.put("avatar", "頭像不能為空");
        }else {
            userRegisterRequest.setAvatarBytes(userRegisterRequest.getAvatar());
        }

        // 如果有任何錯誤訊息，則返回錯誤響應
        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }

        // 進行註冊邏輯
        try {
            Integer userId = userService.register(userRegisterRequest);
            UserVO userVO = userService.getOneUser(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(userVO);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(Collections.singletonMap("error", e.getReason()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody @Validated UserLoginRequest userLoginRequest, HttpSession session) {
        UserVO user = userService.login(userLoginRequest);
        System.out.println("使用者登入成功");
        session.setAttribute("loggedInUser", user);

        String redirectUrl = (String) session.getAttribute("redirectAfterLogin");
        if(redirectUrl == null) {
            redirectUrl="/user_profile?section=profile";

        }else{
            session.removeAttribute("redirectAfterLogin");
        }
        Map<String, String> response = new HashMap<>();
        response.put("redirectUrl", redirectUrl);
        response.put("userId", user.getUserId().toString());
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/status")
    public ResponseEntity<String> getUserStatus(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            UserVO user = (UserVO) session.getAttribute("loggedInUser");
            return ResponseEntity.ok(user.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/current/avatar")
    public ResponseEntity<byte[]> getCurrentUserAvatar(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            UserVO user = (UserVO) session.getAttribute("loggedInUser");
            if (user.getAvatarBytes() != null) {
                // 使用工具類來判斷圖片的 MIME 類型
                String mimeType = ImageTypeUtil.getImageMimeType(user.getAvatarBytes());
                System.out.println("mimeType: " + mimeType);
                if (mimeType != null) {
                    return ResponseEntity
                            .ok()
                            .header("Content-Type", mimeType)
                            .body(user.getAvatarBytes());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        System.out.println("使用者已登出");
        return ResponseEntity.status(HttpStatus.OK).body("已成功登出");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            UserVO user = (UserVO) session.getAttribute("loggedInUser");
            // 建立 UserProfileResponse
            UserProfileResponse userProfileResponse = new UserProfileResponse();
            userProfileResponse.setUsername(user.getUsername());
            userProfileResponse.setInterestsTag(user.getInterestsTag());
            userProfileResponse.setEmail(user.getEmail());
            return ResponseEntity.status(HttpStatus.OK)
                    .cacheControl(CacheControl.noCache()) // 禁用快取
                    .body(userProfileResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/profile")
    public ResponseEntity<String> updateUserProfile(@RequestParam("username") String username,
                                                    @RequestParam("interestsTag") Integer interestsTag,
                                                    @RequestParam(value = "avatar", required = false) MultipartFile avatar,
                                                    HttpSession session) {
        if (session.getAttribute("loggedInUser") != null) {
            UserVO user = (UserVO) session.getAttribute("loggedInUser");

            // 處理頭像檔案
            byte[] avatarBytes = user.getAvatarBytes();
            if (avatar != null && !avatar.isEmpty()) {
                try {
                    avatarBytes = avatar.getBytes();  // 將 MultipartFile 轉換成 byte[] 儲存到資料庫
                } catch (IOException e) {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("頭像上傳失敗");
                }
            }


            // 將資料放入 UserProfileUpdateRequest
            UserProfileUpdateRequest userProfileUpdateRequest = new UserProfileUpdateRequest();
            userProfileUpdateRequest.setUsername(username);
            userProfileUpdateRequest.setAvatar(avatarBytes);
            userProfileUpdateRequest.setInterestsTag(interestsTag);

            // 更新用戶資料
            userService.updateUserProfile(user.getUserId(), userProfileUpdateRequest);
            // 更新 session 中的 UserVO
            user.setUsername(username);
            user.setAvatarBytes(avatarBytes);
            user.setInterestsTag(interestsTag);
            session.setAttribute("loggedInUser", user);
            System.out.println("資料已更改成功!!");
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未授權");
        }
    }


    @PostMapping("/sendAuthCode")
    public ResponseEntity<String> sendAuthCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            userService.generateAndSendAuthCode(email);
            return ResponseEntity.ok("驗證碼已送出至您的電子郵件，請於三分鐘內查收。");
        } catch (EmailSendFailedException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("郵件寄送失敗，請稍後再試。");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("發生未知錯誤，請稍後再試。");
        }
    }

    @PostMapping("/checkAuthCode")
    public ResponseEntity<String> checkAuthCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String ac = request.get("authCode");

        if (userService.validateAuthCode(email, ac)) {
            return ResponseEntity.ok("驗證碼正確");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("驗證碼錯誤或已過期,請重新操作");
        }
    }


}



