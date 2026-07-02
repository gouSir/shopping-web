<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />

<section class="section">
    <h2 class="section-title">📋 我的订单</h2>

    <c:if test="${empty orders}">
        <div class="empty-state">
            <p>📋 还没有订单</p>
            <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary">去购物</a>
        </div>
    </c:if>

    <c:if test="${not empty orders}">
        <table class="order-table">
            <thead>
                <tr>
                    <th>订单编号</th>
                    <th>金额</th>
                    <th>状态</th>
                    <th>下单时间</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.orderNo}</td>
                        <td class="price">¥${order.totalPrice}</td>
                        <td>
                            <span class="status-badge status-${order.status}">${order.statusText}</span>
                        </td>
                        <td>${order.createTime}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/order?action=detail&orderId=${order.id}" class="btn btn-sm">查看详情</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</section>

<jsp:include page="footer.jsp" />
