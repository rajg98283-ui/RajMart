package com.raj.rajmart.dao;

import com.raj.rajmart.model.CartItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcCartDAO implements CartDAO {

    private static final Logger log = LoggerFactory.getLogger(JdbcCartDAO.class);
    private final DataSource dataSource;

    public JdbcCartDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<CartItem> findByUser(long userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.id, c.user_id, c.product_id, c.quantity, " +
                "p.name AS product_name, p.price AS product_price, p.image_url, p.stock_qty " +
                "FROM cart_items c JOIN products p ON c.product_id = p.id WHERE c.user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    CartItem item = new CartItem();
                    item.setId(rs.getLong("id"));
                    item.setUserId(rs.getLong("user_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setProductName(rs.getString("product_name"));
                    item.setProductPrice(rs.getBigDecimal("product_price"));
                    item.setProductImageUrl(rs.getString("image_url"));
                    item.setStockQty(rs.getInt("stock_qty"));
                    list.add(item);
                }
            }
        } catch (SQLException e) {
            log.error("findByUser cart failed", e);
        }
        return list;
    }

    @Override
    public boolean addOrUpdate(long userId, long productId, int quantity) {
        // Upsert: if exists increase, else insert
        String check = "SELECT id, quantity FROM cart_items WHERE user_id = ? AND product_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(check)) {
            ps.setLong(1, userId);
            ps.setLong(2, productId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long id = rs.getLong("id");
                    int newQty = rs.getInt("quantity") + quantity;
                    String upd = "UPDATE cart_items SET quantity = ? WHERE id = ?";
                    try (PreparedStatement ups = conn.prepareStatement(upd)) {
                        ups.setInt(1, newQty);
                        ups.setLong(2, id);
                        return ups.executeUpdate() > 0;
                    }
                }
            }
            String ins = "INSERT INTO cart_items (user_id, product_id, quantity) VALUES (?, ?, ?)";
            try (PreparedStatement ips = conn.prepareStatement(ins)) {
                ips.setLong(1, userId);
                ips.setLong(2, productId);
                ips.setInt(3, quantity);
                return ips.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            log.error("addOrUpdate cart failed", e);
            return false;
        }
    }

    @Override
    public boolean updateQuantity(long cartItemId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setLong(2, cartItemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("updateQuantity failed", e);
            return false;
        }
    }

    @Override
    public boolean remove(long cartItemId, long userId) {
        String sql = "DELETE FROM cart_items WHERE id = ? AND user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cartItemId);
            ps.setLong(2, userId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("remove cart item failed", e);
            return false;
        }
    }

    @Override
    public boolean clear(long userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, userId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            log.error("clear cart failed", e);
            return false;
        }
    }
}
