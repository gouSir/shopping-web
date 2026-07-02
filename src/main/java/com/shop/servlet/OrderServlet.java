package com.shop.servlet;

import com.shop.dao.CartDao;
import com.shop.dao.OrderDao;
import com.shop.dao.ProductDao;
import com.shop.dao.UserDao;
import com.shop.model.CartItem;
import com.shop.model.Order;
import com.shop.model.Order.OrderItem;
import com.shop.model.Product;
import com.shop.model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单控制器
 */
@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final OrderDao orderDao = new OrderDao();
    private final CartDao cartDao = new CartDao();
    private final ProductDao productDao = new ProductDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "checkout": checkout(req, resp, user);         break;
            case "detail":   showOrderDetail(req, resp, user);  break;
            case "list":
            default:         showOrderList(req, resp, user);    break;
        }
    }

    /**
     * 订单列表
     */
    private void showOrderList(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        List<Order> orders = orderDao.findByUserId(user.getId());
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/orders.jsp").forward(req, resp);
    }

    /**
     * 订单详情
     */
    private void showOrderDetail(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        int orderId = parseInt(req.getParameter("orderId"), 0);
        Order order = orderDao.findById(orderId);

        // 只能查看自己的订单
        if (order == null || !order.getUserId().equals(user.getId())) {
            resp.sendRedirect(req.getContextPath() + "/order?action=list");
            return;
        }

        req.setAttribute("order", order);
        req.getRequestDispatcher("/order_detail.jsp").forward(req, resp);
    }

    /**
     * 结算下单
     */
    private void checkout(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException, ServletException {

        // 获取用户购物车
        List<CartItem> cartItems = cartDao.findByUserId(user.getId());
        if (cartItems.isEmpty()) {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>alert('购物车为空，无法下单！');location.href='" +
                    req.getContextPath() + "/cart?action=list';</script>");
            return;
        }

        // 检查库存
        for (CartItem item : cartItems) {
            Product product = productDao.findById(item.getProductId());
            if (product == null || product.getStock() < item.getQuantity()) {
                resp.setContentType("text/html;charset=UTF-8");
                resp.getWriter().write("<script>alert('商品【" + item.getProductName() +
                        "】库存不足！');location.href='" + req.getContextPath() + "/cart?action=list';</script>");
                return;
            }
        }

        // 构造订单明细
        List<OrderItem> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getProductId());
            oi.setProductName(ci.getProductName());
            oi.setPrice(ci.getPrice());
            oi.setQuantity(ci.getQuantity());
            items.add(oi);
            total = total.add(oi.getSubtotal());
        }

        // 获取收货地址
        String address = req.getParameter("address");
        if (address == null || address.trim().isEmpty()) {
            // 取用户默认地址
            User fullUser = userDao.findById(user.getId());
            address = (fullUser != null && fullUser.getAddress() != null) ? fullUser.getAddress() : "";
        }
        if (address.trim().isEmpty()) {
            address = "请查看用户资料完善收货地址";
        }

        // 创建订单（事务）
        int orderId = orderDao.createOrder(user.getId(), address.trim(), items, total);

        if (orderId > 0) {
            resp.sendRedirect(req.getContextPath() + "/order?action=detail&orderId=" + orderId);
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            resp.getWriter().write("<script>alert('下单失败，请稍后重试！');history.back();</script>");
        }
    }

    private int parseInt(String s, int defaultValue) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return defaultValue; }
    }
}
