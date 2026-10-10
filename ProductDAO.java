package com.raj.rajmart.dao;

import com.raj.rajmart.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Data access interface for Product entity.
 */
public interface ProductDAO {

    Optional<Product> findById(long id);

    List<Product> findAll();

    List<Product> findBySeller(long sellerId);

    List<Product> search(String keyword, String category);

    long create(Product product);

    boolean update(Product product);

    boolean delete(long id);

    boolean reduceStock(long productId, int qty);
}
