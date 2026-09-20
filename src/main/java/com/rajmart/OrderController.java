package com.rajmart;

import org.springframework.web.bind.annotation.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {


    // ==============================
// PLACE ORDER
// ==============================
    @PostMapping
    public Map<String, Object> placeOrder(
            @RequestBody Map<String, Object> orderData) {

        Map<String, Object> response = new HashMap<>();

        try {

            String userEmail =
                    (String) orderData.get("userEmail");

            String customerName =
                    (String) orderData.get("customerName");

            String deliveryAddress =
                    (String) orderData.get("deliveryAddress");

            String paymentMethod =
                    (String) orderData.get("paymentMethod");

            double subtotal =
                    Double.parseDouble(
                            orderData.get("subtotal").toString()
                    );

            double deliveryCharge =
                    Double.parseDouble(
                            orderData.get("deliveryCharge").toString()
                    );

            double serviceCharge =
                    Double.parseDouble(
                            orderData.get("serviceCharge").toString()
                    );

            double grandTotal =
                    Double.parseDouble(
                            orderData.get("grandTotal").toString()
                    );

            int customerId =
                    OrderDAO.getCustomerIdByEmail(userEmail);

            if (customerId == -1) {

                response.put("success", false);
                response.put(
                        "message",
                        "Customer account not found."
                );

                return response;
            }

            int orderId =
                    OrderDAO.createOrder(
                            customerId,
                            subtotal,
                            deliveryCharge,
                            serviceCharge,
                            grandTotal,
                            deliveryAddress,
                            paymentMethod,
                            "PAID"
                    );

            if (orderId == -1) {

                response.put("success", false);
                response.put(
                        "message",
                        "Failed to create order."
                );

                return response;
            }

            ArrayList<Map<String, Object>> items =
                    (ArrayList<Map<String, Object>>)
                            orderData.get("items");

            if (items == null || items.isEmpty()) {

                response.put("success", false);
                response.put(
                        "message",
                        "Cart is empty."
                );

                return response;
            }

            for (Map<String, Object> item : items) {

                int productId =
                        Integer.parseInt(
                                item.get("productId").toString()
                        );

                int quantity =
                        Integer.parseInt(
                                item.get("quantity").toString()
                        );

                double price =
                        Double.parseDouble(
                                item.get("price").toString()
                        );

                String productName =
                        getProductName(productId);

                if (productName == null) {

                    response.put("success", false);
                    response.put(
                            "message",
                            "Product not found: " + productId
                    );

                    return response;
                }

                int stock =
                        getCurrentStock(productId);

                if (stock < quantity) {

                    response.put("success", false);
                    response.put(
                            "message",
                            "Insufficient stock for "
                                    + productName
                    );

                    return response;
                }

                double total =
                        price * quantity;

                boolean itemSaved =
                        OrderDAO.addOrderItem(
                                orderId,
                                productId,
                                productName,
                                quantity,
                                price,
                                total
                        );

                if (!itemSaved) {

                    response.put("success", false);
                    response.put(
                            "message",
                            "Failed to save order item."
                    );

                    return response;
                }

                boolean stockUpdated =
                        reduceStock(
                                productId,
                                quantity
                        );

                if (!stockUpdated) {

                    response.put("success", false);
                    response.put(
                            "message",
                            "Failed to update product stock."
                    );

                    return response;
                }
            }

            response.put("success", true);
            response.put(
                    "message",
                    "Order placed successfully."
            );

            response.put("orderId", orderId);
            response.put("grandTotal", grandTotal);

            return response;

        } catch (Exception e) {

            e.printStackTrace();

            response.put("success", false);
            response.put(
                    "message",
                    "Order failed: " + e.getMessage()
            );

            return response;
        }
    }


    // ==============================
// GET ALL ORDERS
// ==============================
    @GetMapping
    public ArrayList<Map<String, Object>> getOrders() {

        return OrderDAO.getAllOrders();
    }


    // ==============================
// GET USER ORDERS
// ==============================
    @GetMapping("/user")
    public ArrayList<Map<String, Object>>
    getUserOrders(
            @RequestParam String email) {

        return OrderDAO.getOrdersByUser(email);
    }


    // ==============================
// GET ORDER ITEMS
// ==============================
    @GetMapping("/{orderId}/items")
    public ArrayList<Map<String, Object>>
    getOrderItems(
            @PathVariable int orderId) {

        return OrderDAO.getOrderItems(orderId);
    }


    // ==============================
// ADMIN INCOMING ORDERS
// ==============================
    @GetMapping("/incoming")
    public ArrayList<Map<String, Object>>
    getIncomingOrders() {

        return OrderDAO.getIncomingOrders();
    }


    // ==============================
// UPDATE ORDER STATUS
// ==============================
    @PutMapping("/{orderId}/status")
    public Map<String, Object>
    updateStatus(
            @PathVariable int orderId,
            @RequestBody Map<String, String> data) {

        Map<String, Object> response =
                new HashMap<>();

        String status =
                data.get("status");

        boolean updated =
                OrderDAO.updateOrderStatus(
                        orderId,
                        status
                );

        response.put(
                "success",
                updated
        );

        if (updated) {

            response.put(
                    "message",
                    "Order status updated."
            );

        } else {

            response.put(
                    "message",
                    "Failed to update order status."
            );
        }

        return response;
    }


    // ==============================
// CUSTOMER CANCEL ORDER
// ==============================
    @PutMapping("/{orderId}/cancel")
    public Map<String, Object> cancelOrder(
            @PathVariable int orderId,
            @RequestParam String email) {

        return OrderDAO.cancelOrder(
                orderId,
                email
        );
    }


    // ==============================
// GET PRODUCT NAME
// ==============================
    private String getProductName(int productId) {

        String sql =
                "SELECT name FROM products WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, productId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getString("name");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return null;
    }


    // ==============================
// GET CURRENT STOCK
// ==============================
    private int getCurrentStock(int productId) {

        String sql =
                "SELECT stock FROM products WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, productId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return resultSet.getInt("stock");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return 0;
    }


    // ==============================
// REDUCE STOCK
// ==============================
    private boolean reduceStock(
            int productId,
            int quantity) {

        String sql =
                "UPDATE products " +
                        "SET stock = stock - ? " +
                        "WHERE id = ? AND stock >= ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, quantity);
            statement.setInt(2, productId);
            statement.setInt(3, quantity);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


}
