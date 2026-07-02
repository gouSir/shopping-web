<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    com.shop.model.User currentUser = (com.shop.model.User) session.getAttribute("user");
    com.shop.dao.CartDao headerCartDao = new com.shop.dao.CartDao();
    int cartCount = currentUser != null ? headerCartDao.getCount(currentUser.getId()) : 0;
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>简易购物网站 - Java Web期末作业</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<!-- 顶部导航栏 -->
<header class="header">
    <div class="container">
        <div class="header-left">
            <a href="${pageContext.request.contextPath}/index.jsp" class="logo">🛒 简易购物</a>
        </div>
        <div class="header-center">
            <form action="${pageContext.request.contextPath}/product/list" method="get" class="search-form">
                <input type="text" name="keyword" placeholder="搜索商品..." value="${param.keyword}">
                <button type="submit">搜索</button>
            </form>
        </div>
        <nav class="header-right">
            <a href="${pageContext.request.contextPath}/product/list">全部商品</a>
            <a href="${pageContext.request.contextPath}/cart?action=list" class="cart-link">
                🛒 购物车
                <% if (cartCount > 0) { %>
                    <span class="cart-badge"><%= cartCount %></span>
                <% } %>
            </a>
            <% if (currentUser != null) { %>
                <a href="${pageContext.request.contextPath}/order?action=list">我的订单</a>
                <% if (currentUser.isAdmin()) { %>
                    <a href="${pageContext.request.contextPath}/admin/index">后台管理</a>
                <% } %>
                <span class="user-info">👤 <%= currentUser.getUsername() %></span>
                <a href="${pageContext.request.contextPath}/logout">退出</a>
            <% } else { %>
                <a href="${pageContext.request.contextPath}/login.jsp">登录</a>
                <a href="${pageContext.request.contextPath}/register.jsp">注册</a>
            <% } %>
        </nav>
    </div>
</header>
<main class="main container">
