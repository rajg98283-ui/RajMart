package com.rajmart;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {


    private static final String URL =
            "jdbc:mysql://localhost:3306/rajmart";

    private static final String USER = "root";

    public static Connection getConnection() {

        try {

            String password =
                    System.getenv("RAJMART_DB_PASSWORD");

            System.out.println("========== DB TEST ==========");

            System.out.println(
                    "DATABASE URL = " + URL
            );

            System.out.println(
                    "DATABASE USER = " + USER
            );

            System.out.println(
                    "PASSWORD SET: " +
                            (password != null && !password.isBlank())
            );

            if (password == null || password.isBlank()) {

                System.out.println(
                        "ERROR: RAJMART_DB_PASSWORD is missing."
                );

                return null;
            }

            Connection connection =
                    DriverManager.getConnection(
                            URL,
                            USER,
                            password
                    );

            System.out.println(
                    "DATABASE CONNECTION SUCCESS!"
            );

            return connection;

        } catch (Exception e) {

            System.out.println(
                    "DATABASE CONNECTION ERROR!"
            );

            e.printStackTrace();

            return null;
        }
    }


}
