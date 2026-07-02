package com.shop.servlet;

import com.shop.dao.UserDao;
import com.shop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * 用户注册
 */
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String password2 = req.getParameter("password2");
        String email = req.getParameter("email");

        // 参数校验
        if (isEmpty(username) || isEmpty(password)) {
            req.setAttribute("error", "用户名和密码不能为空！");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        if (!password.equals(password2)) {
            req.setAttribute("error", "两次输入的密码不一致！");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        if (username.length() < 3 || username.length() > 20) {
            req.setAttribute("error", "用户名长度需在3~20位之间！");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }
        if (password.length() < 6) {
            req.setAttribute("error", "密码长度不能少于6位！");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 检查用户名是否已存在
        if (userDao.findByUsername(username.trim()) != null) {
            req.setAttribute("error", "该用户名已被注册！");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
            return;
        }

        // 注册
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(password.trim());
        user.setEmail(email == null ? "" : email.trim());
        int id = userDao.insert(user);

        if (id > 0) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp?msg=registered");
        } else {
            req.setAttribute("error", "注册失败，请稍后重试！");
            req.getRequestDispatcher("/register.jsp").forward(req, resp);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
