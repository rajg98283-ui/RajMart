package com.raj.rajmart.dao;

import com.raj.rajmart.model.Order;
import com.raj.rajmart.model.OrderItem;

import java.util.List;
import java.util.Optional;

public interface OrderDAO {
    long create(Order order, List<OrderItem> items);
    Optional<Order> findById(long id);
    List<Order> findByBuyer(long buyerId);
    List<Order> findBySeller(long sellerId);
    List<Order> findAll();
    boolean updateStatus(long orderId, Order.Status status);
}
