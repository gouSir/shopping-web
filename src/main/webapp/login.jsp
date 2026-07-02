<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="header.jsp" />

<section class="auth-section">
    <div class="auth-card">
        <h2>用户登录</h2>
        <% if (request.getParameter("msg") != null && request.getParameter("msg").equals("registered")) { %>
            <p class="msg-success">✅ 注册成功，请登录！</p>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <p class="msg-error">❌ ${error}</p>
        <% } %>
        <form action="${pageContext.request.contextPath}/login" method="post" class="auth-form">
            <div class="form-group">
                <label for="username">用户名</label>
                <input type="text" id="username" name="username" placeholder="请输入用户名" required>
            </div>
            <div class="form-group">
                <label for="password">密码</label>
                <input type="password" id="password" name="password" placeholder="请输入密码" required>
            </div>
            <button type="submit" class="btn btn-primary btn-block">登 录</button>
        </form>
        <p class="auth-switch">还没有账号？<a href="${pageContext.request.contextPath}/register.jsp">立即注册</a></p>
        <p class="auth-hint">测试账号：admin / admin123（管理员） ｜ test / test123（普通用户）</p>
    </div>
</section>

<jsp:include page="footer.jsp" />
