<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<jsp:include page="/header.jsp" />

<section class="section">
    <div class="error-page">
        <h1>500</h1>
        <p>服务器内部错误，请稍后重试</p>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">返回首页</a>
    </div>
</section>

<jsp:include page="/footer.jsp" />
