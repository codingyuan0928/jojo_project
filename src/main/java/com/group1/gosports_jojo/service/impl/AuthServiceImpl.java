package com.group1.gosports_jojo.service.impl;


import com.group1.gosports_jojo.dao.AdministratorDAO;
import com.group1.gosports_jojo.dao.UserDAO;
import com.group1.gosports_jojo.dao.VendorDAO;
import com.group1.gosports_jojo.dto.AuthLoginRequest;
import com.group1.gosports_jojo.dto.UserRegisterRequest;
import com.group1.gosports_jojo.dto.VendorRegisterRequest;
import com.group1.gosports_jojo.entity.Administrator;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.security.PasswordUtil;
import com.group1.gosports_jojo.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;


@Service
public class AuthServiceImpl implements AuthService {
    public static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordUtil passwordUtil;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AuthCode authCodeService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private VendorDAO vendorDAO;

    @Autowired
    private AdministratorDAO administratorDAO;


    public boolean accountExists(String email) {
        return userService.findByEmail(email) != null
                || vendorService.findByEmail(email) != null
                || adminService.findByEmail(email) != null;
    }

    public Integer registerUser(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest);
    }

    public Integer registerVendor(VendorRegisterRequest vendorRegisterRequest) {
        return vendorService.register(vendorRegisterRequest);
    }


    public Object login(AuthLoginRequest authLoginRequest) {
        String email = authLoginRequest.getEmail();
        String password = authLoginRequest.getPassword();

        // 檢查 User 表
        UserVO user = userService.findByEmail(email);
        if (user != null) {
            if (passwordUtil.matches(password, user.getPassword())) {
                return user; // 返回 user 物件
            } else {
                log.warn("User email: {} 的密碼不正確", email);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "密碼不正確");
            }
        }

        // 檢查 Vendor 表
        Vendor vendor = vendorService.findByEmail(email);
        if (vendor != null) {
            if (passwordUtil.matches(password, vendor.getPassword())) {
                return vendor; // 返回 vendor 物件
            } else {
                log.warn("Vendor email: {} 的密碼不正確", email);
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "密碼不正確");
            }
        }

        // 若 User 和 Vendor 均不存在
        log.warn("該 email: {} 尚未註冊", email);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "帳戶不存在");
    }

    @Override
    public void generateAndSendAuthCode(String email) {
        String authCode = authCodeService.returnAuthCode();
        redisService.setAuthCode(email, authCode, 180);
        String messageText = "親愛的用戶您好:\n" +
                "您的email驗證碼為: " + authCode + "\n" +
                "請於三分鐘內完成驗證避免逾時，謝謝~";
        emailService.sendMail(email, "GoSports用戶信箱驗證", messageText);
    }

    @Override
    public boolean validateAuthCode(String email, String authCode) {
        String storedAuthCode = redisService.getAuthCode(email);
        return storedAuthCode != null && storedAuthCode.equals(authCode);
    }

    @Override
    public Integer updatePasswordBasedOnRole(String newPassword, String email) {
        String role = null;
        if (adminService.findByEmail(email) != null) {
            role = "admin";
        } else if (userService.findByEmail(email) != null) {
            role = "user";
        } else if (vendorService.findByEmail(email) != null) {
            role = "vendor";
        } else {
            return -1;//未註冊
        }
        if (newPassword == null || newPassword.isEmpty()) {
            return 0;//密碼輸入為空值
        }
        String hashedPassword = passwordUtil.encode(newPassword);
        switch (role) {
            case "admin":
                Administrator admin = adminService.findByEmail(email);
                if (admin != null) {
                    admin.setPassword(hashedPassword);
                    return administratorDAO.update(admin) ? 1 : 0;
                }
                break;
            case "user":
                UserVO user = userService.findByEmail(email);
                if (user != null) {
                    user.setPassword(hashedPassword);
                    return userDAO.update(user) ? 1 : 0;
                }
                break;
            case "vendor":
                Vendor vendor = vendorService.findByEmail(email);
                if (vendor != null) {
                    vendor.setPassword(hashedPassword);
                    return vendorDAO.update(vendor) != null ? 1 : 0;
                }
                break;
            default:
                return 0;  // 未知錯誤
        }
        return 0;//更新失敗
    }
}
