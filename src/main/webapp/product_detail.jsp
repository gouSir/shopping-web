<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="header.jsp" />

<section class="section">
    <c:if test="${empty product}">
        <div class="empty-state"><p>商品不存在</p></div>
    </c:if>
    <c:if test="${not empty product}">
        <div class="detail-container">
            <div class="detail-img">
                <img src="${pageContext.request.contextPath}/images/${product.image}" alt="${product.name}" onerror="this.src='${pageContext.request.contextPath}/images/default.jpg'">
            </div>
            <div class="detail-info">
                <span class="product-category">${product.categoryName}</span>
                <h1>${product.name}</h1>
                <p class="detail-price">¥${product.price}</p>
                <p class="detail-stock ${product.stock > 0 ? 'in-stock' : 'out-of-stock'}">
                    ${product.stock > 0 ? '库存 ' + product.stock + ' 件' : '暂时缺货'}
                </p>
                <div class="detail-desc">
                    <h3>商品描述</h3>
                    <p>${product.description}</p>
                </div>
                <c:if test="${product.stock > 0}">
                    <form action="${pageContext.request.contextPath}/cart" method="post" class="add-cart-form">
                        <input type="hidden" name="action" value="add">
                        <input type="hidden" name="productId" value="${product.id}">
                        <div class="quantity-control">
                            <button type="button" onclick="changeQty(-1)">−</button>
                            <input type="number" name="quantity" id="quantity" value="1" min="1" max="${product.stock}" readonly>
                            <button type="button" onclick="changeQty(1)">+</button>
                        </div>
                        <button type="submit" class="btn btn-primary btn-lg">加入购物车</button>
                    </form>
                </c:if>
            </div>
        </div>
    </c:if>
</section>

<script>
function changeQty(delta) {
    var input = document.getElementById('quantity');
    var val = parseInt(input.value) + delta;
    var max = ${product.stock};
    if (val < 1) val = 1;
    if (val > max) val = max;
    input.value = val;
}
</script>

<jsp:include page="footer.jsp" />
