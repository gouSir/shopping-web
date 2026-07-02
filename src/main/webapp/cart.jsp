<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp" />

<section class="section">
    <h2 class="section-title">🛒 我的购物车</h2>

    <c:if test="${empty cartItems}">
        <div class="empty-state">
            <p>🛒 购物车是空的</p>
            <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary">去逛逛</a>
        </div>
    </c:if>

    <c:if test="${not empty cartItems}">
        <div class="cart-container">
            <table class="cart-table">
                <thead>
                    <tr>
                        <th>商品</th>
                        <th>单价</th>
                        <th>数量</th>
                        <th>小计</th>
                        <th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:set var="total" value="0" />
                    <c:forEach var="item" items="${cartItems}">
                        <tr>
                            <td class="cart-product">
                                <img src="${pageContext.request.contextPath}/images/${item.image}" alt="${item.productName}" onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
                                <a href="${pageContext.request.contextPath}/product/detail/${item.productId}">${item.productName}</a>
                            </td>
                            <td>¥${item.price}</td>
                            <td>
                                <form action="${pageContext.request.contextPath}/cart" method="post" class="qty-form">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="cartId" value="${item.id}">
                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="99" onchange="this.form.submit()" style="width:60px;text-align:center;">
                                </form>
                            </td>
                            <td class="price">¥${item.subtotal}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/cart?action=delete&cartId=${item.id}"
                                   class="btn btn-sm btn-danger" onclick="return confirm('确定要移除这件商品吗？')">删除</a>
                            </td>
                        </tr>
                        <c:set var="total" value="${total + item.subtotal}" />
                    </c:forEach>
                </tbody>
            </table>

            <div class="cart-footer">
                <div class="cart-total">
                    合计：<span class="total-price">¥${total}</span>
                </div>
                <div class="cart-actions">
                    <a href="${pageContext.request.contextPath}/product/list" class="btn">继续购物</a>
                    <form action="${pageContext.request.contextPath}/order" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="checkout">
                        <button type="submit" class="btn btn-primary btn-lg" onclick="return confirm('确认下单吗？')">立即下单</button>
                    </form>
                </div>
            </div>
        </div>
    </c:if>
</section>

<jsp:include page="footer.jsp" />
