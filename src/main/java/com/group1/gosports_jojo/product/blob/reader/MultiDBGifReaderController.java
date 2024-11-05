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
//@WebServlet("/pro/MultiDBGifReader")
//public class MultiDBGifReaderController extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
//
//		res.setContentType("image/gif");
//		ServletOutputStream out = res.getOutputStream();
//		System.out.println("aaaaaa");
//
//		try {
//			Integer productId = Integer.valueOf(req.getParameter("productId"));
//
//			ProService proSvc = new ProService();
//			List <ProVO> list= proSvc.getOverviewPicture(productId);
//		      for(int i =0; i <list.size(); i++) {
//
//		       Integer count = Integer.valueOf(req.getParameter("count"));
//
//		       if(i == count) {
//
//		       byte[] b = proSvc.getOverviewPicture(productId).get(i).getPicture();
//
//		    out.write(b);
//
//		    }
//		      }
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
//
