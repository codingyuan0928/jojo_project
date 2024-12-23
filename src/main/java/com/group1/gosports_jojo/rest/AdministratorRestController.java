package com.group1.gosports_jojo.rest;

import com.group1.gosports_jojo.dto.AdminUpdateRequest;
import com.group1.gosports_jojo.dto.AuthLoginRequest;
import com.group1.gosports_jojo.entity.Administrator;
import com.group1.gosports_jojo.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/admin")
public class AdministratorRestController {
@Autowired
private AdminService adminServiceImpl;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthLoginRequest authLoginRequest, HttpSession session) {
        try {
            Administrator administrator = adminServiceImpl.login(authLoginRequest);
            System.out.println("管理員登入成功");
            session.setAttribute("adminAccount", administrator);
            return ResponseEntity.status(HttpStatus.OK).body("管理員登入成功");
        } catch (ResponseStatusException e) {
            if (e.getStatus() == HttpStatus.BAD_REQUEST) {
                if (e.getReason() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("該 email 尚未註冊");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getReason());
                }
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("發生未知錯誤");
        }
    }
    @GetMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        System.out.println("管理員帳號已登出");
        return ResponseEntity.status(HttpStatus.OK).body("管理員帳號已成功登出");
    }

    @PostMapping("/update")
    public ResponseEntity<String> adminUpdateProfile(@RequestBody AdminUpdateRequest adminUpdateRequest,HttpSession session){
        String newUsername = adminUpdateRequest.getUsername();
        Administrator administrator = (Administrator) session.getAttribute("adminAccount");
        boolean updateSuccessful = adminServiceImpl.updateAdminName(newUsername,administrator);

        if (updateSuccessful) {
            return ResponseEntity.ok("名稱更新成功");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("更新失敗");
        }

    }

}
