package com.raj.rajmart.dao;

import com.raj.rajmart.model.Order;
import com.raj.rajmart.model.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcOrderDAO implements OrderDAO {

    private static final Logger log = LoggerFactory.getLogger(JdbcOrderDAO.class);
    private final DataSource dataSource;

    public JdbcOrderDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public long create(Order order, List<OrderItem> items) {
        String orderSql = "INSERT INTO orders (buyer_id, status, total_amount) VALUES (?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, product_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = dataSource.getConnection()) {
            conn.setAutoCommit(false);
            try {
                long orderId;
                try (PreparedStatement ps = conn.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, order.getBuyerId());
                    ps.setString(2, order.getStatus().name());
                    ps.setBigDecimal(3, order.getTotalAmount());
                    ps.executeUpdate();
                    try (ResultSet keys = ps.getGeneratedKeys()) {
                        if (!keys.next()) {
                            conn.rollback();
                            return -1;
                        }
                        orderId = keys.getLong(1);
                    }
                }
                try (PreparedStatement ips = conn.prepareStatement(itemSql)) {
                    for (OrderItem item : items) {
                        ips.setLong(1, orderId);
                        ips.setLong(2, item.getProductId());
                        ips.setInt(3, item.getQuantity());
                        ips.setBigDecimal(4, item.getUnitPrice());
                        ips.addBatch();
                    }
                    ips.executeBatch();
                }
                conn.commit();
                return orderId;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            log.error("create order failed", e);
            return -1;
        }
    }

    @Override
    public Optional<Order> findById(long id) {
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, u.name AS buyer_name " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(loadItems(conn, id));
                    return Optional.of(order);
                }
            }
        } catch (SQLException e) {
            log.error("findById order failed", e);
        }
        return Optional.empty();
    }

    @Override
    public List<Order> findByBuyer(long buyerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, u.name AS buyer_name " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id WHERE o.buyer_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, buyerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapOrder(rs);
                    o.setItems(loadItems(conn, o.getId()));
                    list.add(o);
                }
            }
        } catch (SQLException e) {
            log.error("findByBuyer failed", e);
        }
        return list;
    }

    @Override
    public List<Order> findBySeller(long sellerId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT DISTINCT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, u.name AS buyer_name " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id " +
                "JOIN order_items oi ON o.id = oi.order_id " +
                "JOIN products p ON oi.product_id = p.id " +
                "WHERE p.seller_id = ? ORDER BY o.created_at DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, sellerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = mapOrder(rs);
                    o.setItems(loadItems(conn, o.getId()));
                    list.add(o);
                }
            }
        } catch (SQLException e) {
            log.error("findBySeller failed", e);
        }
        return list;
    }

    @Override
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.id, o.buyer_id, o.status, o.total_amount, o.created_at, u.name AS buyer_name " +
                "FROM orders o JOIN users u ON o.buyer_id = u.id ORDER BY o.created_at DESC";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = mapOrder(rs);
                o.setItems(loadItems(conn, o.getId()));
                list.add(o);
            }
        } catch (SQLException e) {
            log.error("findAll orders failed", e);
        }
        return list;
    }

    @Override
    public boolean updateStatus(long orderId, Order.Status status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setLong(2, orderId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            log.error("updateStatus failed", e);
            return false;
        }
    }

    private List<OrderItem> loadItems(Connection conn, long orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.id, oi.order_id, oi.product_id, oi.quantity, oi.unit_price, p.name AS product_name " +
                "FROM order_items oi JOIN products p ON oi.product_id = p.id WHERE oi.order_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setId(rs.getLong("id"));
                    item.setOrderId(rs.getLong("order_id"));
                    item.setProductId(rs.getLong("product_id"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    item.setProductName(rs.getString("product_name"));
                    items.add(item);
                }
            }
        }
        return items;
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getLong("id"));
        o.setBuyerId(rs.getLong("buyer_id"));
        o.setStatus(Order.Status.valueOf(rs.getString("status")));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            o.setCreatedAt(ts.toLocalDateTime());
        }
        o.setBuyerName(rs.getString("buyer_name"));
        return o;
    }
}
