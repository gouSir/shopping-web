<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/header.jsp" />

<section class="section">
    <h2 class="section-title">⚙️ 后台管理</h2>
    <div class="admin-cards">
        <a href="${pageContext.request.contextPath}/admin/product" class="admin-card">
            <div class="admin-card-icon">📦</div>
            <h3>商品管理</h3>
            <p>添加、编辑、删除商品</p>
        </a>
        <a href="${pageContext.request.contextPath}/admin/order" class="admin-card">
            <div class="admin-card-icon">📋</div>
            <h3>订单管理</h3>
            <p>查看订单、更新状态</p>
        </a>
        <a href="${pageContext.request.contextPath}/index.jsp" class="admin-card">
            <div class="admin-card-icon">🏠</div>
            <h3>返回前台</h3>
            <p>回到商城首页</p>
        </a>
    </div>
</section>

<jsp:include page="/footer.jsp" />
