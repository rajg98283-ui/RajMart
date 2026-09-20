package com.rajmart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReviewDAO {

    public static boolean addReview(
            int productId,
            String customerName,
            int rating,
            String reviewText) {

        String sql =
                "INSERT INTO reviews " +
                "(product_id, customer_name, rating, review_text) " +
                "VALUES (?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, productId);
            statement.setString(2, customerName);
            statement.setInt(3, rating);
            statement.setString(4, reviewText);

            statement.executeUpdate();

            return true;

        } catch (Exception e) {
            System.out.println("Failed to add review.");
            e.printStackTrace();
            return false;
        }
    }

    public static ArrayList<Map<String, Object>> getReviews(
            int productId) {

        ArrayList<Map<String, Object>> reviews =
                new ArrayList<>();

        String sql =
                "SELECT id, customer_name, rating, " +
                "review_text, review_date " +
                "FROM reviews " +
                "WHERE product_id = ? " +
                "ORDER BY id DESC";

        try (
                Connection connection =
                        DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, productId);

            try (ResultSet resultSet = statement.executeQuery()) {

                while (resultSet.next()) {

                    Map<String, Object> review =
                            new HashMap<>();

                    review.put("id",
                            resultSet.getInt("id"));

                    review.put("customerName",
                            resultSet.getString("customer_name"));

                    review.put("rating",
                            resultSet.getInt("rating"));

                    review.put("reviewText",
                            resultSet.getString("review_text"));

                    review.put("reviewDate",
                            resultSet.getString("review_date"));

                    reviews.add(review);
                }
            }

        } catch (Exception e) {
            System.out.println("Failed to load reviews.");
            e.printStackTrace();
        }

        return reviews;
    }
}
