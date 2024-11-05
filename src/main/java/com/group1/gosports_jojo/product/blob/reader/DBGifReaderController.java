//package com.blob.reader;
//
//import java.io.*;
//import java.util.List;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.*;
//
//import com.product.model.*;
//
//@WebServlet("/pro/DBGifReader")
//public class DBGifReaderController extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//
//		res.setContentType("image/gif");
//		ServletOutputStream out = res.getOutputStream();
//
//		try {
//			Integer productId = Integer.valueOf(req.getParameter("productId"));
//			ProService proSvc = new ProService();
//			byte[] b = proSvc.getOverviewPicture(productId).get(0).getPicture();
//			out.write(b);
//
////		    ProService proSvc = new ProService();
////		    List<ProVO> list1 = proSvc.getAll();
////			System.out.println("xxxxx");
////
////		    for(int i =0; i <list1.size(); i++) {
////		    	Integer proId = list1.get(i).getProductId();
////		    	List<ProVO> list2 = proSvc.getOverviewPicture(proId);
////				System.out.println("yyyyy");
////
////				for(int j = 0 ; j < list2.size(); j++) {
////					out.write(list2.get(j).getPicture());
////					System.out.println("zzzzz");
////				}
////		    }
//
//
//
//
//		} catch (Exception e) {
//			InputStream in = getServletContext().getResourceAsStream("/pro/images/NoData/nopic.jpg");
//			byte[] buf = new byte[in.available()];
//			in.read(buf);
//			out.write(buf);
//			in.close();
//
//		}
//}
//}
