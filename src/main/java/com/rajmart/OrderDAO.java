package com.rajmart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDAO {

    // =========================================
    // GET CUSTOMER ID
    // =========================================

    public static int getCustomerIdByEmail(String email) {

        String sql =
                "SELECT id FROM users WHERE email = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }


    // =========================================
    // CREATE ORDER
    // =========================================

    public static int createOrder(
            int customerId,
            double subtotal,
            double deliveryCharge,
            double serviceCharge,
            double grandTotal,
            String deliveryAddress,
            String paymentMethod,
            String paymentStatus) {

        String customerName;
        String userEmail;

        String userSql =
                "SELECT name, email FROM users WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(userSql)
        ) {

            statement.setInt(1, customerId);

            try (ResultSet resultSet =
                         statement.executeQuery()) {

                if (!resultSet.next()) {
                    return -1;
                }

                customerName =
                        resultSet.getString("name");

                userEmail =
                        resultSet.getString("email");
            }

        } catch (Exception e) {

            e.printStackTrace();

            return -1;
        }


        String sql =
                "INSERT INTO orders " +
                        "(user_email, customer_name, delivery_address, " +
                        "payment_method, subtotal, delivery_charge, " +
                        "service_charge, grand_total, status) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, userEmail);
            statement.setString(2, customerName);
            statement.setString(3, deliveryAddress);
            statement.setString(4, paymentMethod);
            statement.setDouble(5, subtotal);
            statement.setDouble(6, deliveryCharge);
            statement.setDouble(7, serviceCharge);
            statement.setDouble(8, grandTotal);

            statement.setString(9, "PLACED");

            int rows =
                    statement.executeUpdate();

            if (rows > 0) {

                try (
                        ResultSet resultSet =
                                statement.getGeneratedKeys()
                ) {

                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }


    // =========================================
    // ADD ORDER ITEM
    // =========================================

    public static boolean addOrderItem(
            int orderId,
            int productId,
            String productName,
            int quantity,
            double price,
            double total) {

        String sql =
                "INSERT INTO order_items " +
                        "(order_id, product_id, product_name, " +
                        "quantity, price, total) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, orderId);
            statement.setInt(2, productId);
            statement.setString(3, productName);
            statement.setInt(4, quantity);
            statement.setDouble(5, price);
            statement.setDouble(6, total);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================
    // GET ALL ORDERS
    // =========================================

    public static ArrayList<Map<String, Object>>
    getAllOrders() {

        ArrayList<Map<String, Object>> orders =
                new ArrayList<>();

        String sql =
                "SELECT id, user_email, customer_name, " +
                        "delivery_address, payment_method, subtotal, " +
                        "delivery_charge, service_charge, grand_total, " +
                        "status, created_at " +
                        "FROM orders ORDER BY id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                orders.add(
                        createOrderMap(resultSet)
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return orders;
    }


    // =========================================
    // GET USER ORDERS
    // =========================================

    public static ArrayList<Map<String, Object>>
    getOrdersByUser(String email) {

        ArrayList<Map<String, Object>> orders =
                new ArrayList<>();

        String sql =
                "SELECT id, user_email, customer_name, " +
                        "delivery_address, payment_method, subtotal, " +
                        "delivery_charge, service_charge, grand_total, " +
                        "status, created_at " +
                        "FROM orders " +
                        "WHERE LOWER(TRIM(user_email)) = " +
                        "LOWER(TRIM(?)) " +
                        "ORDER BY id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, email);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    orders.add(
                            createOrderMap(resultSet)
                    );
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return orders;
    }


    // =========================================
    // GET INCOMING ORDERS
    // =========================================

    public static ArrayList<Map<String, Object>>
    getIncomingOrders() {

        ArrayList<Map<String, Object>> orders =
                new ArrayList<>();

        String sql =
                "SELECT id, user_email, customer_name, " +
                        "delivery_address, payment_method, subtotal, " +
                        "delivery_charge, service_charge, grand_total, " +
                        "status, created_at " +
                        "FROM orders " +
                        "WHERE status IN " +
                        "('PLACED', 'PENDING', 'CONFIRMED', 'SHIPPED') " +
                        "ORDER BY id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                orders.add(
                        createOrderMap(resultSet)
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return orders;
    }


    // =========================================
    // GET ORDER ITEMS
    // =========================================

    public static ArrayList<Map<String, Object>>
    getOrderItems(int orderId) {

        ArrayList<Map<String, Object>> items =
                new ArrayList<>();

        String sql =
                "SELECT id, order_id, product_id, " +
                        "product_name, quantity, price, total " +
                        "FROM order_items " +
                        "WHERE order_id = ? " +
                        "ORDER BY id";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, orderId);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                while (resultSet.next()) {

                    Map<String, Object> item =
                            new HashMap<>();

                    item.put(
                            "id",
                            resultSet.getInt("id")
                    );

                    item.put(
                            "order_id",
                            resultSet.getInt("order_id")
                    );

                    item.put(
                            "product_id",
                            resultSet.getInt("product_id")
                    );

                    item.put(
                            "product_name",
                            resultSet.getString(
                                    "product_name"
                            )
                    );

                    item.put(
                            "quantity",
                            resultSet.getInt(
                                    "quantity"
                            )
                    );

                    item.put(
                            "price",
                            resultSet.getDouble(
                                    "price"
                            )
                    );

                    item.put(
                            "total",
                            resultSet.getDouble(
                                    "total"
                            )
                    );

                    items.add(item);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return items;
    }


    // =========================================
    // UPDATE ORDER STATUS
    // =========================================

    public static boolean updateOrderStatus(
            int orderId,
            String status) {

        if (
                orderId <= 0 ||
                        status == null ||
                        status.trim().isEmpty()
        ) {

            return false;
        }


        String sql =
                "UPDATE orders SET status = ? WHERE id = ?";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    status.trim().toUpperCase()
            );

            statement.setInt(
                    2,
                    orderId
            );

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================================
    // CUSTOMER CANCEL ORDER
    // =========================================

    public static Map<String, Object>
    cancelOrder(
            int orderId,
            String customerEmail) {

        Map<String, Object> result =
                new HashMap<>();

        Connection connection = null;

        try {

            if (
                    orderId <= 0 ||
                            customerEmail == null ||
                            customerEmail.trim().isEmpty()
            ) {

                result.put(
                        "success",
                        false
                );

                result.put(
                        "message",
                        "Invalid order information."
                );

                return result;
            }


            connection =
                    DatabaseConnection.getConnection();


            if (connection == null) {

                throw new Exception(
                        "Database connection failed."
                );
            }


            connection.setAutoCommit(false);


            // ---------------------------------
            // CHECK ORDER
            // ---------------------------------

            String checkSql =
                    "SELECT user_email, status " +
                            "FROM orders " +
                            "WHERE id = ? " +
                            "FOR UPDATE";


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    checkSql
                            )
            ) {

                statement.setInt(
                        1,
                        orderId
                );


                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        connection.rollback();

                        result.put(
                                "success",
                                false
                        );

                        result.put(
                                "message",
                                "Order not found."
                        );

                        return result;
                    }


                    String orderEmail =
                            resultSet.getString(
                                    "user_email"
                            );


                    String currentStatus =
                            resultSet.getString(
                                    "status"
                            );


                    // OWNER CHECK

                    if (
                            orderEmail == null ||
                                    !orderEmail.trim()
                                            .equalsIgnoreCase(
                                                    customerEmail.trim()
                                            )
                    ) {

                        connection.rollback();

                        result.put(
                                "success",
                                false
                        );

                        result.put(
                                "message",
                                "You can cancel only your own order."
                        );

                        return result;
                    }


                    // CANCEL ONLY BEFORE SHIPPING

                    if (
                            !"PLACED".equalsIgnoreCase(
                                    currentStatus
                            ) &&
                                    !"PENDING".equalsIgnoreCase(
                                            currentStatus
                                    ) &&
                                    !"CONFIRMED".equalsIgnoreCase(
                                            currentStatus
                                    )
                    ) {

                        connection.rollback();

                        result.put(
                                "success",
                                false
                        );

                        result.put(
                                "message",
                                "This order can no longer be cancelled."
                        );

                        return result;
                    }
                }
            }


            // ---------------------------------
            // RESTORE STOCK
            // ---------------------------------

            String itemSql =
                    "SELECT product_id, quantity " +
                            "FROM order_items " +
                            "WHERE order_id = ?";


            try (
                    PreparedStatement itemStatement =
                            connection.prepareStatement(
                                    itemSql
                            )
            ) {

                itemStatement.setInt(
                        1,
                        orderId
                );


                try (
                        ResultSet resultSet =
                                itemStatement.executeQuery()
                ) {

                    while (resultSet.next()) {

                        int productId =
                                resultSet.getInt(
                                        "product_id"
                                );


                        int quantity =
                                resultSet.getInt(
                                        "quantity"
                                );


                        String stockSql =
                                "UPDATE products " +
                                        "SET stock = stock + ? " +
                                        "WHERE id = ?";


                        try (
                                PreparedStatement stockStatement =
                                        connection.prepareStatement(
                                                stockSql
                                        )
                        ) {

                            stockStatement.setInt(
                                    1,
                                    quantity
                            );

                            stockStatement.setInt(
                                    2,
                                    productId
                            );

                            stockStatement.executeUpdate();
                        }
                    }
                }
            }


            // ---------------------------------
            // UPDATE STATUS
            // ---------------------------------

            String updateSql =
                    "UPDATE orders " +
                            "SET status = 'CANCELLED' " +
                            "WHERE id = ?";


            try (
                    PreparedStatement updateStatement =
                            connection.prepareStatement(
                                    updateSql
                            )
            ) {

                updateStatement.setInt(
                        1,
                        orderId
                );


                int updated =
                        updateStatement.executeUpdate();


                if (updated <= 0) {

                    connection.rollback();

                    result.put(
                            "success",
                            false
                    );

                    result.put(
                            "message",
                            "Failed to cancel order."
                    );

                    return result;
                }
            }


            connection.commit();


            result.put(
                    "success",
                    true
            );

            result.put(
                    "message",
                    "Order cancelled successfully."
            );


            return result;


        } catch (Exception e) {

            e.printStackTrace();


            try {

                if (connection != null) {
                    connection.rollback();
                }

            } catch (Exception ignored) {
            }


            result.put(
                    "success",
                    false
            );

            result.put(
                    "message",
                    "Unable to cancel order."
            );


            return result;


        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception ignored) {
            }
        }
    }


    // =========================================
    // CUSTOMER RETURN REQUEST
    // =========================================

    public static Map<String, Object>
    requestReturn(
            int orderId,
            String customerEmail) {

        Map<String, Object> result =
                new HashMap<>();


        Connection connection = null;


        try {

            // ---------------------------------
            // VALIDATION
            // ---------------------------------

            if (
                    orderId <= 0 ||
                            customerEmail == null ||
                            customerEmail.trim().isEmpty()
            ) {

                result.put(
                        "success",
                        false
                );

                result.put(
                        "message",
                        "Invalid order information."
                );

                return result;
            }


            connection =
                    DatabaseConnection.getConnection();


            if (connection == null) {

                throw new Exception(
                        "Database connection failed."
                );
            }


            // ---------------------------------
            // CHECK ORDER OWNER + STATUS
            // ---------------------------------

            String checkSql =
                    "SELECT user_email, status " +
                            "FROM orders " +
                            "WHERE id = ?";


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    checkSql
                            )
            ) {

                statement.setInt(
                        1,
                        orderId
                );


                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {

                    if (!resultSet.next()) {

                        result.put(
                                "success",
                                false
                        );

                        result.put(
                                "message",
                                "Order not found."
                        );

                        return result;
                    }


                    String orderEmail =
                            resultSet.getString(
                                    "user_email"
                            );


                    String currentStatus =
                            resultSet.getString(
                                    "status"
                            );


                    // OWNER CHECK

                    if (
                            orderEmail == null ||
                                    !orderEmail.trim()
                                            .equalsIgnoreCase(
                                                    customerEmail.trim()
                                            )
                    ) {

                        result.put(
                                "success",
                                false
                        );

                        result.put(
                                "message",
                                "You can return only your own order."
                        );

                        return result;
                    }


                    // RETURN ONLY AFTER DELIVERY

                    if (
                            !"DELIVERED".equalsIgnoreCase(
                                    currentStatus
                            )
                    ) {

                        result.put(
                                "success",
                                false
                        );

                        result.put(
                                "message",
                                "Return is available after delivery."
                        );

                        return result;
                    }
                }
            }


            // ---------------------------------
            // UPDATE RETURN STATUS
            // ---------------------------------

            String updateSql =
                    "UPDATE orders " +
                            "SET status = 'RETURN_REQUESTED' " +
                            "WHERE id = ? " +
                            "AND LOWER(TRIM(user_email)) = " +
                            "LOWER(TRIM(?)) " +
                            "AND status = 'DELIVERED'";


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(
                                    updateSql
                            )
            ) {

                statement.setInt(
                        1,
                        orderId
                );


                statement.setString(
                        2,
                        customerEmail.trim()
                );


                int updated =
                        statement.executeUpdate();


                if (updated > 0) {

                    result.put(
                            "success",
                            true
                    );

                    result.put(
                            "message",
                            "Return request submitted successfully."
                    );

                } else {

                    result.put(
                            "success",
                            false
                    );

                    result.put(
                            "message",
                            "Return request could not be submitted."
                    );
                }
            }


            return result;


        } catch (Exception e) {

            e.printStackTrace();


            result.put(
                    "success",
                    false
            );

            result.put(
                    "message",
                    "Unable to submit return request."
            );


            return result;


        } finally {

            try {

                if (connection != null) {
                    connection.close();
                }

            } catch (Exception ignored) {
            }
        }
    }


    // =========================================
    // ORDER MAP
    // =========================================

    private static Map<String, Object>
    createOrderMap(
            ResultSet resultSet)
            throws Exception {

        Map<String, Object> order =
                new HashMap<>();


        order.put(
                "id",
                resultSet.getInt("id")
        );


        order.put(
                "order_id",
                resultSet.getInt("id")
        );


        order.put(
                "user_email",
                resultSet.getString(
                        "user_email"
                )
        );


        order.put(
                "customer_name",
                resultSet.getString(
                        "customer_name"
                )
        );


        order.put(
                "delivery_address",
                resultSet.getString(
                        "delivery_address"
                )
        );


        order.put(
                "payment_method",
                resultSet.getString(
                        "payment_method"
                )
        );


        order.put(
                "subtotal",
                resultSet.getDouble(
                        "subtotal"
                )
        );


        order.put(
                "delivery_charge",
                resultSet.getDouble(
                        "delivery_charge"
                )
        );


        order.put(
                "service_charge",
                resultSet.getDouble(
                        "service_charge"
                )
        );


        order.put(
                "grand_total",
                resultSet.getDouble(
                        "grand_total"
                )
        );


        order.put(
                "status",
                resultSet.getString(
                        "status"
                )
        );


        order.put(
                "created_at",
                resultSet.getTimestamp(
                        "created_at"
                )
        );


        return order;
    }
}

