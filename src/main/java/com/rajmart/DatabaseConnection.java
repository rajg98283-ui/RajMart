package com.rajmart;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    public static Connection getConnection() {

        try {

            String host = System.getenv("MYSQLHOST");
            String port = System.getenv("MYSQLPORT");
            String database = System.getenv("MYSQLDATABASE");
            String user = System.getenv("MYSQLUSER");
            String password = System.getenv("MYSQLPASSWORD");

            String url =
                    "jdbc:mysql://" + host + ":" + port + "/" + database
                            + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

            System.out.println("========== DB TEST ==========");
            System.out.println("DATABASE HOST = " + host);
            System.out.println("DATABASE = " + database);
            System.out.println("DATABASE USER = " + user);
            System.out.println(
                    "PASSWORD SET: " +
                            (password != null && !password.isBlank())
            );

            if (host == null || port == null ||
                    database == null || user == null ||
                    password == null || password.isBlank()) {

                System.out.println("ERROR: Railway MySQL variables are missing.");
                return null;
            }

            Connection connection =
                    DriverManager.getConnection(
                            url,
                            user,
                            password
                    );

            System.out.println("DATABASE CONNECTION SUCCESS!");

            return connection;

        } catch (Exception e) {

            System.out.println("DATABASE CONNECTION ERROR!");
            e.printStackTrace();

            return null;
        }
    }
}