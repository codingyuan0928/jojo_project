package com.group1.gosports_jojo.notification.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.notification.model.*;
import com.group1.gosports_jojo.vendor.model.VendorVO;

@Controller
public class NotificationController {

	@Autowired
	NotificationService notificationSvc;


	@GetMapping("/notification")
	public String getNotificationByUser(HttpServletRequest req, Model model) {

		HttpSession session = req.getSession();

		UserVO userVO = (UserVO) session.getAttribute("userAccount");
		Integer userid = userVO.getUserId();
		
		List<NotificationVO> list = notificationSvc.getNotificationByUser(userid);
		model.addAttribute("personalNotification",list);

		return "notification";
	}
	
	
	@PostMapping("/hidden_notification_C")
	public String hiddenNotificationC(HttpServletRequest req, Model model) {
System.out.println("有進#49--hidden_notification");
		HttpSession session = req.getSession();

		UserVO userVO = (UserVO) session.getAttribute("userAccount");
		Integer userid = userVO.getUserId();
System.out.println("#54--userid"+userid);
		
		Integer notificationId = Integer.valueOf(req.getParameter("notificationId"));
		notificationSvc.hiddenNotification(notificationId);
System.out.println("#59--notificationId"+notificationId);
		
		List<NotificationVO> list = notificationSvc.getNotificationByUser(userid);
		model.addAttribute("personalNotification",list);
System.out.println("#62--查詢個人通知"+list);


return "redirect:/user_profile?section=notifications";
	}	
	
	

//
//	@GetMapping("/notification_vendor")
//	public String getNotificationByVendor(HttpServletRequest req, Model model) {
//
//		req.setAttribute("vendorId", 1);
//		Integer vendorId = (Integer) (req.getAttribute("vendorId"));
////		 Integer userId = Integer.valueOf(req.getParameter("userId"));
//
//		List<NotificationVO> list = notificationSvc.getNotificationByVendor(vendorId);
//		model.addAttribute("vendorNotification",list);
//
//		return "notification_vendor";
//	}
//
//
//	@PostMapping("/hidden_notification_V")
//	public String hiddenNotificationV(HttpServletRequest req, Model model) {
////System.out.println("有進#72--hidden_notification");
////		HttpSession session = req.getSession();
////
////		VendorVO vendorVO = (VendorVO) session.getAttribute("VendorAccount");
////		Integer vendorId = vendorVO.getVendorId();
////System.out.println("#78--userid"+vendorId);
//
//		req.setAttribute("vendorId", 1);
//		Integer vendorId = (Integer) (req.getAttribute("vendorId"));
//
//		Integer notificationId = Integer.valueOf(req.getParameter("notificationId"));
//		notificationSvc.hiddenNotification(notificationId);
//System.out.println("#82--notificationId"+notificationId);
//
//		List<NotificationVO> list = notificationSvc.getNotificationByVendor(vendorId);
//		model.addAttribute("vendorNotification",list);
//System.out.println("#85--查詢個人通知"+list);
//
//return "/notification_vendor";
//	}
//

	
	
	
	
}
