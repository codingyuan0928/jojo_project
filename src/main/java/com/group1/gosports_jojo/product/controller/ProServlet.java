//package com.product.controller;
//
//import java.io.IOException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.RequestDispatcher;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.group1.gosports_jojo.dao.impl.ProDAO;
//import com.group1.gosports_jojo.model.*;
//
//
//public class ProServlet extends HttpServlet{
//
//	public void doGet(HttpServletRequest req, HttpServletResponse res)
//			throws ServletException, IOException {
//		doPost(req, res);
//	}
//
//	public void doPost(HttpServletRequest req, HttpServletResponse res)
//			throws ServletException, IOException {
//
//		req.setCharacterEncoding("UTF-8");
//		String action = req.getParameter("action");
//
//		if ("searchnam".equals(action)) {    // 來自shop_homepage.jsp的請求
//			Map<String,String> errorMsgs = new LinkedHashMap<String,String>();  //把錯誤資訊存在ermaps，並且用map存取
//			req.setAttribute("errorMsgs", errorMsgs);
////			 System.out.println("近來囉");,
//
//			/***************************1.接收請求參數 - 輸入格式的錯誤處理**********************/
//		    String keyin = req.getParameter("keyin");
//		    if (keyin == null || (keyin.trim()).length() == 0) {
//		     errorMsgs.put("empty1","請輸入產品名稱");
//		    }
//
//		    if (!errorMsgs.isEmpty()) {
//			     RequestDispatcher failureView = req
//			       .getRequestDispatcher("/pro/shop_homepage.jsp");
//			     failureView.forward(req, res);
//			     return;//程式中斷
//			    }
//
//
//		     /***************************2.開始查詢資料*****************************************/
//
//			  if (keyin.isEmpty()) {
//			    errorMsgs.put("empty2","查無資料");
//				}
//			  if (!errorMsgs.isEmpty()) {
//			     RequestDispatcher failureView = req
//			       .getRequestDispatcher("/pro/shop_homepage.jsp");
//			     failureView.forward(req, res);
//			     return;//程式中斷
//			    }
//
//			/***************************3.查詢完成,準備轉交(Send the Success view)*************/
////				   System.out.println("找方法");
//		    ProDAO dao = new ProDAO();
//			List<ProVO> search = dao.getSearchnam(keyin);
//		    req.setAttribute("list", search);   // 資料庫取出的empVO物件,存入req
//			    String url = "/pro/select_homepage.jsp";
//			    RequestDispatcher successView = req.getRequestDispatcher(url);   // 成功轉交 product_detail.jsp
//			    successView.forward(req, res);
//	}
//
//
//			/************************* 下拉式選單功能 *************************/
//
//			if ("choose_popular".equals(action)) {  // like getall,來自select_homepage.jsp的請求
//			/*************************** 1. 開始查詢資料 **********************/
//		    ProDAO dao = new ProDAO();
//			List<ProVO> popular = dao.getPopular();
//
//			/***************************2. 查詢完成,準備轉交(Send the Success view)*************/
////			HttpSession session = req.getSession();
////			ProService prosvc = new ProService();
////			prosvc.getPopular();
//
//			req.setAttribute("list", popular);    //資料庫取出的list物件,存入session
//			// Send the Success view
//			String url = "/pro/select_homepage.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交listAllEmp2_getFromSession.jsp
//			successView.forward(req, res);
//			return;
//		}
//
//
//			if ("choose_new".equals(action)) {  // like getall,來自select_homepage.jsp的請求
//
//			/*************************** 1.開始查詢資料�  **********************/
//			ProDAO dao = new ProDAO();
//			System.out.println("xxx");
//			List<ProVO> newer = dao.getNew();
//			System.out.println("yyy");
//
//			/***************************2. 查詢完成,準備轉交(Send the Success view)*************/
////				HttpSession session = req.getSession();
//			req.setAttribute("list", newer);    // 資料庫取出的list物件,存入session
//			System.out.println("zzz");
//
//			// Send the Success view
//			String url = "/pro/select_homepage.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交listAllEmp2_getFromSession.jsp
//			successView.forward(req, res);
//			return;
//			}
//
//			if ("choose_price".equals(action)) {  // like getall,來自select_homepage.jsp的請求
//
//			/*************************** 1.開始查詢資料 **********************/
//			ProDAO dao = new ProDAO();
//			List<ProVO> popular = dao.getPrice_LtoH();
//
//			/***************************2. 查詢完成,準備轉交(Send the Success view)*************/
//
////				HttpSession session = req.getSession();
//			req.setAttribute("list", popular);     // 資料庫取出的list物件,存入session
//			// Send the Success view
//			String url = "/pro/select_homepage.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交listAllEmp2_getFromSession.jsp
//			successView.forward(req, res);
//			return;
//			}
//
//
//
//			if ("productID".equals(action)) { // like getall,來自select_homepage.jsp的請求
//			/***************************1.�����ШD�Ѽ�-��J�榡�����~�B�z**********************/
//			Integer str = Integer.valueOf(req.getParameter("productId"));
//			req.setAttribute("productId", str);  ///���Fproduct_detail.jsp���ngetParameter,�ҥH���Bset�F
//
//			System.out.println(str);
//
//			/***************************2.�}�l�d�߸��*****************************************/
//
//			 ProService proSvc = new ProService();
//			 List<ProVO> list = proSvc.getOverviewPicture(str);
//			 req.setAttribute("list", list);
//
//
//
//			ProService proSvc1 = new ProService();
//			ProVO proVO = proSvc1.getProductById(str);
//			req.setAttribute("proVO", proVO); // ��Ʈw���X��proVO����A�s�Jreq
//
//
//			List<ProVO> proVO_ad = proSvc1.getAd();
//			req.setAttribute("proVO_ad", proVO_ad);
//
//
//			/***************************3.�d�ߧ����A�ǳƴ���(Send the Success view)*************/
//			String url = "/pro/product_detail.jsp";
//			RequestDispatcher successView = req.getRequestDispatcher(url); // ���\��� listOneEmp.jsp
//			successView.forward(req, res);
//	}
//
//
//
//
//			//廣告圖片
////			if ("ad".equals(action)) { // �Ӧ�shop-homepage.jsp���ШD
////			/***************************1.�����ШD�Ѽ�-��J�榡�����~�B�z**********************/
////			Integer productId = Integer.valueOf(req.getParameter("productId"));
////			req.setAttribute("productId", productId);  ///���Fproduct_detail.jsp���ngetParameter,�ҥH���Bset�F
////
////			System.out.println(productId);
////
////			/***************************2.�}�l�d�߸��*****************************************/
////
////			 ProService proSvc = new ProService();
////			 ProVO provo = proSvc.getProductById(productId);
////
////			 req.setAttribute("proVO", provo);
//
//
////			ProService proSvc1 = new ProService();
////			ProVO proVO = proSvc1.getProductById(str);
//
//			/***************************3.�d�ߧ����A�ǳƴ���(Send the Success view)*************/
////			req.setAttribute("list", provo); // ��Ʈw���X��proVO����A�s�Jreq
////			String url = "/pro/product_detail.jsp";
////			RequestDispatcher successView = req.getRequestDispatcher(url); // ���\��� listOneEmp.jsp
////			successView.forward(req, res);
////	}
//
//
//
//
//
//
//
//	}
//}
//
