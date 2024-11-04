//package com.group1.gosports_jojo.jimmy;
//
//import com.group1.gosports_jojo.dao.impl.AddProductDAO;
//import com.group1.gosports_jojo.model.AddProductVO;
//
//import java.io.*;
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import javax.servlet.*;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//
//
//
//
//@MultipartConfig
//public class AddProductServlet extends HttpServlet {
//
//	public void doGet(HttpServletRequest req, HttpServletResponse res)
//			throws ServletException, IOException {
//		doPost(req, res);
//	}
//
//	public void doPost(HttpServletRequest req, HttpServletResponse res)
//			throws ServletException, IOException {
//
//		System.out.println("進來囉!!!!!!!!!");
//
//		req.setCharacterEncoding("UTF-8");
//		String productName = req.getParameter("productName");
//		String productSpec = req.getParameter("productSpec");
//		String price = req.getParameter("price");
//		String stock = req.getParameter("stock");
//		String productContent = req.getParameter("productContent");
//		String action = req.getParameter("actions");
//
//	    AddProductVO addVO = new AddProductVO();
//
//	    int price1 = Integer.parseInt(price);
//	    int stock1 = Integer.parseInt(stock);
//	    int action1 = Integer.parseInt(action);
//
//
//	    addVO.setProductName(productName);
//	    addVO.setProductSpec(productSpec);
//	    addVO.setPrice(price1);
//	    addVO.setStock(stock1);
//	    addVO.setProductContent(productContent);
//	    //addVO.setAddress(address);
//
//	    th: AddProductDAO adao = new AddProductDAO();
//
//	    String errm = adao.insert(addVO, action1);
//
//	    int maxId = adao.maxId();
//
//		Part pic1 = req.getPart("pic1");
//		Part pic2 = req.getPart("pic2");
//		Part pic3 = req.getPart("pic3");
//		Part pic4 = req.getPart("pic4");
//		Part pic5 = req.getPart("pic5");
//
//		InputStream pic = null;
//
//		if(pic1 != null && pic1.getSize()>0) {
//
//			pic = pic1.getInputStream();
//			adao.insert2(pic, maxId);
//		}
//
//		if(pic2 != null && pic2.getSize()>0) {
//
//			pic = pic2.getInputStream();
//			adao.insert2(pic, maxId);
//		}
//
//		if(pic3 != null && pic3.getSize()>0) {
//
//			pic = pic3.getInputStream();
//			adao.insert2(pic, maxId);
//		}
//		if(pic4 != null && pic4.getSize()>0) {
//
//			pic = pic4.getInputStream();
//			adao.insert2(pic, maxId);
//		}
//		if(pic5 != null && pic5.getSize()>0) {
//
//			pic = pic5.getInputStream();
//			adao.insert2(pic, maxId);
//		};
//
//			String url = "/addProduct/addProduct.jsp";
//
//			//res.sendRedirect(url);
//
//            req.setAttribute("ERRM", errm); //
//          //檢查錯誤
//			RequestDispatcher successView = req.getRequestDispatcher(url); //
//			successView.forward(req, res);
//	}
//	}
