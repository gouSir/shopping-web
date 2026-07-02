package com.shop.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 购物车项实体类
 */
public class CartItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Integer userId;
    private Integer productId;
    private String productName;     // 商品名称
    private BigDecimal price;       // 商品单价
    private String image;           // 商品图片
    private Integer quantity;       // 数量
    private Timestamp addTime;

    public CartItem() {}

    /**
     * 计算小计金额
     */
    public BigDecimal getSubtotal() {
        if (price == null || quantity == null) return BigDecimal.ZERO;
        return price.multiply(new BigDecimal(quantity));
    }

    // ========== Getter & Setter ==========
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Timestamp getAddTime() { return addTime; }
    public void setAddTime(Timestamp addTime) { this.addTime = addTime; }

    @Override
    public String toString() {
        return "CartItem{productName='" + productName + "', quantity=" + quantity + "}";
    }
}
