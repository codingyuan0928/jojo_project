package com.group1.gosports_jojo.advice;


import com.group1.gosports_jojo.model.UserVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@ControllerAdvice
public class GlobalModelAttribute {
    @ModelAttribute
    public void addUserInfoToModel(HttpServletRequest request, Model model) {
        // 獲取當前的 HttpSession
        HttpSession session = request.getSession();

        // 從 session 中取得使用者資訊

        UserVO user = (UserVO) session.getAttribute("loggedInUser");

        // 根據 session 中是否有使用者資訊來判斷登入狀態
        if (user != null) {
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("username", user.getUsername());
            String base64Avatar = Base64.getEncoder().encodeToString(user.getAvatarBytes());
            model.addAttribute("avatar", "data:image/png;base64," + base64Avatar);
//            model.addAttribute("userId", user.getUserId());
        } else {
            model.addAttribute("isAuthenticated", false);
        }
    }
}
