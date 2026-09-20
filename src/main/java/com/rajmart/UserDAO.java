package com.rajmart;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserDAO {

    // =========================
    // REGISTER USER
    // =========================
    public static boolean registerUser(
            String name,
            String email,
            String password,
            String role) {

        String sql =
                "INSERT INTO users " +
                        "(name, email, password_hash, role) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {

            if (connection == null) {

                System.out.println(
                        "REGISTER FAILED: Database connection is null."
                );

                return false;
            }


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {

                statement.setString(
                        1,
                        name
                );

                statement.setString(
                        2,
                        email.trim()
                );

                statement.setString(
                        3,
                        password
                );

                statement.setString(
                        4,
                        role
                );


                int rows =
                        statement.executeUpdate();


                return rows > 0;
            }


        } catch (Exception e) {

            System.out.println(
                    "REGISTER FAILED!"
            );

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // LOGIN USER
    // =========================
    public static Map<String, Object> loginUser(
            String email,
            String password) {


        /*
         * IMPORTANT:
         *
         * First find the user using email only.
         * Then compare password in Java.
         *
         * This makes debugging much easier than
         * checking email + password directly in SQL.
         */

        String sql =
                "SELECT id, name, email, password_hash, role " +
                        "FROM users " +
                        "WHERE LOWER(TRIM(email)) = LOWER(TRIM(?))";


        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {


            if (connection == null) {

                System.out.println(
                        "LOGIN FAILED: Database connection is null."
                );

                return null;
            }


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {


                statement.setString(
                        1,
                        email.trim()
                );


                try (
                        ResultSet resultSet =
                                statement.executeQuery()
                ) {


                    /*
                     * USER NOT FOUND
                     */

                    if (!resultSet.next()) {

                        System.out.println(
                                "LOGIN FAILED: User not found for email = "
                                        + email
                        );

                        return null;
                    }


                    /*
                     * Read database values
                     */

                    String dbPassword =
                            resultSet.getString(
                                    "password_hash"
                            );


                    String dbRole =
                            resultSet.getString(
                                    "role"
                            );


                    /*
                     * DEBUG INFORMATION
                     *
                     * Password itself is NEVER printed.
                     */

                    System.out.println(
                            "LOGIN EMAIL FOUND: " +
                                    resultSet.getString("email")
                    );

                    System.out.println(
                            "LOGIN ROLE IN DB: " +
                                    dbRole
                    );

                    System.out.println(
                            "PASSWORD PROVIDED: " +
                                    (password != null &&
                                            !password.isEmpty())
                    );

                    System.out.println(
                            "PASSWORD STORED: " +
                                    (dbPassword != null &&
                                            !dbPassword.isEmpty())
                    );


                    /*
                     * PASSWORD CHECK
                     */

                    if (
                            password == null ||
                                    dbPassword == null ||
                                    !dbPassword.equals(password)
                    ) {

                        System.out.println(
                                "LOGIN FAILED: Password does not match."
                        );

                        return null;
                    }


                    /*
                     * LOGIN SUCCESS
                     */

                    Map<String, Object> user =
                            new HashMap<>();


                    user.put(
                            "id",
                            resultSet.getInt("id")
                    );


                    user.put(
                            "name",
                            resultSet.getString("name")
                    );


                    user.put(
                            "email",
                            resultSet.getString("email")
                    );


                    user.put(
                            "role",
                            dbRole
                    );


                    System.out.println(
                            "LOGIN SUCCESS: " +
                                    email
                    );


                    return user;
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "LOGIN DATABASE ERROR!"
            );

            e.printStackTrace();

            return null;
        }
    }


    // =========================
    // GET ALL USERS
    // =========================
    public static ArrayList<Map<String, Object>>
    getAllUsers() {


        ArrayList<Map<String, Object>> users =
                new ArrayList<>();


        String sql =
                "SELECT id, name, email, role " +
                        "FROM users " +
                        "ORDER BY id DESC";


        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {


            if (connection == null) {

                return users;
            }


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(sql);

                    ResultSet resultSet =
                            statement.executeQuery()
            ) {


                while (resultSet.next()) {


                    Map<String, Object> user =
                            new HashMap<>();


                    user.put(
                            "id",
                            resultSet.getInt("id")
                    );


                    user.put(
                            "name",
                            resultSet.getString("name")
                    );


                    user.put(
                            "email",
                            resultSet.getString("email")
                    );


                    user.put(
                            "role",
                            resultSet.getString("role")
                    );


                    users.add(user);
                }
            }


        } catch (Exception e) {

            System.out.println(
                    "GET USERS FAILED!"
            );

            e.printStackTrace();
        }


        return users;
    }


    // =========================
    // DELETE USER
    // =========================
    public static boolean deleteUser(int id) {


        String sql =
                "DELETE FROM users WHERE id = ?";


        try (
                Connection connection =
                        DatabaseConnection.getConnection()
        ) {


            if (connection == null) {

                return false;
            }


            try (
                    PreparedStatement statement =
                            connection.prepareStatement(sql)
            ) {


                statement.setInt(
                        1,
                        id
                );


                int rows =
                        statement.executeUpdate();


                return rows > 0;
            }


        } catch (Exception e) {

            System.out.println(
                    "DELETE USER FAILED!"
            );

            e.printStackTrace();

            return false;
        }
    }
}

