<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />

<section class="auth-section">
    <div class="auth-card">
        <h2>用户注册</h2>
        <% if (request.getAttribute("error") != null) { %>
            <p class="msg-error">❌ ${error}</p>
        <% } %>
        <form action="${pageContext.request.contextPath}/register" method="post" class="auth-form">
            <div class="form-group">
                <label for="username">用户名 <span class="required">*</span></label>
                <input type="text" id="username" name="username" placeholder="3-20位字符" required minlength="3" maxlength="20">
            </div>
            <div class="form-group">
                <label for="email">邮箱</label>
                <input type="email" id="email" name="email" placeholder="请输入邮箱">
            </div>
            <div class="form-group">
                <label for="password">密码 <span class="required">*</span></label>
                <input type="password" id="password" name="password" placeholder="至少6位密码" required minlength="6">
            </div>
            <div class="form-group">
                <label for="password2">确认密码 <span class="required">*</span></label>
                <input type="password" id="password2" name="password2" placeholder="再次输入密码" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">注 册</button>
        </form>
        <p class="auth-switch">已有账号？<a href="${pageContext.request.contextPath}/login.jsp">立即登录</a></p>
    </div>
</section>

<jsp:include page="footer.jsp" />
