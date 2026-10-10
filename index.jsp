<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>RajMart — Multi-seller Marketplace</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">RajMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/cart">Cart</a>
                <a href="${pageContext.request.contextPath}/orders">Orders</a>
                <span class="muted">Hi, <c:out value="${sessionScope.user.name}"/></span>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
                <a class="btn btn-primary btn-sm" href="${pageContext.request.contextPath}/register.jsp">Register</a>
            </c:otherwise>
        </c:choose>
    </nav>
</nav>

<section class="hero">
    <h1>Welcome to RajMart</h1>
    <p>Buy and sell products from multiple sellers in one place.</p>
    <a class="btn btn-primary" href="${pageContext.request.contextPath}/products" style="background:white;color:#2563eb;">Browse Products</a>
</section>

<div class="container">
    <h2 style="margin-top:2rem;">Featured Categories</h2>
    <div class="grid">
        <div class="card"><div class="card-body"><h3>Electronics</h3><p class="muted">Gadgets & devices</p>
            <a href="${pageContext.request.contextPath}/products?category=Electronics">Shop →</a></div></div>
        <div class="card"><div class="card-body"><h3>Fashion</h3><p class="muted">Clothing & accessories</p>
            <a href="${pageContext.request.contextPath}/products?category=Fashion">Shop →</a></div></div>
        <div class="card"><div class="card-body"><h3>Home</h3><p class="muted">Home essentials</p>
            <a href="${pageContext.request.contextPath}/products?category=Home">Shop →</a></div></div>
        <div class="card"><div class="card-body"><h3>Grocery</h3><p class="muted">Food & beverages</p>
            <a href="${pageContext.request.contextPath}/products?category=Grocery">Shop →</a></div></div>
    </div>
</div>

<footer>© 2026 RajMart · Built with Java Servlets + JDBC · Capstone Project</footer>
</body>
</html>
