package com.rajmart;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {

    public static Connection getConnection() {

        try {

            String databaseUrl = System.getenv("DATABASE_URL");

            System.out.println("========== RENDER DATABASE TEST ==========");

            System.out.println(
                    "DATABASE_URL SET: " +
                    (databaseUrl != null && !databaseUrl.isBlank())
            );

            if (databaseUrl == null || databaseUrl.isBlank()) {

                System.out.println("ERROR: DATABASE_URL is missing!");

                return null;
            }

            /*
             * Render DATABASE_URL format:
             *
             * postgresql://username:password@hostname:5432/database
             *
             * Password may contain special characters,
             * so we parse the URL manually.
             */

            String url = databaseUrl;

            if (url.startsWith("postgresql://")) {

                url = url.substring("postgresql://".length());
            }

            int atIndex = url.lastIndexOf('@');

            if (atIndex == -1) {

                throw new Exception(
                        "Invalid DATABASE_URL: @ not found"
                );
            }

            String userInfo = url.substring(0, atIndex);

            String hostAndDatabase = url.substring(atIndex + 1);

            int colonIndex = userInfo.indexOf(':');

            if (colonIndex == -1) {

                throw new Exception(
                        "Invalid DATABASE_URL: username/password separator not found"
                );
            }

            String username =
                    userInfo.substring(0, colonIndex);

            String password =
                    userInfo.substring(colonIndex + 1);

            String encodedUsername =
                    URLEncoder.encode(
                            username,
                            StandardCharsets.UTF_8
                    );

            String encodedPassword =
                    URLEncoder.encode(
                            password,
                            StandardCharsets.UTF_8
                    );

            String jdbcUrl =
                    "jdbc:postgresql://" +
                    hostAndDatabase +
                    (hostAndDatabase.contains("?") ? "&" : "?") +
                    "user=" +
                    encodedUsername +
                    "&password=" +
                    encodedPassword;

            System.out.println(
                    "Connecting to Render PostgreSQL..."
            );

            /*
             * Load PostgreSQL JDBC Driver
             */

            Class.forName(
                    "org.postgresql.Driver"
            );

            /*
             * Connect to PostgreSQL
             */

            Connection connection =
                    DriverManager.getConnection(
                            jdbcUrl
                    );

            System.out.println(
                    "DATABASE CONNECTION SUCCESS!"
            );


            // =====================================================
            // USERS TABLE
            // =====================================================

            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(150) UNIQUE NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        role VARCHAR(20) NOT NULL
                    )
                    """;

            Statement usersStatement =
                    connection.createStatement();

            usersStatement.executeUpdate(
                    createUsersTable
            );

            usersStatement.close();

            System.out.println(
                    "USERS TABLE READY!"
            );


            // =====================================================
            // PRODUCTS TABLE
            // =====================================================

            String createProductsTable = """
                    CREATE TABLE IF NOT EXISTS products (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(200) NOT NULL,
                        price NUMERIC(12,2) NOT NULL,
                        stock INTEGER NOT NULL,
                        image_url VARCHAR(500)
                    )
                    """;

            Statement productsTableStatement =
                    connection.createStatement();

            productsTableStatement.executeUpdate(
                    createProductsTable
            );

            productsTableStatement.close();

            System.out.println(
                    "PRODUCTS TABLE READY!"
            );


            // =====================================================
            // CHECK WHETHER PRODUCTS ALREADY EXIST
            // =====================================================

            String checkProducts =
                    "SELECT COUNT(*) FROM products";

            Statement checkStatement =
                    connection.createStatement();

            ResultSet productResult =
                    checkStatement.executeQuery(
                            checkProducts
                    );

            productResult.next();

            int productCount =
                    productResult.getInt(1);

            productResult.close();

            checkStatement.close();


            // =====================================================
            // INSERT 7 PRODUCTS ONLY IF EMPTY
            // =====================================================

            if (productCount == 0) {

                String insertProducts = """
                        INSERT INTO products
                        (name, price, stock, image_url)
                        VALUES

                        (
                            'Tv',
                            35000.00,
                            4,
                            '/uploads/products/62bfdc8e-5921-4d3b-82be-7f1ea5aaa61b.jpg'
                        ),

                        (
                            'HP Laptop 15',
                            55000.00,
                            30,
                            '/uploads/products/d17d5e21-87d7-416f-8295-a77fe32eab26.webp'
                        ),

                        (
                            'Dell Inspiron 15',
                            65000.00,
                            40,
                            '/uploads/products/f11ec074-4ffa-4901-8bd9-c38529573f0d.jpeg'
                        ),

                        (
                            'Lenovo ideapad Slim 3',
                            75999.00,
                            55,
                            '/uploads/products/ee734e32-a2ca-4b61-accf-d01571a8f798.webp'
                        ),

                        (
                            'ASUS VivoBook 15',
                            59999.00,
                            45,
                            '/uploads/products/6aab5db7-b669-4688-b3a1-0e6bee6ef406.webp'
                        ),

                        (
                            'HP Wireless Mouse',
                            699.00,
                            70,
                            '/uploads/products/76245bcc-8b07-4e2b-9fe2-1a9fb80bccff.jpeg'
                        ),

                        (
                            'Sony Bluetooth Speaker',
                            2999.00,
                            30,
                            '/uploads/products/76117d3a-2c14-4b08-af93-f14ec73ca292.jpeg'
                        )
                        """;

                Statement insertStatement =
                        connection.createStatement();

                insertStatement.executeUpdate(
                        insertProducts
                );

                insertStatement.close();

                System.out.println(
                        "7 PRODUCTS INSERTED!"
                );

            } else {

                System.out.println(
                        "PRODUCTS ALREADY EXIST: " +
                        productCount
                );
            }


            // =====================================================
            // RETURN CONNECTION
            // =====================================================

            return connection;


        } catch (Exception e) {

            System.out.println(
                    "========== DATABASE CONNECTION ERROR =========="
            );

            System.out.println(
                    "ERROR TYPE: " +
                    e.getClass().getName()
            );

            System.out.println(
                    "ERROR MESSAGE: " +
                    e.getMessage()
            );

            e.printStackTrace();

            return null;
        }
    }
}