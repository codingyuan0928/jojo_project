//package com.product.controller;
//
//import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
////import javax.servlet.annotation.WebServlet;
//
////import com.group.model.GroupService;
////import com.group.model.GroupVO;
//import com.user.model.UserService;
//import com.user.model.UserVO;
//
////@WebServlet("/loginhandler")
//public class LoginHandler extends HttpServlet {
//	private static final long serialVersionUID = 1L;
//
//
//   //�i�ˬd�ϥΪ̿�J���b��(account) �K�X(password)�O�_���ġj
//   //�i��ڤW���ܸ�Ʈw�j�M���j
////  protected boolean allowUser(String password) {
////	  LoginHandler lo =new LoginHandler();
////
////	  if ( "tomcat".equals(password))
////      return true;
////    else
////      return false;
////  }
//
//
//  public void doPost(HttpServletRequest req, HttpServletResponse res)
//                                throws ServletException, IOException {
//    req.setCharacterEncoding("Big5");
//    res.setContentType("text/html; charset=Big5");
//    PrintWriter out = res.getWriter();
//
//    // �i���o�ϥΪ� �b��(account) �K�X(password)�j
//    String account = req.getParameter("account");
//    String password = req.getParameter("password");
//
//
//    //////////////////�q��Ʈw��,�ϥ� usersvc �� compare ��k�� user_id,user_name,user_password/////////////////////////
//	UserService userSvc = new UserService();
//	UserVO userVO = userSvc.compare(account);
//	String DbPassword= userVO.getPassword();
//	String username = userVO.getUsername();
//	Integer userId = userVO.getUserId();
//
//
//    // �i�ˬd�ӱb�� , �K�X�O�_���ġj
//    if (!password.equals(DbPassword)) {          //�i�b�� , �K�X�L�Įɡj
//      out.println("<HTML><HEAD><TITLE>Access Denied</TITLE></HEAD>");
//      out.println("<BODY>�A���b�� , �K�X�L��!<BR>");
//      out.println("�Ы������s�n�J <A HREF="+req.getContextPath()+"/pro/login.html>���s�n�J</A>");
//      out.println("</BODY></HTML>");
//    }else {                                       //�i�b�� , �K�X���Į�, �~���H�U�u�@�j
//      HttpSession session = req.getSession();
//      session.setAttribute("account", account);   //*�u�@1: �~�bsession�����w�g�n�J�L������
////////////////////////////////�q��Ʈw�� userId �ϥ� session �e���s�����U��///////////////////////////////////////
//      session.setAttribute("userId", userId);
//       try {
//         String location = (String) session.getAttribute("location");
//         if (location != null) {
//           session.removeAttribute("location");   //*�u�@2: �ݬݦ��L�ӷ����� (-->�p���ӷ�����:�h���ɦܨӷ�����)
//           res.sendRedirect(location);
//           return;
//         }
//       }catch (Exception ignored) { }
//
//      res.sendRedirect(req.getContextPath()+"/pro/login_success.jsp");  //*�u�@3: (-->�p�L�ӷ�����:�h���ɦ�login_success.jsp)
//    }
//  }
//}