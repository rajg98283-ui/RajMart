package com.raj.rajmart.service;

import com.raj.rajmart.dao.ProductDAO;
import com.raj.rajmart.model.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class ProductService {

    private final ProductDAO productDAO;

    public ProductService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    public List<Product> listAll() {
        return productDAO.findAll();
    }

    public List<Product> search(String keyword, String category) {
        return productDAO.search(keyword, category);
    }

    public Optional<Product> getById(long id) {
        return productDAO.findById(id);
    }

    public List<Product> listBySeller(long sellerId) {
        return productDAO.findBySeller(sellerId);
    }

    public long create(long sellerId, String name, String description, BigDecimal price,
                       int stock, String category, String imageUrl) {
        if (name == null || name.isBlank() || price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Invalid product data");
        }
        Product p = new Product();
        p.setSellerId(sellerId);
        p.setName(name.trim());
        p.setDescription(description);
        p.setPrice(price);
        p.setStockQty(Math.max(0, stock));
        p.setCategory(category);
        p.setImageUrl(imageUrl);
        return productDAO.create(p);
    }

    public boolean update(Product product) {
        return productDAO.update(product);
    }

    public boolean delete(long id) {
        return productDAO.delete(id);
    }
}
