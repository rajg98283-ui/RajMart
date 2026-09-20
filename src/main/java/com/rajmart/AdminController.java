package com.rajmart;

import org.springframework.web.bind.annotation.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        Map<String, Object> stats = new HashMap<>();

        String usersSql =
                "SELECT COUNT(*) FROM users";

        String buyersSql =
                "SELECT COUNT(*) FROM users WHERE role = 'BUYER'";

        String sellersSql =
                "SELECT COUNT(*) FROM users WHERE role = 'SELLER'";

        String productsSql =
                "SELECT COUNT(*) FROM products";

        String ordersSql =
                "SELECT COUNT(*) FROM orders";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            stats.put("totalMembers",
                    getCount(connection, usersSql));

            stats.put("buyers",
                    getCount(connection, buyersSql));

            stats.put("sellers",
                    getCount(connection, sellersSql));

            stats.put("products",
                    getCount(connection, productsSql));

            stats.put("orders",
                    getCount(connection, ordersSql));

            stats.put("success", true);

        } catch (Exception e) {

            e.printStackTrace();

            stats.put("success", false);
            stats.put("message", "Failed to load admin statistics.");
        }

        return stats;
    }

    private int getCount(
            Connection connection,
            String sql) throws Exception {

        try (
            PreparedStatement statement =
                    connection.prepareStatement(sql);

            ResultSet resultSet =
                    statement.executeQuery()
        ) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }

        return 0;
    }
}
