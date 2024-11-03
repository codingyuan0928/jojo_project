package com.group1.gosports_jojo.filter;

import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.model.UserVO;
import org.apache.catalina.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginRedirectFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        UserVO userAccount = (UserVO) session.getAttribute("userAccount");
        Vendor vendorAccount = (Vendor) session.getAttribute("vendorAccount");

        if (userAccount == null && vendorAccount == null) {
            String redirectUrl = req.getRequestURI();
            session.setAttribute("redirectAfterLogin", redirectUrl);
            res.sendRedirect("/login");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
