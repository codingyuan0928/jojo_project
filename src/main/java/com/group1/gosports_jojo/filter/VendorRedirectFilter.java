package com.group1.gosports_jojo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class VendorRedirectFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        Object account = session.getAttribute("vendorAccount");

        if (account == null) {
            session.setAttribute("redirectAfterLogin", req.getRequestURI());
            res.sendRedirect("/login");
            return;
        }
        chain.doFilter(request, response);
    }
}
