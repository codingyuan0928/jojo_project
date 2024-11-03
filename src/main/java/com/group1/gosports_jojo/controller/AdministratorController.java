package com.group1.gosports_jojo.controller;

import com.group1.gosports_jojo.dto.UserListDTO;
import com.group1.gosports_jojo.service.AdminService;
import com.group1.gosports_jojo.service.UserService;
import com.group1.gosports_jojo.service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdministratorController {

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private AdminService adminService;


    @GetMapping("/administrators")
    public String administrators(Model model) {
        List<UserListDTO> userLists = userService.getAllUserDTOs();
        model.addAttribute("userlists", userLists);
        return "administrators";
    }
    @GetMapping("/administrator_login")
    public String administratorsLogin(){
        return "administrator_login";
    }
}
