package com.rajmart;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {

        try {

            // Render PostgreSQL DATABASE_URL
            String databaseUrl = System.getenv("DATABASE_URL");

            System.out.println("========== RENDER DATABASE TEST ==========");

            if (databaseUrl == null || databaseUrl.isBlank()) {

                System.out.println("DATABASE_URL = NOT FOUND");
                System.out.println("ERROR: Render DATABASE_URL is missing.");

                return null;
            }

            System.out.println("DATABASE_URL = FOUND");

            // Convert Render PostgreSQL URL
            // postgresql://...
            //        ↓
            // jdbc:postgresql://...

            if (databaseUrl.startsWith("postgresql://")) {

                databaseUrl = databaseUrl.replaceFirst(
                        "^postgresql://",
                        "jdbc:postgresql://"
                );
            }

            // PostgreSQL connection
            Connection connection =
                    DriverManager.getConnection(databaseUrl);

            System.out.println("DATABASE CONNECTION SUCCESS!");
            System.out.println("==========================================");

            return connection;

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION FAILED!");
            System.out.println("==========================================");

            e.printStackTrace();

            return null;
        }
    }
}