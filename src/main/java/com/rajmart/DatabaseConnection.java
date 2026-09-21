package com.rajmart;

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

            if (databaseUrl.startsWith("postgresql://")) {
                databaseUrl = databaseUrl.replaceFirst(
                        "^postgresql://",
                        "jdbc:postgresql://"
                );
            }

            System.out.println("Connecting to Render PostgreSQL...");

            Class.forName("org.postgresql.Driver");

            Connection connection =
                    DriverManager.getConnection(databaseUrl);

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