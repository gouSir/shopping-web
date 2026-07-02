<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />

<!-- 首页横幅 -->
<section class="hero">
    <div class="hero-content">
        <h1>欢迎来到简易购物商城</h1>
        <p>精选好物，实惠价格，快速下单</p>
        <a href="${pageContext.request.contextPath}/product/list" class="btn btn-primary">立即选购</a>
    </div>
</section>

<!-- 首页商品推荐（最新8件商品） -->
<section class="section">
    <h2 class="section-title">🔥 热门推荐</h2>
    <div class="product-grid">
        <jsp:useBean id="productDao" class="com.shop.dao.ProductDao" scope="page" />
        <c:forEach var="p" items="${productDao.findAll(null, null, 1, 8)}">
            <div class="product-card">
                <div class="product-img">
                    <img src="${pageContext.request.contextPath}/images/${p.image}" alt="${p.name}" onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
                </div>
                <div class="product-info">
                    <h3><a href="${pageContext.request.contextPath}/product/detail/${p.id}">${p.name}</a></h3>
                    <p class="product-desc">${p.description}</p>
                    <div class="product-bottom">
                        <span class="product-price">¥${p.price}</span>
                        <a href="${pageContext.request.contextPath}/cart?action=add&productId=${p.id}&quantity=1" class="btn btn-sm btn-primary">加入购物车</a>
                    </div>
                </div>
            </div>
        </c:forEach>
    </div>
</section>

<jsp:include page="footer.jsp" />
