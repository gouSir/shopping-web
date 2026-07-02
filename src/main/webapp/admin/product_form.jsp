<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/header.jsp" />

<section class="section">
    <h2>${empty product ? '添加商品' : '编辑商品'}</h2>

    <form action="${pageContext.request.contextPath}/admin/product/save" method="post" class="admin-form">
        <c:if test="${not empty product}">
            <input type="hidden" name="id" value="${product.id}">
        </c:if>
        <div class="form-group">
            <label>商品名称 <span class="required">*</span></label>
            <input type="text" name="name" value="${product.name}" required>
        </div>
        <div class="form-group">
            <label>商品分类</label>
            <select name="categoryId">
                <option value="">请选择分类</option>
                <c:forEach var="cat" items="${categories}">
                    <option value="${cat.id}" ${product.categoryId == cat.id ? 'selected' : ''}>${cat.name}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>价格 <span class="required">*</span></label>
            <input type="number" step="0.01" name="price" value="${product.price}" required>
        </div>
        <div class="form-group">
            <label>库存</label>
            <input type="number" name="stock" value="${empty product ? 0 : product.stock}">
        </div>
        <div class="form-group">
            <label>商品描述</label>
            <textarea name="description" rows="4">${product.description}</textarea>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">保存</button>
            <a href="${pageContext.request.contextPath}/admin/product" class="btn">取消</a>
        </div>
    </form>
</section>

<jsp:include page="/footer.jsp" />
