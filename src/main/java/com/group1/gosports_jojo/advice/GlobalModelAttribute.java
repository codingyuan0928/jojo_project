package com.group1.gosports_jojo.advice;


import com.group1.gosports_jojo.entity.Administrator;
import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.model.UserVO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Base64;

@ControllerAdvice
public class GlobalModelAttribute {

    @ModelAttribute
    public void addUserInfoToModel(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        UserVO user = (UserVO) session.getAttribute("userAccount");
        Vendor vendor = (Vendor) session.getAttribute("vendorAccount");
        Administrator admin = (Administrator) session.getAttribute("adminAccount");


        if(admin != null) {
            model.addAttribute("isAuthenticated", true);
            model.addAttribute("role", "ADMIN");
            model.addAttribute("id", admin.getAdministratorId());
            model.addAttribute("username", admin.getUsername());
            model.addAttribute("permissionName",admin.getPermission().getPermissionName());
            model.addAttribute("email", admin.getEmail());
            model.addAttribute("enabled", admin.getEnabled());
            model.addAttribute("createdAt", admin.getCreatedAt());
            model.addAttribute("updatedAt", admin.getUpdatedAt());
        }else if (user != null) {
                model.addAttribute("isAuthenticated", true);
                model.addAttribute("role", "USER");
                model.addAttribute("username", user.getUsername());
                String base64Avatar = Base64.getEncoder().encodeToString(user.getAvatar());
                model.addAttribute("avatar", "data:image/png;base64," + base64Avatar);
                model.addAttribute("userId", user.getUserId());

            } else if (vendor != null) {
                model.addAttribute("isAuthenticated", true);
                model.addAttribute("role", "VENDOR");
                model.addAttribute("vendorId", vendor.getVendorId());
                String base64Avatar = Base64.getEncoder().encodeToString(vendor.getAvatar());
                model.addAttribute("avatar", "data:image/png;base64," + base64Avatar);
                model.addAttribute("username", vendor.getUsername());
                model.addAttribute("email", vendor.getEmail());
                model.addAttribute("companyName", vendor.getCompanyName());
                model.addAttribute("companyAddress", vendor.getCompanyAddress());
                model.addAttribute("companyPhone", vendor.getCompanyPhone());
                model.addAttribute("companyEmail", vendor.getCompanyEmail());
                 String base64RD = Base64.getEncoder().encodeToString(vendor.getRegistrationDocument());
                model.addAttribute("registrationDocument","data:application/pdf;base64," + base64RD);
                model.addAttribute("shopName", vendor.getShopName());
                model.addAttribute("unifiedBusinessNumber", vendor.getUnifiedBusinessNumber());
            } else {
                model.addAttribute("isAuthenticated", false);
                model.addAttribute("role", "GUEST");
        }

    }

}
