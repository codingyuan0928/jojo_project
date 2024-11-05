package com.group1.gosports_jojo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    @GetMapping("/test")
    public String test(){
        return "test";
    }
    @GetMapping("/sendEmailTest")
    public String sendEmailTest(){
        return "sendEmailTest";
    }
    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/login")
    public String userLogin(){
        return "login";
    }
    @GetMapping("/user_forget_password")
    public String user_forget_password(){
        return "user_forget_password";
    }

    @GetMapping("/user_registration")
    public String user_registration(){
        return "user_registration";
    }
    @GetMapping("/vendor_registration")
    public String vendor_registration(){
        return "vendor_registration";
    }


   @GetMapping("/chatroom")
    public String chatroom(){
        return "chatroom";
    }

    @GetMapping("/orderhistory")
    public String orderhistory(){
        return "orderhistory";
    }

    @GetMapping("/shoppingcart")
    public String shoppingcart(){
        return "shoppingcart";
    }

    @GetMapping("/user_profile")
    public String userProfile(
            @RequestParam(value = "section", required = false,defaultValue = "profile") String section,
            Model model
    ) {
        // 將 section 值放入模型中，讓 Thymeleaf 可以使用
        model.addAttribute("section", section);

        // 根據 section 的值來執行不同邏輯或設定不同的模板數據
        switch (section) {
            case "profile":
                // 執行個人資料相關邏輯
                break;
            case "group-record":
                // 執行參團紀錄相關邏輯
                break;
            case "order-record":
                // 執行訂單紀錄相關邏輯
                break;
            case "notifications":
                // 執行通知相關邏輯
                break;
            default:
                // 預設邏輯（如 section 參數為空，同profile）
                break;
        }

        // 返回視圖名稱
        return "user_profile";
    }

}

