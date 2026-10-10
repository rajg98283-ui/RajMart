package com.raj.rajmart.service;

import com.raj.rajmart.dao.CartDAO;
import com.raj.rajmart.dao.ProductDAO;
import com.raj.rajmart.model.CartItem;
import com.raj.rajmart.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class CartService {

    private final CartDAO cartDAO;
    private final ProductDAO productDAO;

    public CartService(CartDAO cartDAO, ProductDAO productDAO) {
        this.cartDAO = cartDAO;
        this.productDAO = productDAO;
    }

    public List<CartItem> getCart(long userId) {
        return cartDAO.findByUser(userId);
    }

    public BigDecimal getTotal(long userId) {
        return getCart(userId).stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public boolean addItem(long userId, long productId, int qty) {
        if (qty < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1");
        }
        Optional<Product> p = productDAO.findById(productId);
        if (p.isEmpty() || p.get().getStockQty() < qty) {
            throw new IllegalArgumentException("Product unavailable or insufficient stock");
        }
        return cartDAO.addOrUpdate(userId, productId, qty);
    }

    public boolean updateQuantity(long cartItemId, int qty) {
        if (qty < 1) {
            return false;
        }
        return cartDAO.updateQuantity(cartItemId, qty);
    }

    public boolean removeItem(long cartItemId, long userId) {
        return cartDAO.remove(cartItemId, userId);
    }

    public boolean clear(long userId) {
        return cartDAO.clear(userId);
    }
}
