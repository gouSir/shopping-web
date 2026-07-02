package com.shop.servlet;

import com.shop.dao.ProductDao;
import com.shop.model.Category;
import com.shop.model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

/**
 * 商品控制器 — 处理商品列表和商品详情
 */
@WebServlet("/product/*")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final int PAGE_SIZE = 8;
    private final ProductDao productDao = new ProductDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pathInfo = req.getPathInfo(); // /detail/3  或 空

        // 商品详情：/product/detail/3
        if (pathInfo != null && pathInfo.startsWith("/detail/")) {
            int productId = parseId(pathInfo.substring(8));
            showDetail(req, resp, productId);
            return;
        }

        // 商品列表
        showList(req, resp);
    }

    /**
     * 商品列表（分页 + 分类筛选 + 关键词搜索）
     */
    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // 参数获取
        String keyword = req.getParameter("keyword");
        int categoryId = parseInt(req.getParameter("categoryId"), 0);
        int page = parseInt(req.getParameter("page"), 1);

        // 查询
        List<Product> products = productDao.findAll(
                categoryId > 0 ? categoryId : null,
                keyword, page, PAGE_SIZE);
        int total = productDao.count(categoryId > 0 ? categoryId : null, keyword);
        int totalPages = (int) Math.ceil((double) total / PAGE_SIZE);

        // 分类列表
        List<Category> categories = productDao.findAllCategories();

        // 回传参数
        req.setAttribute("products", products);
        req.setAttribute("categories", categories);
        req.setAttribute("currentPage", page);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("total", total);
        req.setAttribute("currentCategoryId", categoryId);
        req.setAttribute("keyword", keyword);

        req.getRequestDispatcher("/products.jsp").forward(req, resp);
    }

    /**
     * 商品详情
     */
    private void showDetail(HttpServletRequest req, HttpServletResponse resp, int productId)
            throws ServletException, IOException {

        Product product = productDao.findById(productId);
        if (product == null) {
            resp.sendRedirect(req.getContextPath() + "/products.jsp");
            return;
        }

        req.setAttribute("product", product);
        req.getRequestDispatcher("/product_detail.jsp").forward(req, resp);
    }

    private int parseInt(String s, int defaultValue) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    private int parseId(String s) {
        try { return Integer.parseInt(s); }
        catch (NumberFormatException e) { return 0; }
    }
}
