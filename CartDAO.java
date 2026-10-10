package com.raj.rajmart.dao;

import com.raj.rajmart.model.CartItem;

import java.util.List;

public interface CartDAO {
    List<CartItem> findByUser(long userId);
    boolean addOrUpdate(long userId, long productId, int quantity);
    boolean updateQuantity(long cartItemId, int quantity);
    boolean remove(long cartItemId, long userId);
    boolean clear(long userId);
}
