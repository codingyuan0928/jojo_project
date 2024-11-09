package com.group1.gosports_jojo.controller;


import com.group1.gosports_jojo.member.model.MemberService;
import com.group1.gosports_jojo.member.model.MemberVO;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.notification.model.NotificationService;
import com.group1.gosports_jojo.notification.model.NotificationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
public class UserController {
    @Autowired
    MemberService memberSv;

    //書懿新增
    @Autowired
    NotificationService notificationSvc;

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
            Model model, HttpServletRequest req, HttpServletResponse res
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


        //裝揪團紀錄頁面資訊

        HttpSession session = req.getSession();

        UserVO userVO = (UserVO)session.getAttribute("userAccount");
        Integer userId = userVO.getUserId();

        session.setAttribute("userId", userId);

        List<MemberVO> listCurrent = memberSv.queryCurrentTeam(userId);
        List<MemberVO> listHistory = memberSv.queryHistoryTeam(userId);
        MemberVO yesCount = memberSv.queryPresentYes(userId);
        MemberVO noCount = memberSv.queryPresentNo(userId);
        MemberVO countLeaderTimes = memberSv.countLeaderTimes(userId);
        MemberVO countLeaderNo = memberSv.countLeaderNo(userId);

        req.setAttribute("listCurrent",listCurrent);
        req.setAttribute("listHistory",listHistory);
        req.setAttribute("yesCount",yesCount);
        req.setAttribute("noCount",noCount);
        req.setAttribute("countLeaderTimes",countLeaderTimes);
        req.setAttribute("countLeaderNo",countLeaderNo);


        //書懿新增
        notificationSvc.updateNotificationReadedC(userId);

        List<NotificationVO> list = notificationSvc.getNotificationByUser(userId);
        model.addAttribute("personalNotification",list);



        // 返回視圖名稱
        return "user_profile";
    }

}

