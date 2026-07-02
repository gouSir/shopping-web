package com.shop.servlet;

import com.shop.dao.CartDao;
import com.shop.dao.ProductDao;
import com.shop.model.CartItem;
import com.shop.model.Product;
import com.shop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 购物车控制器
 */
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CartDao cartDao = new CartDao();
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 检查登录
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "add":     addToCart(req, resp, user.getId());    break;
            case "update":  updateQuantity(req, resp);              break;
            case "delete":  deleteItem(req, resp);                  break;
            case "list":
            default:        showCart(req, resp, user.getId());      break;
        }
    }

    /**
     * 显示购物车
     */
    private void showCart(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws ServletException, IOException {
        List<CartItem> cartItems = cartDao.findByUserId(userId);
        req.setAttribute("cartItems", cartItems);
        req.getRequestDispatcher("/cart.jsp").forward(req, resp);
    }

    /**
     * 添加商品到购物车
     */
    private void addToCart(HttpServletRequest req, HttpServletResponse resp, Integer userId)
            throws IOException {
        int productId = parseInt(req.getParameter("productId"), 0);
        int quantity = parseInt(req.getParameter("quantity"), 1);

        if (productId <= 0) {
            resp.sendRedirect(req.getContextPath() + "/product/list");
            return;
        }
        if (quantity <= 0) quantity = 1;

        // 检查商品是否存在且有库存
        Product product = productDao.findById(productId);
        if (product == null || product.getStock() <= 0) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>alert('商品不存在或已售罄！');history.back();</script>");
            return;
        }

        cartDao.add(userId, productId, quantity);

        // 如果是 AJAX 请求返回 JSON
        String ajax = req.getParameter("ajax");
        if ("true".equals(ajax)) {
            int count = cartDao.getCount(userId);
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().write("{\"success\":true,\"count\":" + count + "}");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/cart?action=list");
    }

    /**
     * 更新购物车项数量
     */
    private void updateQuantity(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int cartId = parseInt(req.getParameter("cartId"), 0);
        int quantity = parseInt(req.getParameter("quantity"), 1);
        if (quantity <= 0) quantity = 1;

        cartDao.updateQuantity(cartId, quantity);
        resp.sendRedirect(req.getContextPath() + "/cart?action=list");
    }

    /**
     * 删除购物车项
     */
    private void deleteItem(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int cartId = parseInt(req.getParameter("cartId"), 0);
        cartDao.delete(cartId);
        resp.sendRedirect(req.getContextPath() + "/cart?action=list");
    }

    private int parseInt(String s, int defaultValue) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
