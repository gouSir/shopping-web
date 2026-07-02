<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<jsp:include page="header.jsp" />

<section class="section">
    <!-- 分类导航 -->
    <div class="category-nav">
        <a href="${pageContext.request.contextPath}/product/list" class="${empty currentCategoryId or currentCategoryId == 0 ? 'active' : ''}">全部</a>
        <c:forEach var="cat" items="${categories}">
            <a href="${pageContext.request.contextPath}/product/list?categoryId=${cat.id}"
               class="${currentCategoryId == cat.id ? 'active' : ''}">${cat.name}</a>
        </c:forEach>
    </div>

    <!-- 搜索结果提示 -->
    <c:if test="${not empty keyword}">
        <p class="search-hint">搜索 "<strong>${keyword}</strong>" 的结果，共 ${total} 件商品</p>
    </c:if>

    <!-- 商品列表 -->
    <c:if test="${empty products}">
        <div class="empty-state">
            <p>📦 暂无商品</p>
        </div>
    </c:if>
    <div class="product-grid">
        <c:forEach var="p" items="${products}">
            <div class="product-card">
                <div class="product-img">
                    <img src="${pageContext.request.contextPath}/images/${p.image}" alt="${p.name}" onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
                    <c:if test="${p.stock <= 0}">
                        <span class="sold-out">已售罄</span>
                    </c:if>
                </div>
                <div class="product-info">
                    <span class="product-category">${p.categoryName}</span>
                    <h3><a href="${pageContext.request.contextPath}/product/detail/${p.id}">${p.name}</a></h3>
                    <p class="product-desc">${p.description}</p>
                    <div class="product-bottom">
                        <span class="product-price">¥${p.price}</span>
                        <c:if test="${p.stock > 0}">
                            <a href="${pageContext.request.contextPath}/cart?action=add&productId=${p.id}&quantity=1" class="btn btn-sm btn-primary">加入购物车</a>
                        </c:if>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>

    <!-- 分页 -->
    <c:if test="${totalPages > 1}">
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <a href="${pageContext.request.contextPath}/product/list?page=${currentPage - 1}&categoryId=${currentCategoryId}&keyword=${keyword}">上一页</a>
            </c:if>
            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="${pageContext.request.contextPath}/product/list?page=${i}&categoryId=${currentCategoryId}&keyword=${keyword}"
                   class="${i == currentPage ? 'active' : ''}">${i}</a>
            </c:forEach>
            <c:if test="${currentPage < totalPages}">
                <a href="${pageContext.request.contextPath}/product/list?page=${currentPage + 1}&categoryId=${currentCategoryId}&keyword=${keyword}">下一页</a>
            </c:if>
        </div>
    </c:if>
</section>

<jsp:include page="footer.jsp" />
