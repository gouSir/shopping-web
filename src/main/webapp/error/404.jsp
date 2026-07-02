<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true"%>
<jsp:include page="/header.jsp" />

<section class="section">
    <div class="error-page">
        <h1>404</h1>
        <p>抱歉，您访问的页面不存在</p>
        <a href="${pageContext.request.contextPath}/index.jsp" class="btn btn-primary">返回首页</a>
    </div>
</section>

<jsp:include page="/footer.jsp" />
