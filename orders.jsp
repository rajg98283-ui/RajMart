<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Orders — RajMart</title>
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
    <h1>Orders</h1>
    <c:choose>
        <c:when test="${empty orders}">
            <p class="muted" style="margin-top:1.5rem;">No orders yet.</p>
        </c:when>
        <c:otherwise>
            <table style="margin-top:1.5rem;">
                <thead>
                <tr><th>Order #</th><th>Buyer</th><th>Status</th><th>Total</th><th>Date</th></tr>
                </thead>
                <tbody>
                <c:forEach var="o" items="${orders}">
                    <tr>
                        <td>#${o.id}</td>
                        <td><c:out value="${o.buyerName}"/></td>
                        <td><span class="badge"><c:out value="${o.status}"/></span></td>
                        <td>₹<fmt:formatNumber value="${o.totalAmount}" minFractionDigits="2"/></td>
                        <td class="muted">${o.createdAt}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>
