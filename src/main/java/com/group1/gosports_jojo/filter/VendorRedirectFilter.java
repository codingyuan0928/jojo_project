package com.group1.gosports_jojo.filter;

import com.group1.gosports_jojo.entity.Vendor;
import com.group1.gosports_jojo.model.UserVO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class VendorRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        Object vendorAccount = session.getAttribute("vendorAccount");

        if (vendorAccount== null) {
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (vendorAccount instanceof Vendor && ((Vendor) vendorAccount).getEnabled() == 0) {
            res.sendRedirect(req.getContextPath() + "/account-suspended");
            return;
        }

        chain.doFilter(request, response);
    }
}

