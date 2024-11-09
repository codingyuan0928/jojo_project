package com.group1.gosports_jojo.controller;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.group1.gosports_jojo.model.PostVO;
import com.group1.gosports_jojo.model.ProVO;
import com.group1.gosports_jojo.model.ReplyVO;
import com.group1.gosports_jojo.service.impl.shop.ProService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.group1.gosports_jojo.customersupport.model.*;
import com.group1.gosports_jojo.group.model.*;
import com.group1.gosports_jojo.model.UserVO;
import com.group1.gosports_jojo.notification.model.*;
import com.group1.gosports_jojo.notificationitem.model.*;
import com.group1.gosports_jojo.service.impl.*;
import com.group1.gosports_jojo.vendor.model.*;


@Controller
public class CustomerSupportController {

	@Autowired
	CustomerSupportService customerSupportSvc;

	@Autowired
	NotificationService notificationSvc;

	@Autowired
	NotificationItemListService notificationItemListSvc;

	@Autowired
	GroupService groupSvc;

	@Autowired
	PostService postSvc;

	@Autowired
	ReplyService replySvc;

	@Autowired
	ProService proSvc;

	@Autowired
	VendorService vendorSvc1;

	@GetMapping("/feedback_form")
	public String feedbackForm(Model model) {
		CustomerSupportVO customerSupportVO = new CustomerSupportVO();
		model.addAttribute("customerSupportVO", customerSupportVO);

		System.out.println(customerSupportVO.getIssueDescription());

		return "feedback_form";
	}

	@GetMapping("/feedback_submitted")
	public String feedbackSubmitted() {
		return "feedback_submitted";
	}

//	@GetMapping("/customer_support_query")
//	public String customerSupportQuery(HttpServletRequest req, Model model) {
//		req.setAttribute("activeTab", 1);
//		return "admin_customer_support_query";
//	}

//	@GetMapping("/ad_platform")
//	public String adPlatform(HttpServletRequest req, Model model) {
//
//		List<ProVO> backendAdList = proSvc.getAd();
//		model.addAttribute("backendAdList", backendAdList);
//
//		return "admin_ad_platform";
//	}

//	@GetMapping("/customer_support")
//	public String GetCustomerSupportFeedback(HttpServletRequest req, Model model) {
//
//		List<CustomerSupportVO> customerSupportList1 = customerSupportSvc.findPendingCaseC();
//		model.addAttribute("customerSupportList1", customerSupportList1);
//
//		List<CustomerSupportVO> customerSupportList2 = customerSupportSvc.getAllC();
//		model.addAttribute("customerSupportList2", customerSupportList2);
//
//		List<NotificationItemListVO> customerSupportList = notificationItemListSvc.getAll();
//		model.addAttribute("customerSupportList", customerSupportList);
//
//		return "admin_customer_support";
//	}

	
//	@PostMapping("/update_customer_support_feedback") // 來自customer_support.jsp的請求
//	public String updateCustomerSupportFeedback(HttpServletRequest req, Model model) {
//
//System.out.println("有進updateCustomerSupportFeedback");
//
//		/*************************** 1.接收請求參數 - 輸入格式的錯誤處理 **********************/
//		String status = req.getParameter("status");
//		String notificationContent = req.getParameter("notificationContent");
//		Integer formId = Integer.valueOf(req.getParameter("formId").trim());
//System.out.println("status" + status);
//System.out.println("notificationContent" + notificationContent);
//System.out.println("formId" + formId);
//System.out.println("==================================");
//
//		/*************************** 2.送出資料 *****************************************/
//
//		if ("1: 未處理".equals(status)) {
//
//			CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//			customerSupportDTO.setFormId(formId);
//			customerSupportDTO.setErrorMsgs("請選擇處理狀態!!");
//
//System.out.println("#138--getFormId: " + customerSupportDTO.getFormId());
//System.out.println("#138--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());
//
//			if (customerSupportDTO != null) {
//				model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//				List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//				model.addAttribute("list1", list1);
//
//				List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//				model.addAttribute("list2", list2);
//
//				List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//				model.addAttribute("list", list);
//
//				return "admin_customer_support";// 程式中斷
//			}
//		}
//
//
//		if ("2: 結案_毋須處理".equals(status)) {
//
//			String str = req.getParameter("notifiedReferenceId");
//System.out.println("#156--formId: " + formId);
//System.out.println("#156--notifiedReferenceId: " + str);
//
//			if (str != null || (str.trim()).length() != 0) {
//				CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//				customerSupportDTO.setFormId(formId);
//				customerSupportDTO.setErrorMsgs("目前處理狀態不須發送通知!!");
//
//System.out.println("#170--getFormId: " + customerSupportDTO.getFormId());
//System.out.println("#170--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());
//
//				if (customerSupportDTO != null) {
//					model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//					List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//					model.addAttribute("list1", list1);
//
//					List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//					model.addAttribute("list2", list2);
//
//					List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//					model.addAttribute("list", list);
//
//					return "admin_customer_support";// 程式中斷
//				}
//			}
//
//		}
//
//
//		if ("3: 結案_發送通知信件".equals(status)) {
//
//			// 驗證notifiedReferenceId是否為整數格式
//			String str = req.getParameter("notifiedReferenceId");
//System.out.println("#189--notifiedReferenceId: " + str);
//
//			try {
//
//				Integer notifiedReferenceId = Integer.valueOf(str);
//
//			} catch (Exception e) {
//
//				CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//				customerSupportDTO.setFormId(formId);
//				customerSupportDTO.setErrorMsgs("查無資料!!");
//				model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//System.out.println("#208--getFormId: " + customerSupportDTO.getFormId());
//System.out.println("#208--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());
//
//				List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//				model.addAttribute("list1", list1);
//
//				List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//				model.addAttribute("list2", list2);
//
//				List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//				model.addAttribute("list", list);
//
//				return "admin_customer_support";// 程式中斷
//			}
//
//			// 驗證是否有此揪團id
//			if (notificationContent.startsWith("移除揪團")) {
//
//				Integer notifiedReferenceId_Group = Integer.valueOf(req.getParameter("notifiedReferenceId").trim());
//				System.out.println("#173--notifiedReferenceId: " + notifiedReferenceId_Group);
//
//				GroupVO groupVO = groupSvc.findByPrimaryKey(notifiedReferenceId_Group);
//
//				if (groupVO == null) {
//
//					CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//					customerSupportDTO.setFormId(formId);
//					customerSupportDTO.setErrorMsgs("查無資料!!");
//					model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//	System.out.println("#238--getFormId: " + customerSupportDTO.getFormId());
//	System.out.println("#238--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());
//
//					List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//					model.addAttribute("list1", list1);
//
//					List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//					model.addAttribute("list2", list2);
//
//					List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//					model.addAttribute("list", list);
//
//					return "admin_customer_support";// 程式中斷
//
//				} else {
//					customerSupportSvc.updateFeedback3(status, notifiedReferenceId_Group, notificationContent, formId);
//				}
//			}
//
//			// 驗證是否有此文章id
//			if (notificationContent.startsWith("移除文章")) {
//
//				Integer notifiedReferenceId_Article = Integer.valueOf(req.getParameter("notifiedReferenceId").trim());
//				System.out.println("#204--notifiedReferenceId: " + notifiedReferenceId_Article);
//
//				PostVO postVO = postSvc.getOnePost(notifiedReferenceId_Article); // 查詢是否有此文章id
//
//				if (postVO == null) {
//
//					CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//					customerSupportDTO.setFormId(formId);
//					customerSupportDTO.setErrorMsgs("查無資料!!");
//					model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//	System.out.println("#278--getFormId: " + customerSupportDTO.getFormId());
//	System.out.println("#278--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());
//
//					List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//					model.addAttribute("list1", list1);
//
//					List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//					model.addAttribute("list2", list2);
//
//					List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//					model.addAttribute("list", list);
//
//					return "admin_customer_support";// 程式中斷
//
//				} else {
//					customerSupportSvc.updateFeedback3(status, notifiedReferenceId_Article, notificationContent,
//							formId);
//				}
//			}
//
//			// 驗證是否有此留言id
//			if (notificationContent.startsWith("移除留言")) {
//
//				Integer notifiedReferenceId_Reply = Integer.valueOf(req.getParameter("notifiedReferenceId").trim());
//				System.out.println("#234--notifiedReferenceId: " + notifiedReferenceId_Reply);
//
//				ReplyVO replyVO = replySvc.getOne(notifiedReferenceId_Reply); // 查詢是否有此留言id
//
//				if (replyVO == null) {
//
//					CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//					customerSupportDTO.setFormId(formId);
//					customerSupportDTO.setErrorMsgs("查無資料!!");
//					model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//	System.out.println("#318--getFormId: " + customerSupportDTO.getFormId());
//	System.out.println("#318--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());

//
//					List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//					model.addAttribute("list1", list1);
//
//					List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//					model.addAttribute("list2", list2);
//
//					List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//					model.addAttribute("list", list);
//
//					return "admin_customer_support";// 程式中斷
//
//				} else {
//					customerSupportSvc.updateFeedback3(status, notifiedReferenceId_Reply, notificationContent, formId);
//				}
//			}
//
//			// 驗證是否有此商品id
//			if (notificationContent.startsWith("商品下架(經檢舉)")) {
//
//				Integer notifiedReferenceId_Product = Integer.valueOf(req.getParameter("notifiedReferenceId").trim());
//				System.out.println("#265--notifiedReferenceId: " + notifiedReferenceId_Product);
//
//				ProVO proVO = proSvc.getProductById(notifiedReferenceId_Product); // 到會員表查詢是否有此userid
//
//				if (proVO == null) {
//
//					CustomerSupportDTO customerSupportDTO = new CustomerSupportDTO();
//
//					customerSupportDTO.setFormId(formId);
//					customerSupportDTO.setErrorMsgs("查無資料!!");
//					model.addAttribute("customerSupportDTO", customerSupportDTO);
//
//	System.out.println("#358--getFormId: " + customerSupportDTO.getFormId());
//	System.out.println("#358--getErrorMsgs: " + customerSupportDTO.getErrorMsgs());
//
//					List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//					model.addAttribute("list1", list1);
//
//					List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//					model.addAttribute("list2", list2);
//
//					List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//					model.addAttribute("list", list);
//
//					return "admin_customer_support";// 程式中斷
//
//				} else {
//					customerSupportSvc.updateFeedback3(status, notifiedReferenceId_Product, notificationContent,
//							formId);
//				}
//			}
//		}
//
//		/*************************** 3.修改完成,準備轉交(Send the Success view) *************/
//		List<CustomerSupportVO> list1 = customerSupportSvc.findPendingCaseC();
//		model.addAttribute("list1", list1);
//
//		List<CustomerSupportVO> list2 = customerSupportSvc.getAllC();
//		model.addAttribute("list2", list2);
//
//		List<NotificationItemListVO> list = notificationItemListSvc.getAll();
//		model.addAttribute("list", list);
//
//		return "admin_customer_support";
//	}
	

	@PostMapping("/insert_Feedback_Form")
	public String insert_Feedback_Form(HttpServletRequest req, Model model) {
		Integer userid;

		try {
			HttpSession session = req.getSession();

			UserVO userVO = (UserVO) session.getAttribute("userAccount");
			userid = userVO.getUserId();
		} catch (NullPointerException n) {

			userid = 0;

		}

		// 沒登入，仍可提交客服表單，但預設user_id = 0

		String title = req.getParameter("feedback-title");

		String category = req.getParameter("feedback-category");

		String content = req.getParameter("feedback-content");

		/*************************** 2.開始新增資料 ***************************************/
		CustomerSupportVO customerSupportVO = customerSupportSvc.addFeedbackForm(userid, category, title, content);
		model.addAttribute("customerSupportVO", customerSupportVO);

		System.out.println(customerSupportVO.getCategory());
		System.out.println(customerSupportVO.getTitle());
		System.out.println(customerSupportVO.getIssueDescription());

		/*************************** 3.新增完成,準備轉交(Send the Success view) ***********/
		return "feedback_submitted";
	}

//	@PostMapping("/backend_search_group")
//	public String backend_search_group(HttpServletRequest req, Model model) {
//
//		String keyword = req.getParameter("keyword");
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		List<GroupVO> listGroup = groupSvc.getGroupByKeyWord(keyword, keyword, keyword);
//		req.setAttribute("listGroup", listGroup);
//
//		req.setAttribute("activeTab", 1);
//
//		return "admin_customer_support_query";
//	}

//	@PostMapping("/backend_search_article")
//	public String backend_search_article(HttpServletRequest req, Model model) {
//
//		String keyword = req.getParameter("keyword");
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		List<PostVO> listArticle = postSvc.getArticleByKeyWord(keyword, keyword);
//		req.setAttribute("listArticle", listArticle);
//
//		req.setAttribute("activeTab", 2);
//
//		return "admin_customer_support_query";
//	}
//
//	@PostMapping("/backend_search_reply")
//	public String backend_search_reply(HttpServletRequest req, Model model) {
//
//		String keyword = req.getParameter("keyword");
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		List<ReplyVO> listReply = replySvc.getReplyByKeyWord(keyword);
//		req.setAttribute("listReply", listReply);
//
//		req.setAttribute("activeTab", 3);
//
//		return "admin_customer_support_query";
//	}
//
//	@PostMapping("/backend_search_product")
//	public String backend_search_product(HttpServletRequest req, Model model) {
//
//		String keyword = req.getParameter("keyword");
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		List<ProVO> listPro = proSvc.getProductByKeyWord(keyword, keyword);
//		req.setAttribute("listPro", listPro);
//
//		req.setAttribute("activeTab", 4);
//
//		return "admin_customer_support_query";
//	}

//	@PostMapping("/remove_ad")
//	public String remove_ad(HttpServletRequest req, Model model) {
//
//		Integer productId = Integer.valueOf(req.getParameter("productId"));
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		proSvc.changeAdOff(productId);
//
//		List<ProVO> list = proSvc.getAd();
//		model.addAttribute("list", list);
//
//		return "admin_ad_platform";
//	}

	
//	@PostMapping("/insert_ad")
//	public String insert_ad(HttpServletRequest req, Model model) {
//
//		Integer productId = Integer.valueOf(req.getParameter("productId"));
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		proSvc.changeAdOn(productId);
//
//		String serchProductName = "";
//
//		List<ProVO> list_product = proSvc.getSearchnam(serchProductName);
//		req.setAttribute("list_product", list_product);
//
//		List<ProVO> list = proSvc.getAd();
//		model.addAttribute("list", list);
//
//		return "admin_ad_platform";
//	}
//
//
	
//	@PostMapping("/serchProductName")
//	public String serchProductName(HttpServletRequest req, Model model) {
//
//		String serchProductName = req.getParameter("serchProductName");
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		List<ProVO> list_product = proSvc.getSearchnam(serchProductName);
//		req.setAttribute("list_product", list_product);
//
//		List<ProVO> list = proSvc.getAd();
//		model.addAttribute("list", list);
//
//		return "admin_ad_platform";
//	}

	@GetMapping("/vendor_review")
	public String vendorReview(HttpServletRequest req, Model model) {

		List<VendorVO> vendorlist1 = vendorSvc1.getPendingVendor();
		req.setAttribute("vendorlist1", vendorlist1);

		List<VendorVO> vendorlist2 = vendorSvc1.getAllVendor();
		req.setAttribute("vendorlist2", vendorlist2);

		return "admin_vendor_review";
	}

	@GetMapping("/getOneVendorImage")
	public void getOneVendorImage(HttpServletRequest req, HttpServletResponse res, Model model) throws IOException {
		res.setContentType("image/gif");
		ServletOutputStream out = res.getOutputStream();

		try {
			Integer vendorId = Integer.valueOf(req.getParameter("vendorId"));

			if (vendorSvc1.getOneVendorImage(vendorId).getVendorId() == vendorId) {
				out.write(vendorSvc1.getOneVendorImage(vendorId).getRegistrationDocument());
			}
		} catch (Exception e) {
			byte[] buf = java.util.Base64.getDecoder().decode(
					"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");
			out.write(buf);

		} finally {
			out.close();
		}
	}

//	@PostMapping("/update_vendor_status")
//	public String updateVendorStatus(HttpServletRequest req, Model model) {
//
//		Integer vendorId = Integer.valueOf(req.getParameter("vendorId"));
//		Integer status = Integer.valueOf(req.getParameter("status"));
//
//		/*************************** 2.開始新增資料 ***************************************/
//
//		if (status == 1) {
//			vendorSvc1.updatePass(vendorId);
//		} else if (status == 2) {
//			vendorSvc1.updateHidden(vendorId);
//
//		}
//
//		List<VendorVO> vendorlist1 = vendorSvc1.getPendingVendor();
//		req.setAttribute("vendorlist1", vendorlist1);
//
//		List<VendorVO> vendorlist2 = vendorSvc1.getAllVendor();
//		req.setAttribute("vendorlist2", vendorlist2);
//
//		return "admin_vendor_review";
//	}

}
