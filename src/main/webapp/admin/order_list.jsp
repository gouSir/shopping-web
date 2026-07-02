<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />

<section class="section">
    <h2>📋 订单管理</h2>

    <c:if test="${empty orders}">
        <div class="empty-state"><p>暂无订单</p></div>
    </c:if>

    <c:if test="${not empty orders}">
        <table class="order-table">
            <thead>
                <tr>
                    <th>订单编号</th><th>用户</th><th>金额</th><th>状态</th><th>时间</th><th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.orderNo}</td>
                        <td>${order.username}</td>
                        <td class="price">¥${order.totalPrice}</td>
                        <td><span class="status-badge status-${order.status}">${order.statusText}</span></td>
                        <td>${order.createTime}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/order/detail?id=${order.id}" class="btn btn-sm">详情</a>
                            <c:choose>
                                <c:when test="${order.status == 0}">
                                    <a href="${pageContext.request.contextPath}/admin/order/status?id=${order.id}&status=1" class="btn btn-sm btn-primary">标记付款</a>
                                </c:when>
                                <c:when test="${order.status == 1}">
                                    <a href="${pageContext.request.contextPath}/admin/order/status?id=${order.id}&status=2" class="btn btn-sm btn-primary">标记发货</a>
                                </c:when>
                                <c:when test="${order.status == 2}">
                                    <a href="${pageContext.request.contextPath}/admin/order/status?id=${order.id}&status=3" class="btn btn-sm btn-success">完成</a>
                                </c:when>
                            </c:choose>
                            <c:if test="${order.status == 0}">
                                <a href="${pageContext.request.contextPath}/admin/order/status?id=${order.id}&status=4" class="btn btn-sm btn-danger"
                                   onclick="return confirm('确定取消该订单吗？')">取消</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</section>

<jsp:include page="/footer.jsp" />
