package com.shop.filter;

import com.shop.model.User;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 管理员过滤器 — 拦截 /admin/ 下的所有请求
 */
@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        if (!user.isAdmin()) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>alert('权限不足，仅管理员可访问！');history.back();</script>");
            return;
        }
        chain.doFilter(req, resp);
    }

    @Override
    public void destroy() {}
}
