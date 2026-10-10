package com.raj.rajmart.controller;

import com.raj.rajmart.dao.JdbcCartDAO;
import com.raj.rajmart.dao.JdbcProductDAO;
import com.raj.rajmart.listener.DataSourceListener;
import com.raj.rajmart.model.CartItem;
import com.raj.rajmart.model.User;
import com.raj.rajmart.service.CartService;
import com.raj.rajmart.util.JsonUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = {"/api/v1/cart/*", "/cart"})
public class CartServlet extends HttpServlet {

    private CartService cartService;

    @Override
    public void init() {
        DataSource ds = DataSourceListener.getDataSource(getServletContext());
        cartService = new CartService(new JdbcCartDAO(ds), new JdbcProductDAO(ds));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }
        List<CartItem> items = cartService.getCart(user.getId());
        boolean isApi = req.getRequestURI().contains("/api/");
        if (isApi) {
            Map<String, Object> data = new HashMap<>();
            data.put("items", items);
            data.put("total", cartService.getTotal(user.getId()));
            JsonUtil.writeSuccess(resp, data);
        } else {
            req.setAttribute("cartItems", items);
            req.setAttribute("cartTotal", cartService.getTotal(user.getId()));
            req.getRequestDispatcher("/cart.jsp").forward(req, resp);
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
            if ("add".equals(action)) {
                long productId = Long.parseLong(req.getParameter("productId"));
                int qty = Integer.parseInt(req.getParameter("quantity") != null ? req.getParameter("quantity") : "1");
                cartService.addItem(user.getId(), productId, qty);
                JsonUtil.writeSuccess(resp, Map.of("message", "Added to cart"));
            } else if ("update".equals(action)) {
                long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                int qty = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateQuantity(cartItemId, qty);
                JsonUtil.writeSuccess(resp, Map.of("message", "Updated"));
            } else if ("remove".equals(action)) {
                long cartItemId = Long.parseLong(req.getParameter("cartItemId"));
                cartService.removeItem(cartItemId, user.getId());
                JsonUtil.writeSuccess(resp, Map.of("message", "Removed"));
            } else {
                JsonUtil.writeError(resp, 400, "BAD_REQUEST", "Unknown action");
            }
        } catch (Exception e) {
            JsonUtil.writeError(resp, 400, "VALIDATION_ERROR", e.getMessage());
        }
    }
}
