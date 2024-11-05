//package com.group1.gosports_jojo.jimmy;
//
//import com.group1.gosports_jojo.dao.impl.OrderDAO;
//import com.group1.gosports_jojo.model.OrderVO;
//
//import java.io.*;
//import java.sql.Timestamp;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//
//
//
//@WebServlet(name = "OrderServlet", urlPatterns = {"/order"})
//public class OrderServlet extends HttpServlet {
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
//
//
//
//		if ("update".equals(action)) {
//
//			    String id = req.getParameter("id");
//
//			    String status = req.getParameter("status");
//
//			    Integer orderId = Integer.parseInt(id);
//			    Integer orderStatus = Integer.parseInt(status);
//			    OrderDAO orderDAO = new OrderDAO();
//
//			    orderDAO.update(orderId,orderStatus);
//
//				String url = "/order/order.jsp";
//				RequestDispatcher successView = req.getRequestDispatcher(url);
//				successView.forward(req, res);
//		}
//
//
//
//
//
//
//		if("search".equals(action)) {
//
//			String time1 = req.getParameter("time1");
//			String time2 = req.getParameter("time2");
//
//
//		    // 檢查 time1 和 time2 是否為 null 或空字串
//		    if (time1 == null || time1.isEmpty() || time2 == null || time2.isEmpty()) {
//		        // 如果 time1 或 time2 為 null 或空，處理錯誤情況，例如設置默認值或返回錯誤訊息
//		        req.setAttribute("errorMsg", "日期區間無效，請重新選擇時間範圍。");
//		        RequestDispatcher failureView = req.getRequestDispatcher("/order/orderTime.jsp");
//		        failureView.forward(req, res);
//		        return; // 結束這個請求處理
//		    }
//
//			System.out.println(time1);
//			System.out.println(time2);
//			DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE;
//			LocalDate timeOne = LocalDate.parse(time1);
//			LocalDate timeTwo = LocalDate.parse(time2);
//
//		    Timestamp beginTime;
//		    Timestamp endTime;
//		    Integer orderStatus = 0 ;
//
//		    if(timeOne.isBefore(timeTwo)) {
//		    	beginTime = Timestamp.valueOf(timeOne.atStartOfDay());
//		    	endTime = Timestamp.valueOf(timeTwo.atStartOfDay());
//		    }else {
//		    	beginTime = Timestamp.valueOf(timeTwo.atStartOfDay());
//		    	endTime = Timestamp.valueOf(timeOne.atStartOfDay());
//		    }
//
//
//		    OrderDAO orderDAO = new OrderDAO();
//
//		    Set<OrderVO> orderList = orderDAO.getOrdersByTime(beginTime, endTime);//, orderStatus);
//
//			req.setAttribute("OrderList", orderList); // 嚙踝蕭w嚙踝蕭嚙碼嚙踝蕭empVO嚙踝蕭嚙踝蕭,嚙編嚙皚req
//
//			orderList.forEach(e->{
//		    	System.out.println(e);
//		    });
//
//			String url;
//			//if(orderStatus == 1) {
//			url = "/order/orderTime.jsp";
//			//}else {
//			 //  url = "/order/order.jsp";
//			//}
//			RequestDispatcher successView = req.getRequestDispatcher(url); // 嚙踝蕭嚙穀嚙踝蕭嚙� listOneEmp.jsp
//			successView.forward(req, res);
//		}
//
//	if("search2".equals(action)) {//確認order2裡面按鈕的名字 跟order分開
//
//		String time1 = req.getParameter("time1");
//		String time2 = req.getParameter("time2");
//
//
//	    // 檢查 time1 和 time2 是否為 null 或空字串
//	    if (time1 == null || time1.isEmpty() || time2 == null || time2.isEmpty()) {
//	        // 如果 time1 或 time2 為 null 或空，處理錯誤情況，例如設置默認值或返回錯誤訊息
//	        req.setAttribute("errorMsg", "日期區間無效，請重新選擇時間範圍。");
//	        RequestDispatcher failureView = req.getRequestDispatcher("/order/orderTime.jsp");
//	        failureView.forward(req, res);
//	        return; // 結束這個請求處理
//	    }
//
//
//		System.out.println(time1);
//		System.out.println(time2);
//		DateTimeFormatter formatter =  DateTimeFormatter.ISO_LOCAL_DATE;
//		LocalDate timeOne = LocalDate.parse(time1);
//		LocalDate timeTwo = LocalDate.parse(time2);
//
//	    Timestamp beginTime;
//	    Timestamp endTime;
//	    Integer orderStatus = 0 ;
//
//	    if(timeOne.isBefore(timeTwo)) {
//	    	beginTime = Timestamp.valueOf(timeOne.atStartOfDay());
//	    	endTime = Timestamp.valueOf(timeTwo.atStartOfDay());
//	    }else {
//	    	beginTime = Timestamp.valueOf(timeTwo.atStartOfDay());
//	    	endTime = Timestamp.valueOf(timeOne.atStartOfDay());
//	    }
//
//	    OrderDAO orderDAO = new OrderDAO();
//
//	    Set<OrderVO> orderList = orderDAO.getOrdersByTime(beginTime, endTime);//, orderStatus);
//
//	    orderList.forEach(e->{
//	    	System.out.println(e);
//	    });
//
//
//		req.setAttribute("OrderList", orderList);
//
//		String url;
//		//if(orderStatus == 1) {
//		url = "/order/orderTime2.jsp";
//		//}else {
//		 //  url = "/order/order.jsp";
//		//}
//		RequestDispatcher successView = req.getRequestDispatcher(url);
//		successView.forward(req, res);
//	}
//}
//}
