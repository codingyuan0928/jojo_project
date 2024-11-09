package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.dto.AuthLoginRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.dto.VendorRegisterRequest;
import com.group1.gosports_jojo.entity.Administrator;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.exception.EmailSendFailedException;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.service.AuthService;
import com.group1.gosports_jojo.service.UserService;
import com.group1.gosports_jojo.service.impl.AuthServiceImpl;
import com.group1.gosports_jojo.service.impl.UserServiceImpl;
import com.group1.gosports_jojo.service.impl.VendorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthRestController {
    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private AuthService authService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private VendorServiceImpl vendorServiceImpl;

    //登入
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Validated AuthLoginRequest authLoginRequest,BindingResult loginResult, HttpSession session) {
        Object account = authService.login(authLoginRequest);
        Map<String, Object> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        if (loginResult.hasErrors()) {
            loginResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        }
        if (!errors.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        if (account instanceof UserVO) {
            log.info("一般用戶登入成功");
            UserVO user = (UserVO) account;
            session.setAttribute("userAccount", user);

            String redirectUrl = (String) session.getAttribute("redirectAfterUserLogin");
            if (redirectUrl == null) {
                redirectUrl = "/user_profile?section=profile";
            } else {
                session.removeAttribute("redirectAfterUserLogin");
            }
            response.put("redirectUrl", redirectUrl);
            response.put("userId", user.getUserId());
            response.put("role", "user");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } else if (account instanceof Vendor) {
            log.info("廠商用戶登入成功");
            Vendor vendor = (Vendor) account;
            session.setAttribute("vendorAccount", vendor);

            String redirectUrl = (String) session.getAttribute("redirectAfterVendorLogin");
            if (redirectUrl == null) {
                redirectUrl = "/vendors/product_menu";
            } else {
                session.removeAttribute("redirectAfterVendorLogin");
            }
            response.put("redirectUrl", redirectUrl);
            response.put("vendorId", vendor.getVendorId());
            response.put("role", "vendor");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("無效的帳戶或密碼");
    }

    //註冊
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestParam("userType") String userType,
                                      @Valid @ModelAttribute UserRegisterRequest userRegisterRequest,
                                      BindingResult userResult,
                                      @Valid @ModelAttribute VendorRegisterRequest vendorRegisterRequest,
                                      BindingResult vendorResult) {

        Map<String, String> errors = new HashMap<>();

        if ("USER".equalsIgnoreCase(userType)) {

            if (userResult.hasErrors()) {
                userResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            }

            if (authService.accountExists(userRegisterRequest.getEmail())) {
                log.warn("該email: {} 已經註冊", userRegisterRequest.getEmail());
                errors.put("email", "Email 已經註冊");
            }

            if (userRegisterRequest.getAvatar() == null || userRegisterRequest.getAvatar().isEmpty()) {
                errors.put("avatar", "頭像選項為必填");
            }

            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            Integer userId = authService.registerUser(userRegisterRequest);
            UserVO user = userServiceImpl.getOneUser(userId);
            String username = user.getUsername();
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "用戶註冊成功", "username", username));
        } else if ("VENDOR".equalsIgnoreCase(userType)) {

            if (vendorResult.hasErrors()) {
                vendorResult.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
            }

            if (authService.accountExists(vendorRegisterRequest.getEmail())) {
                log.warn("該email: {} 已經註冊", vendorRegisterRequest.getEmail());
                errors.put("email", "Email 已經註冊");
            }

            if (vendorServiceImpl.findByCompanyName(vendorRegisterRequest.getCompanyName()) != null) {
                log.warn("該公司名稱: {} 已經註冊", vendorRegisterRequest.getCompanyName());
                errors.put("companyName", "公司名稱已經被註冊");
            }

            // 檢查 shop_name 重複
            if (vendorServiceImpl.findByShopName(vendorRegisterRequest.getShopName()) != null) {
                log.warn("該商店名稱: {} 已經註冊", vendorRegisterRequest.getShopName());
                errors.put("shopName", "商店名稱已經被註冊");
            }

            // 檢查公司電話重複
            if (vendorServiceImpl.findByCompanyPhone(vendorRegisterRequest.getCompanyPhone()) != null) {
                log.warn("該公司電話: {} 已經被使用", vendorRegisterRequest.getCompanyPhone());
                errors.put("companyPhone", "公司電話已經被使用");
            }

            // 檢查統一編號重複
            if (vendorServiceImpl.findByUnifiedBusinessNumber(vendorRegisterRequest.getUnifiedBusinessNumber()) != null) {
                log.warn("該統一編號: {} 已經註冊", vendorRegisterRequest.getUnifiedBusinessNumber());
                errors.put("unifiedBusinessNumber", "統一編號已經被註冊");
            }

            // 檢查公司EMAIL重複
            if (vendorServiceImpl.findByCompanyEmail(vendorRegisterRequest.getCompanyEmail()) != null) {
                log.warn("該公司EMAIL: {} 已經註冊", vendorRegisterRequest.getCompanyEmail());
                errors.put("companyEmail", "公司EMAIL已經被註冊");
            }

            if (vendorRegisterRequest.getAvatar() == null || vendorRegisterRequest.getAvatar().isEmpty()) {
                errors.put("avatar", "頭像選項為必填");
            }
            if (vendorRegisterRequest.getRegistrationDocument() == null || vendorRegisterRequest.getRegistrationDocument().isEmpty()) {
                errors.put("registrationDocument", "營業登記.pdf為必填");
            }
            if (!errors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
            }

            Integer vendorId = authService.registerVendor(vendorRegisterRequest);
            Vendor vendor = vendorServiceImpl.findVendorById(vendorId);
            String vendorName = vendor.getUsername();
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "廠商註冊成功", "vendorName", vendorName));

        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("無效的註冊請求");
    }


    @PostMapping("/sendAuthCode")
    public ResponseEntity<String> sendAuthCode(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        try {
            authService.generateAndSendAuthCode(email);
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

        if (authService.validateAuthCode(email, ac)) {
            return ResponseEntity.ok("驗證碼正確");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("驗證碼錯誤或已過期,請重新操作");
        }
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String newPassword = request.get("newPassword");
        String email = request.get("email");

        Integer result = authService.updatePasswordBasedOnRole(newPassword, email);
        switch (result) {
            case 1:
                return ResponseEntity.status(HttpStatus.OK).body("密碼已更新成功");
            case -1:
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("此信箱尚未註冊");
            case 0:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("密碼更新失敗，請稍後再試!");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生未知錯誤，請稍後再試!");
        }
    }
}
