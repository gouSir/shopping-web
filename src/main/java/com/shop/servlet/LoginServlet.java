package com.shop.servlet;

import com.shop.dao.UserDao;
import com.shop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 用户登录
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            req.setAttribute("error", "用户名和密码不能为空！");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        User user = userDao.findByUsernameAndPassword(username.trim(), password.trim());
        if (user == null) {
            req.setAttribute("error", "用户名或密码错误！");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        // 登录成功，写入 session
        HttpSession session = req.getSession();
        session.setAttribute("user", user);

        // 跳转到之前的页面或首页
        String redirectUrl = (String) session.getAttribute("redirectUrl");
        if (redirectUrl != null) {
            session.removeAttribute("redirectUrl");
            resp.sendRedirect(redirectUrl);
        } else {
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        }
    }
}
