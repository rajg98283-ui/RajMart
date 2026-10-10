package com.raj.rajmart.dao;

import com.raj.rajmart.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of ProductDAO.
 */
public class JdbcProductDAO implements ProductDAO {

    private static final Logger log = LoggerFactory.getLogger(JdbcProductDAO.class);
    private final DataSource dataSource;

    public JdbcProductDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Optional<Product> findById(long id) {
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, " +
                "p.category, p.image_url, p.created_at, u.name AS seller_name " +
                "FROM products p LEFT JOIN users u ON p.seller_id = u.id WHERE p.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findById product failed", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Product> findAll() {
        return search(null, null);
    }

    @Override
    public List<Product> findBySeller(long sellerId) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, " +
                "p.category, p.image_url, p.created_at, u.name AS seller_name " +
                "FROM products p LEFT JOIN users u ON p.seller_id = u.id WHERE p.seller_id = ? ORDER BY p.id DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("findBySeller failed", e);
        }
        return list;
    }

    @Override
    public List<Product> search(String keyword, String category) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT p.id, p.seller_id, p.name, p.description, p.price, p.stock_qty, " +
                "p.category, p.image_url, p.created_at, u.name AS seller_name " +
                "FROM products p LEFT JOIN users u ON p.seller_id = u.id WHERE 1=1");
        List<Object> params = new ArrayList<>();
        if (keyword != null && !keyword.isBlank()) {
            sql.append(" AND (LOWER(p.name) LIKE ? OR LOWER(p.description) LIKE ?)");
            String like = "%" + keyword.toLowerCase() + "%";
            params.add(like);
            params.add(like);
        }
        if (category != null && !category.isBlank()) {
            sql.append(" AND LOWER(p.category) = ?");
            params.add(category.toLowerCase());
        }
        sql.append(" ORDER BY p.created_at DESC");

        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            log.error("search products failed", e);
        }
        return list;
    }

    @Override
    public long create(Product product) {
        String sql = "INSERT INTO products (seller_id, name, description, price, stock_qty, category, image_url) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, product.getSellerId());
            ps.setString(2, product.getName());
            ps.setString(3, product.getDescription());
            ps.setBigDecimal(4, product.getPrice());
            ps.setInt(5, product.getStockQty());
            ps.setString(6, product.getCategory());
            ps.setString(7, product.getImageUrl());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        } catch (SQLException e) {
            log.error("create product failed", e);
        }
        return -1;
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name=?, description=?, price=?, stock_qty=?, category=?, image_url=? WHERE id=? AND seller_id=?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setBigDecimal(3, product.getPrice());
            ps.setInt(4, product.getStockQty());
            ps.setString(5, product.getCategory());
            ps.setString(6, product.getImageUrl());
            ps.setLong(7, product.getId());
            ps.setLong(8, product.getSellerId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("update product failed", e);
            return false;
        }
    }

    @Override
    public boolean delete(long id) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("delete product failed", e);
            return false;
        }
    }

    @Override
    public boolean reduceStock(long productId, int qty) {
        String sql = "UPDATE products SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setLong(2, productId);
            ps.setInt(3, qty);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("reduceStock failed", e);
            return false;
        }
    }

    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getLong("id"));
        p.setSellerId(rs.getLong("seller_id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setCategory(rs.getString("category"));
        p.setImageUrl(rs.getString("image_url"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            p.setCreatedAt(ts.toLocalDateTime());
        }
        try {
            p.setSellerName(rs.getString("seller_name"));
        } catch (SQLException ignored) {
        }
        return p;
    }
}
