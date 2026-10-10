package com.raj.rajmart.controller;

import com.raj.rajmart.dao.JdbcCartDAO;
import com.raj.rajmart.dao.JdbcOrderDAO;
import com.raj.rajmart.dao.JdbcProductDAO;
import com.raj.rajmart.listener.DataSourceListener;
import com.raj.rajmart.model.Order;
import com.raj.rajmart.model.User;
import com.raj.rajmart.service.OrderService;
import com.raj.rajmart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/v1/orders/*", "/orders", "/checkout"})
public class OrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        DataSource ds = DataSourceListener.getDataSource(getServletContext());
        orderService = new OrderService(new JdbcOrderDAO(ds), new JdbcCartDAO(ds), new JdbcProductDAO(ds));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        String path = req.getRequestURI().substring(req.getContextPath().length());
        boolean isApi = path.startsWith("/api/");

        if (path.contains("/checkout")) {
            req.getRequestDispatcher("/checkout.jsp").forward(req, resp);
            return;
        }

        List<Order> orders;
        if (user.getRole() == User.Role.ADMIN) {
            orders = orderService.getAllOrders();
        } else if (user.getRole() == User.Role.SELLER) {
            orders = orderService.getSellerOrders(user.getId());
        } else {
            orders = orderService.getBuyerOrders(user.getId());
        }

        if (isApi) {
            JsonUtil.writeSuccess(resp, orders);
        } else {
            req.setAttribute("orders", orders);
            req.getRequestDispatcher("/orders.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            JsonUtil.writeError(resp, 401, "UNAUTHORIZED", "Login required");
            return;
        }
        String action = req.getParameter("action");
        try {
            if ("place".equals(action) || "checkout".equals(action)) {
                // mock payment confirmation
                String paymentConfirm = req.getParameter("paymentConfirm");
                if (!"yes".equalsIgnoreCase(paymentConfirm) && !"true".equalsIgnoreCase(paymentConfirm)) {
                    JsonUtil.writeError(resp, 400, "PAYMENT_REQUIRED", "Mock payment confirmation required (paymentConfirm=yes)");
                    return;
                }
                long orderId = orderService.placeOrder(user.getId());
                JsonUtil.writeSuccess(resp, 201, Map.of("orderId", orderId, "message", "Order placed successfully (mock payment)"));
            } else if ("status".equals(action) && (user.getRole() == User.Role.SELLER || user.getRole() == User.Role.ADMIN)) {
                long orderId = Long.parseLong(req.getParameter("orderId"));
                Order.Status status = Order.Status.valueOf(req.getParameter("status").toUpperCase());
                orderService.updateStatus(orderId, status);
                JsonUtil.writeSuccess(resp, Map.of("message", "Status updated"));
            } else {
                JsonUtil.writeError(resp, 400, "BAD_REQUEST", "Unknown action");
            }
        } catch (Exception e) {
            JsonUtil.writeError(resp, 400, "ERROR", e.getMessage());
        }
    }
}
