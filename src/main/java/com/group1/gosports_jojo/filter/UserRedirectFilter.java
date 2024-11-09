package com.group1.gosports_jojo.filter;

import com.group1.gosports_jojo.model.UserVO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



    public class UserRedirectFilter implements Filter {

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            HttpSession session = req.getSession();

            Object userAccount = session.getAttribute("userAccount");

            if (userAccount == null) {
                session.setAttribute("redirectAfterLogin", req.getRequestURI());
                res.sendRedirect(req.getContextPath() + "/login");
                return;
            }

            if (userAccount instanceof UserVO && ((UserVO) userAccount).getEnabled() == 0) {
                res.sendRedirect(req.getContextPath() + "/account-suspended");
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

