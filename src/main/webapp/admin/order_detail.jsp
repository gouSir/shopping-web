<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />

<section class="section">
    <h2>📋 订单详情</h2>

    <c:if test="${empty order}">
        <div class="empty-state"><p>订单不存在</p></div>
    </c:if>

    <c:if test="${not empty order}">
        <div class="order-detail-card">
            <div class="order-info">
                <p><strong>订单编号：</strong>${order.orderNo}</p>
                <p><strong>下单用户：</strong>${order.username}</p>
                <p><strong>下单时间：</strong>${order.createTime}</p>
                <p><strong>订单状态：</strong><span class="status-badge status-${order.status}">${order.statusText}</span></p>
                <p><strong>收货地址：</strong>${order.address}</p>
            </div>
            <table class="cart-table" style="margin-top:20px;">
                <thead><tr><th>商品</th><th>单价</th><th>数量</th><th>小计</th></tr></thead>
                <tbody>
                    <c:forEach var="item" items="${order.items}">
                        <tr>
                            <td>${item.productName}</td>
                            <td>¥${item.price}</td>
                            <td>${item.quantity}</td>
                            <td class="price">¥${item.subtotal}</td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <div class="order-total-bar">
                <span>总金额：<strong class="total-price">¥${order.totalPrice}</strong></span>
            </div>
        </div>
        <p style="margin-top:20px;"><a href="${pageContext.request.contextPath}/admin/order" class="btn">← 返回订单列表</a></p>
    </c:if>
</section>

<jsp:include page="/footer.jsp" />
