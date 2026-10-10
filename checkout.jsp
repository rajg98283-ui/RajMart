<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Checkout — RajMart</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<nav class="navbar">
    <a class="brand" href="${pageContext.request.contextPath}/">RajMart</a>
</nav>
<div class="form-card">
    <h2>Mock Payment Confirmation</h2>
    <p class="muted" style="margin:1rem 0;">This is a simulated payment step. No real payment is processed.</p>
    <form method="post" action="${pageContext.request.contextPath}/api/v1/orders">
        <input type="hidden" name="action" value="place">
        <input type="hidden" name="paymentConfirm" value="yes">
        <button type="submit" class="btn btn-primary" style="width:100%;">Confirm & Place Order</button>
    </form>
    <p style="margin-top:1rem;text-align:center;"><a href="${pageContext.request.contextPath}/cart">← Back to Cart</a></p>
</div>
<script>
document.querySelector('form').addEventListener('submit', async function(e) {
  e.preventDefault();
  const fd = new FormData(this);
  const res = await fetch(this.action, { method: 'POST', body: new URLSearchParams(fd) });
  const json = await res.json();
  if (json.success) {
    alert('Order placed! Order ID: ' + json.data.orderId);
    window.location = '${pageContext.request.contextPath}/orders';
  } else {
    alert(json.error ? json.error.message : 'Failed');
  }
});
</script>
</body>
</html>
