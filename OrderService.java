package com.raj.rajmart.service;

import com.raj.rajmart.dao.CartDAO;
import com.raj.rajmart.dao.OrderDAO;
import com.raj.rajmart.dao.ProductDAO;
import com.raj.rajmart.model.CartItem;
import com.raj.rajmart.model.Order;
import com.raj.rajmart.model.OrderItem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public OrderService(OrderDAO orderDAO, CartDAO cartDAO, ProductDAO productDAO) {
        this.orderDAO = orderDAO;
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    /**
     * Place order from cart with mock payment confirmation.
     */
    public long placeOrder(long buyerId) {
        List<CartItem> cart = cartDAO.findByUser(buyerId);
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (CartItem ci : cart) {
            if (ci.getStockQty() < ci.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for: " + ci.getProductName());
            }
            OrderItem oi = new OrderItem();
            oi.setProductId(ci.getProductId());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getProductPrice());
            items.add(oi);
            total = total.add(ci.getLineTotal());
        }

        Order order = new Order();
        order.setBuyerId(buyerId);
        order.setStatus(Order.Status.PENDING);
        order.setTotalAmount(total);

        long orderId = orderDAO.create(order, items);
        if (orderId < 0) {
            throw new RuntimeException("Failed to create order");
        }

        // reduce stock
        for (OrderItem oi : items) {
            productDAO.reduceStock(oi.getProductId(), oi.getQuantity());
        }
        // clear cart
        cartDAO.clear(buyerId);
        return orderId;
    }

    public List<Order> getBuyerOrders(long buyerId) {
        return orderDAO.findByBuyer(buyerId);
    }

    public List<Order> getSellerOrders(long sellerId) {
        return orderDAO.findBySeller(sellerId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public Optional<Order> getOrder(long id) {
        return orderDAO.findById(id);
    }

    public boolean updateStatus(long orderId, Order.Status status) {
        return orderDAO.updateStatus(orderId, status);
    }
}
