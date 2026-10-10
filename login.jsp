<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Login — RajMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">RajMart</a>
</nav>
<div class="form-card">
    <h2 style="margin-bottom:1rem;">Login</h2>
    <c:if test="${param.registered == '1'}">
        <div class="alert alert-success">Registration successful! Please login.</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert alert-error"><c:out value="${error}"/></div>
    </c:if>
    <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="form-group">
            <label>Email</label>
            <input type="email" name="email" required placeholder="you@example.com">
        </div>
        <div class="form-group">
            <label>Password</label>
            <input type="password" name="password" required>
        </div>
        <button type="submit" class="btn btn-primary" style="width:100%;">Login</button>
    </form>
    <p class="muted" style="margin-top:1rem;text-align:center;">
        No account? <a href="${pageContext.request.contextPath}/register.jsp">Register</a>
    </p>
    <hr style="margin:1.25rem 0;border:none;border-top:1px solid #e2e8f0;">
    <p class="muted" style="font-size:0.8rem;">Demo accounts (password: <b>password123</b>)<br>
        buyer@rajmart.com · seller@rajmart.com · admin@rajmart.com</p>
</div>
</body>
</html>
