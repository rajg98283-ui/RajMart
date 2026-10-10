<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Cart — RajMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">RajMart</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">Cart</a>
        <a href="${pageContext.request.contextPath}/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </nav>
</nav>
<div class="container">
    <h1>Your Cart</h1>
    <c:choose>
        <c:when test="${empty cartItems}">
            <p class="muted" style="margin-top:1.5rem;">Cart is empty. <a href="${pageContext.request.contextPath}/products">Browse products</a></p>
        </c:when>
        <c:otherwise>
            <table style="margin-top:1.5rem;">
                <thead>
                <tr><th>Product</th><th>Price</th><th>Qty</th><th>Total</th><th></th></tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${cartItems}">
                    <tr>
                        <td><c:out value="${item.productName}"/></td>
                        <td>₹<fmt:formatNumber value="${item.productPrice}" minFractionDigits="2"/></td>
                        <td>${item.quantity}</td>
                        <td>₹<fmt:formatNumber value="${item.lineTotal}" minFractionDigits="2"/></td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/api/v1/cart" style="display:inline;">
                                <input type="hidden" name="action" value="remove">
                                <input type="hidden" name="cartItemId" value="${item.id}">
                                <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
            <p style="margin-top:1rem;font-size:1.25rem;font-weight:700;">
                Total: ₹<fmt:formatNumber value="${cartTotal}" minFractionDigits="2"/>
            </p>
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/checkout" style="margin-top:1rem;">Proceed to Checkout</a>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
