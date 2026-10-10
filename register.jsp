<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Register — RajMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">RajMart</a>
</nav>
<div class="form-card">
    <h2 style="margin-bottom:1rem;">Create Account</h2>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/register">
        <div class="form-group">
            <label>Full Name</label>
            <input type="text" name="name" required>
        </div>
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required>
        </div>
        <div class="form-group">
            <label>Password (min 6 chars)</label>
            <input type="password" name="password" required minlength="6">
        </div>
        <div class="form-group">
            <label>I want to</label>
            <select name="role">
                <option value="BUYER">Buy products</option>
                <option value="SELLER">Sell products</option>
            </select>
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;">Register</button>
    </form>
    <p class="muted" style="margin-top:1rem;text-align:center;">
        Already have an account? <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
    </p>
</div>
</body>
</html>
