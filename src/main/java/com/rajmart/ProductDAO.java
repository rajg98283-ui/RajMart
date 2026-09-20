package com.rajmart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    public static List<Product> getAllProducts() {

        List<Product> products = new ArrayList<>();

        String sql =
                "SELECT id, name, price, stock, image_url " +
                        "FROM products ORDER BY id";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()
        ) {

            while (resultSet.next()) {

                Product product = new Product();

                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setPrice(resultSet.getDouble("price"));
                product.setQuantity(resultSet.getInt("stock"));
                product.setImage(resultSet.getString("image_url"));

                products.add(product);
            }

        } catch (Exception e) {

            e.printStackTrace();

            throw new RuntimeException(
                    "Failed to load products from database.",
                    e
            );
        }

        return products;
    }


    // ADD PRODUCT + IMAGE
    public static boolean addProduct(
            String name,
            double price,
            int quantity,
            String image) {

        String sql =
                "INSERT INTO products " +
                        "(name, price, stock, image_url) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setString(4, image);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Failed to add product.");
            e.printStackTrace();

            return false;
        }
    }


    // UPDATE PRODUCT + IMAGE
    public static boolean updateProduct(
            int id,
            String name,
            double price,
            int quantity,
            String image) {

        String sql =
                "UPDATE products SET " +
                        "name = ?, " +
                        "price = ?, " +
                        "stock = ?, " +
                        "image_url = ? " +
                        "WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(1, name);
            statement.setDouble(2, price);
            statement.setInt(3, quantity);
            statement.setString(4, image);
            statement.setInt(5, id);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Failed to update product.");
            e.printStackTrace();

            return false;
        }
    }


    public static boolean deleteProduct(int id) {

        String sql =
                "DELETE FROM products WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Failed to delete product.");
            e.printStackTrace();

            return false;
        }
    }


    public static boolean updateQuantity(
            int id,
            int quantity) {

        String sql =
                "UPDATE products SET stock = ? " +
                        "WHERE id = ?";

        try (
                Connection connection = DatabaseConnection.getConnection();
                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, quantity);
            statement.setInt(2, id);

            return statement.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Failed to update quantity.");
            e.printStackTrace();

            return false;
        }
    }
}

