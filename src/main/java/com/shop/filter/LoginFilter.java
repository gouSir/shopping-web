package com.shop.filter;

import com.shop.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录过滤器 — 拦截需要登录才能访问的页面
 */
@WebFilter(urlPatterns = {"/cart.jsp", "/orders.jsp", "/checkout.jsp"})
public class LoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            // 未登录，跳转到登录页
            req.getSession().setAttribute("redirectUrl", req.getRequestURI());
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {}
}
