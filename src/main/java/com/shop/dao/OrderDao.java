package com.shop.dao;

import com.shop.model.Order;
import com.shop.model.Order.OrderItem;
import com.shop.utils.DBUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 订单数据访问层
 */
public class OrderDao {

    /**
     * 生成唯一订单编号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    /**
     * 创建订单（事务：插入订单 → 插入明细 → 扣库存 → 清空购物车）
     * @return 订单 ID，失败返回 -1
     */
    public int createOrder(Integer userId, String address, List<OrderItem> items, BigDecimal totalPrice) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 插入订单
            String orderNo = generateOrderNo();
            String sqlOrder = "INSERT INTO orders (order_no, user_id, total_price, status, address) VALUES (?, ?, ?, 'pending', ?)";
            int orderId;
            try (PreparedStatement stmt = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, orderNo);
                stmt.setInt(2, userId);
                stmt.setBigDecimal(3, totalPrice);
                stmt.setString(4, address);
                stmt.executeUpdate();
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (!rs.next()) { conn.rollback(); return -1; }
                    orderId = rs.getInt(1);
                }
            }

            // 2. 插入订单明细 + 扣库存
            String sqlItem = "INSERT INTO order_items (order_id, product_id, product_name, product_price, quantity) VALUES (?, ?, ?, ?, ?)";
            String sqlStock = "UPDATE products SET stock = stock - ? WHERE id = ? AND stock >= ?";
            try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem);
                 PreparedStatement stmtStock = conn.prepareStatement(sqlStock)) {
                for (OrderItem item : items) {
                    stmtItem.setInt(1, orderId);
                    stmtItem.setInt(2, item.getProductId());
                    stmtItem.setString(3, item.getProductName());
                    stmtItem.setBigDecimal(4, item.getPrice());
                    stmtItem.setInt(5, item.getQuantity());
                    stmtItem.executeUpdate();

                    stmtStock.setInt(1, item.getQuantity());
                    stmtStock.setInt(2, item.getProductId());
                    stmtStock.setInt(3, item.getQuantity());
                    int affected = stmtStock.executeUpdate();
                    if (affected == 0) { conn.rollback(); return -1; } // 库存不足
                }
            }

            // 3. 清空购物车
            new CartDao().clearByUserId(userId, conn);

            conn.commit();
            return orderId;
        } catch (SQLException e) {
            DBUtil.rollback(conn);
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
        return -1;
    }

    /**
     * 查询用户订单列表
     */
    public List<Order> findByUserId(Integer userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY create_time DESC";
        List<Order> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }

    /**
     * 查询所有订单（管理员）
     */
    public List<Order> findAll() {
        String sql = "SELECT o.*, u.username FROM orders o JOIN users u ON o.user_id = u.id ORDER BY o.create_time DESC";
        List<Order> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = mapOrder(rs);
                order.setUsername(rs.getString("username"));
                list.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }

    /**
     * 查询订单详情（含明细）
     */
    public Order findById(Integer orderId) {
        // 查询订单
        String sqlOrder = "SELECT o.*, u.username FROM orders o JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sqlOrder);
            stmt.setInt(1, orderId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                Order order = mapOrder(rs);
                order.setUsername(rs.getString("username"));
                // 查询明细
                order.setItems(findItemsByOrderId(orderId, conn));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }

    /**
     * 更新订单状态（管理员）
     */
    public void updateStatus(Integer orderId, int status) {
        // 将整数状态码转为字符串
        String statusStr;
        switch (status) {
            case 1: statusStr = "paid"; break;
            case 2: statusStr = "shipped"; break;
            case 3: statusStr = "completed"; break;
            case 4: statusStr = "canceled"; break;
            default: statusStr = "pending"; break;
        }
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, statusStr);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    // ===================== 私有方法 =====================

    private List<OrderItem> findItemsByOrderId(Integer orderId, Connection conn) throws SQLException {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        List<OrderItem> items = new ArrayList<>();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getInt("id"));
                    item.setOrderId(rs.getInt("order_id"));
                    item.setProductId(rs.getInt("product_id"));
                    item.setProductName(rs.getString("product_name"));
                    item.setPrice(rs.getBigDecimal("product_price"));
                    item.setQuantity(rs.getInt("quantity"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    /**
     * 将数据库状态字符串转为整数（兼容旧代码）
     */
    private int statusStrToInt(String s) {
        if (s == null) return 0;
        switch (s) {
            case "paid":      return 1;
            case "shipped":   return 2;
            case "completed": return 3;
            case "canceled":  return 4;
            default:          return 0; // pending
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setOrderNo(rs.getString("order_no"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(statusStrToInt(rs.getString("status")));
        order.setAddress(rs.getString("address"));
        order.setCreateTime(rs.getTimestamp("create_time"));
        return order;
    }
}
