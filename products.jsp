<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Products — RajMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">RajMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <c:if test="${not empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/cart">Cart</a>
            <a href="${pageContext.request.contextPath}/orders">Orders</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </c:if>
        <c:if test="${empty sessionScope.user}">
            <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
        </c:if>
    </nav>
</nav>
<div class="container">
    <h1>Products</h1>
    <form class="search-bar" method="get" action="${pageContext.request.contextPath}/products">
        <input type="text" name="q" placeholder="Search products..." value="${param.q}" style="flex:1;min-width:180px;">
        <select name="category">
            <option value="">All Categories</option>
            <option value="Electronics" ${param.category == 'Electronics' ? 'selected' : ''}>Electronics</option>
            <option value="Fashion" ${param.category == 'Fashion' ? 'selected' : ''}>Fashion</option>
            <option value="Home" ${param.category == 'Home' ? 'selected' : ''}>Home</option>
            <option value="Grocery" ${param.category == 'Grocery' ? 'selected' : ''}>Grocery</option>
        </select>
        <button type="submit" class="btn btn-primary">Search</button>
    </form>

    <div class="grid">
        <c:forEach var="p" items="${products}">
            <div class="card">
                <img src="${empty p.imageUrl ? 'https://via.placeholder.com/300x300?text=Product' : p.imageUrl}" alt="<c:out value='${p.name}'/>">
                <div class="card-body">
                    <h3><c:out value="${p.name}"/></h3>
                    <p class="muted"><c:out value="${p.category}"/> · <c:out value="${p.sellerName}"/></p>
                    <p class="price">₹<fmt:formatNumber value="${p.price}" minFractionDigits="2"/></p>
                    <p class="muted">Stock: ${p.stockQty}</p>
                    <c:if test="${not empty sessionScope.user}">
                        <form method="post" action="${pageContext.request.contextPath}/api/v1/cart" style="margin-top:0.5rem;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="${p.id}">
                            <input type="hidden" name="quantity" value="1">
                            <button type="submit" class="btn btn-primary btn-sm">Add to Cart</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </c:forEach>
    </div>
    <c:if test="${empty products}">
        <p class="muted" style="margin-top:2rem;">No products found.</p>
    </c:if>
</div>
<footer>© 2026 RajMart</footer>
</body>
</html>
