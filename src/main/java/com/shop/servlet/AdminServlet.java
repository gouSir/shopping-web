package com.shop.servlet;

import com.shop.dao.OrderDao;
import com.shop.dao.ProductDao;
import com.shop.model.Category;
import com.shop.model.Order;
import com.shop.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

/**
 * 管理员控制器
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final ProductDao productDao = new ProductDao();
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 已由 AdminFilter 拦截验证管理员身份

        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("/index")) {
            adminIndex(req, resp);
        } else if (pathInfo.equals("/product")) {
            productList(req, resp);
        } else if (pathInfo.equals("/product/add")) {
            productForm(req, resp, null);
        } else if (pathInfo.equals("/product/edit")) {
            productForm(req, resp, parseInt(req.getParameter("id"), 0));
        } else if (pathInfo.equals("/product/save")) {
            productSave(req, resp);
        } else if (pathInfo.equals("/product/delete")) {
            productDelete(req, resp);
        } else if (pathInfo.equals("/order")) {
            orderList(req, resp);
        } else if (pathInfo.equals("/order/status")) {
            orderStatus(req, resp);
        } else if (pathInfo.equals("/order/detail")) {
            orderDetail(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/index");
        }
    }

    // ==================== 首页 ====================
    private void adminIndex(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/admin/index.jsp").forward(req, resp);
    }

    // ==================== 商品管理 ====================
    private void productList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int page = parseInt(req.getParameter("page"), 1);
        List<Product> products = productDao.findAll(null, null, page, 20);
        int total = productDao.count(null, null);
        int totalPages = (int) Math.ceil((double) total / 20);

        req.setAttribute("products", products);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.getRequestDispatcher("/admin/product_list.jsp").forward(req, resp);
    }

    private void productForm(HttpServletRequest req, HttpServletResponse resp, Integer productId)
            throws ServletException, IOException {
        List<Category> categories = productDao.findAllCategories();
        req.setAttribute("categories", categories);

        if (productId != null && productId > 0) {
            Product product = productDao.findById(productId);
            req.setAttribute("product", product);
        }
        req.getRequestDispatcher("/admin/product_form.jsp").forward(req, resp);
    }

    private void productSave(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        String idStr = req.getParameter("id");
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String priceStr = req.getParameter("price");
        String stockStr = req.getParameter("stock");
        String categoryIdStr = req.getParameter("categoryId");

        if (isEmpty(name) || isEmpty(priceStr)) {
            resp.getWriter().write("<script>alert('名称和价格不能为空！');history.back();</script>");
            return;
        }

        Product product = new Product();
        product.setName(name.trim());
        product.setDescription(description == null ? "" : description.trim());
        product.setPrice(new BigDecimal(priceStr.trim()));
        product.setStock(parseInt(stockStr, 0));
        product.setCategoryId(parseInt(categoryIdStr, 0));

        if (!isEmpty(idStr) && parseInt(idStr, 0) > 0) {
            // 编辑
            product.setId(Integer.parseInt(idStr));
            productDao.update(product);
        } else {
            // 新增
            productDao.insert(product);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/product");
    }

    private void productDelete(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = parseInt(req.getParameter("id"), 0);
        if (id > 0) {
            productDao.delete(id);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/product");
    }

    // ==================== 订单管理 ====================
    private void orderList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<Order> orders = orderDao.findAll();
        req.setAttribute("orders", orders);
        req.getRequestDispatcher("/admin/order_list.jsp").forward(req, resp);
    }

    private void orderDetail(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int orderId = parseInt(req.getParameter("id"), 0);
        Order order = orderDao.findById(orderId);
        req.setAttribute("order", order);
        req.getRequestDispatcher("/admin/order_detail.jsp").forward(req, resp);
    }

    private void orderStatus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int orderId = parseInt(req.getParameter("id"), 0);
        int status = parseInt(req.getParameter("status"), 0);
        if (orderId > 0) {
            orderDao.updateStatus(orderId, status);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/order");
    }

    // ==================== 工具方法 ====================
    private int parseInt(String s, int defaultValue) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
