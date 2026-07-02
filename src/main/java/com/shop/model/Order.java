package com.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

/**
 * 订单实体类
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String orderNo;
    private Integer userId;
    private String username;        // 用户名（联表）
    private BigDecimal totalPrice;
    private Integer status;         // 0-待付款，1-已付款，2-已发货，3-已完成，4-已取消
    private String address;
    private Timestamp createTime;
    private List<OrderItem> items;  // 订单明细

    public Order() {}

    /**
     * 获取状态的中文描述
     */
    public String getStatusText() {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待付款";
            case 1: return "已付款";
            case 2: return "已发货";
            case 3: return "已完成";
            case 4: return "已取消";
            default: return "未知";
        }
    }

    // ========== Getter & Setter ==========
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getOrderNo() { return orderNo; }
    public void setOrderNo(String orderNo) { this.orderNo = orderNo; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public BigDecimal getTotalPrice() { return totalPrice; }
    public void setTotalPrice(BigDecimal totalPrice) { this.totalPrice = totalPrice; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Timestamp getCreateTime() { return createTime; }
    public void setCreateTime(Timestamp createTime) { this.createTime = createTime; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }

    @Override
    public String toString() {
        return "Order{orderNo='" + orderNo + "', totalPrice=" + totalPrice + ", status=" + getStatusText() + "}";
    }

    /**
     * 订单明细内部类
     */
    public static class OrderItem implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer id;
        private Integer orderId;
        private Integer productId;
        private String productName;
        private BigDecimal price;
        private Integer quantity;

        public BigDecimal getSubtotal() {
            if (price == null || quantity == null) return BigDecimal.ZERO;
            return price.multiply(new BigDecimal(quantity));
        }

        // ========== Getter & Setter ==========
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public Integer getOrderId() { return orderId; }
        public void setOrderId(Integer orderId) { this.orderId = orderId; }

        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
    }
}
