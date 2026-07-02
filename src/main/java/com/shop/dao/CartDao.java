package com.shop.dao;

import com.shop.model.CartItem;
import com.shop.utils.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车数据访问层
 */
public class CartDao {

    /**
     * 查询用户购物车
     */
    public List<CartItem> findByUserId(Integer userId) {
        String sql = "SELECT c.*, p.name AS product_name, p.price, p.image FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id WHERE c.user_id = ? ORDER BY c.add_time DESC";
        List<CartItem> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return list;
    }

    /**
     * 查找某用户的某商品是否已在购物车中
     */
    public CartItem findByUserAndProduct(Integer userId, Integer productId) {
        String sql = "SELECT c.*, p.name AS product_name, p.price, p.image FROM cart_items c " +
                     "JOIN products p ON c.product_id = p.id WHERE c.user_id = ? AND c.product_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return null;
    }

    /**
     * 添加到购物车
     */
    public void add(Integer userId, Integer productId, int quantity) {
        // 如果已存在则更新数量
        CartItem exist = findByUserAndProduct(userId, productId);
        if (exist != null) {
            updateQuantity(exist.getId(), exist.getQuantity() + quantity);
            return;
        }
        String sql = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    /**
     * 更新购物车项数量
     */
    public void updateQuantity(Integer cartId, int quantity) {
        if (quantity <= 0) {
            delete(cartId);
            return;
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, quantity);
            stmt.setInt(2, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    /**
     * 删除购物车项
     */
    public void delete(Integer cartId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt);
        }
    }

    /**
     * 清空用户购物车
     */
    public void clearByUserId(Integer userId, Connection conn) throws SQLException {
        // 用于事务中调用
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    /**
     * 获取购物车商品数量
     */
    public int getCount(Integer userId) {
        String sql = "SELECT COUNT(*) FROM cart_items WHERE user_id = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, stmt, rs);
        }
        return 0;
    }

    private CartItem mapRow(ResultSet rs) throws SQLException {
        CartItem item = new CartItem();
        item.setId(rs.getInt("id"));
        item.setUserId(rs.getInt("user_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setAddTime(rs.getTimestamp("add_time"));
        try { item.setProductName(rs.getString("product_name")); } catch (SQLException ignored) {}
        try { item.setPrice(rs.getBigDecimal("price")); } catch (SQLException ignored) {}
        try { item.setImage(rs.getString("image")); } catch (SQLException ignored) {}
        return item;
    }
}
