package com.group1.gosports_jojo.filter;

import com.group1.gosports_jojo.entity.Administrator;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AdminRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        Object adminAccount = (Administrator) session.getAttribute("administratorAccount");

        if (adminAccount == null) {
            res.sendRedirect("/administrator_login");
            return;
        }

        if(adminAccount instanceof Administrator && ((Administrator)adminAccount).getEnabled()==0) {
            res.sendRedirect(req.getContextPath() + "/account-suspended");
            return;
        }





        chain.doFilter(request, response);
    }
}
