//package com.product.controller;
//
//import java.io.*;
//import javax.servlet.*;
//import javax.servlet.http.*;
//
//public class LoginFilter implements Filter {
//
//	private FilterConfig config;
//
//	public void init(FilterConfig config) {
//		this.config = config;
//	}
//
//	public void destroy() {
//		config = null;
//	}
//
//	public void doFilter(ServletRequest request, ServletResponse response,
//			FilterChain chain) throws ServletException, IOException {
//
//		HttpServletRequest req = (HttpServletRequest) request;
//		HttpServletResponse res = (HttpServletResponse) response;
//
//
//		// ?��?��?? session??
//		HttpSession session = req.getSession();
//
//		// ?��?? session ?��?��此user?��?��?��?��??��??
//		Object account = session.getAttribute("account");
//
//		if (account == null) {
//			session.setAttribute("location", req.getRequestURI());
//			res.sendRedirect(req.getContextPath() + "/pro/login.html");
//			return;
//		} else {
//
//
//			chain.doFilter(request, response);
//
//
//		}
//	}
//}