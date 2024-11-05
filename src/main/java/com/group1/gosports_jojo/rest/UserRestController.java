package com.group1.gosports_jojo.rest;


import com.group1.gosports_jojo.dto.UserProfileResponse;
import com.group1.gosports_jojo.dto.UserProfileUpdateRequest;
import com.group1.gosports_jojo.exception.EmailSendFailedException;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.service.UserService;
import com.group1.gosports_jojo.util.ImageTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpSession;

import java.io.IOException;


@RestController
@RequestMapping("/users")
public class UserRestController {

    @Autowired
    private UserService userService;


    @GetMapping("/status")
    public ResponseEntity<String> getUserStatus(HttpSession session) {
        if (session.getAttribute("userAccount") != null) {
            UserVO user = (UserVO) session.getAttribute("userAccount");
            return ResponseEntity.ok(user.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/current/avatar")
    public ResponseEntity<byte[]> getCurrentUserAvatar(HttpSession session) {
        if (session.getAttribute("userAccount") != null) {
            UserVO user = (UserVO) session.getAttribute("userAccount");
            if (user.getAvatar() != null) {
                // 使用工具類來判斷圖片的 MIME 類型
                String mimeType = ImageTypeUtil.getImageMimeType(user.getAvatar());
                System.out.println("mimeType: " + mimeType);
                if (mimeType != null) {
                    return ResponseEntity
                            .ok()
                            .header("Content-Type", mimeType)
                            .body(user.getAvatar());
                }
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }


    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        System.out.println("使用者已登出");
        return ResponseEntity.status(HttpStatus.OK).body("使用者已成功登出");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getUserProfile(HttpSession session) {
        if (session.getAttribute("userAccount") != null) {
            UserVO user = (UserVO) session.getAttribute("userAccount");
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
        if (session.getAttribute("userAccount") != null) {
            UserVO user = (UserVO) session.getAttribute("userAccount");

            // 處理頭像檔案
            byte[] avatarBytes = user.getAvatar();
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
            user.setAvatar(avatarBytes);
            user.setInterestsTag(interestsTag);
            session.setAttribute("userAccount", user);
            System.out.println("資料已更改成功!!");
            return ResponseEntity.ok("更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("未授權");
        }
    }





}



