<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />

<section class="section">
    <div class="admin-header">
        <h2>📦 商品管理</h2>
        <a href="${pageContext.request.contextPath}/admin/product/add" class="btn btn-primary">+ 添加商品</a>
    </div>

    <c:if test="${empty products}">
        <div class="empty-state"><p>暂无商品</p></div>
    </c:if>

    <c:if test="${not empty products}">
        <table class="order-table">
            <thead>
                <tr>
                    <th>ID</th><th>名称</th><th>分类</th><th>价格</th><th>库存</th><th>操作</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="p" items="${products}">
                    <tr>
                        <td>${p.id}</td>
                        <td>${p.name}</td>
                        <td>${p.categoryName}</td>
                        <td class="price">¥${p.price}</td>
                        <td>${p.stock}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/product/edit?id=${p.id}" class="btn btn-sm">编辑</a>
                            <a href="${pageContext.request.contextPath}/admin/product/delete?id=${p.id}" class="btn btn-sm btn-danger"
                               onclick="return confirm('确定删除【${p.name}】吗？')">删除</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <a href="${pageContext.request.contextPath}/admin/product?page=${currentPage - 1}">上一页</a>
                </c:if>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/admin/product?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <a href="${pageContext.request.contextPath}/admin/product?page=${currentPage + 1}">下一页</a>
                </c:if>
            </div>
        </c:if>
    </c:if>
</section>

<jsp:include page="/footer.jsp" />
