package com.rajmart;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    public static Connection getConnection() {

        try {

            String databaseUrl = System.getenv("DATABASE_URL");

            System.out.println("========== RENDER DATABASE TEST ==========");

            System.out.println("DATABASE_URL SET: " +
                    (databaseUrl != null && !databaseUrl.isBlank()));

            if (databaseUrl == null || databaseUrl.isBlank()) {
                System.out.println("ERROR: DATABASE_URL is missing!");
                return null;
            }

            // Remove postgresql://
            String url = databaseUrl;

            if (url.startsWith("postgresql://")) {
                url = url.substring("postgresql://".length());
            }

            /*
             * Render URL format:
             *
             * postgresql://username:password@hostname:5432/database
             *
             * We split it manually because the password may contain
             * special characters such as @.
             */

            int atIndex = url.lastIndexOf('@');

            if (atIndex == -1) {
                throw new Exception("Invalid DATABASE_URL: @ not found");
            }

            String userInfo = url.substring(0, atIndex);
            String hostAndDatabase = url.substring(atIndex + 1);

            int colonIndex = userInfo.indexOf(':');

            if (colonIndex == -1) {
                throw new Exception("Invalid DATABASE_URL: username/password separator not found");
            }

            String username = userInfo.substring(0, colonIndex);
            String password = userInfo.substring(colonIndex + 1);

            String encodedUsername =
                    URLEncoder.encode(username, StandardCharsets.UTF_8);

            String encodedPassword =
                    URLEncoder.encode(password, StandardCharsets.UTF_8);

            String jdbcUrl =
                    "jdbc:postgresql://" +
                    hostAndDatabase +
                    (hostAndDatabase.contains("?") ? "&" : "?") +
                    "user=" + encodedUsername +
                    "&password=" + encodedPassword;

            System.out.println("Connecting to Render PostgreSQL...");

            Class.forName("org.postgresql.Driver");

            Connection connection =
                    DriverManager.getConnection(jdbcUrl);

            System.out.println("DATABASE CONNECTION SUCCESS!");

            // Create users table automatically
            String createUsersTable = """
                    CREATE TABLE IF NOT EXISTS users (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        email VARCHAR(150) UNIQUE NOT NULL,
                        password_hash VARCHAR(255) NOT NULL,
                        role VARCHAR(20) NOT NULL
                    )
                    """;

            Statement statement = connection.createStatement();
            statement.executeUpdate(createUsersTable);
            statement.close();

            System.out.println("USERS TABLE READY!");

            return connection;

        } catch (Exception e) {

            System.out.println("========== DATABASE CONNECTION ERROR ==========");

            System.out.println("ERROR TYPE: " +
                    e.getClass().getName());

            System.out.println("ERROR MESSAGE: " +
                    e.getMessage());

            e.printStackTrace();

            return null;
        }
    }
}