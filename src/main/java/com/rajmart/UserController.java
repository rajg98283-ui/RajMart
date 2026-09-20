package com.rajmart;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    // =========================
    // REGISTER
    // =========================
    @PostMapping("/register")
    public Map<String, Object> register(
            @RequestBody UserRequest request) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            if (request.name == null ||
                    request.name.trim().isEmpty()) {

                response.put("success", false);
                response.put(
                        "message",
                        "Name is required."
                );

                return response;
            }

            if (request.email == null ||
                    request.email.trim().isEmpty()) {

                response.put("success", false);
                response.put(
                        "message",
                        "Email is required."
                );

                return response;
            }

            if (request.password == null ||
                    request.password.trim().isEmpty()) {

                response.put("success", false);
                response.put(
                        "message",
                        "Password is required."
                );

                return response;
            }

            String role = request.role;

            if (role == null ||
                    role.trim().isEmpty()) {

                role = "BUYER";
            }

            role = role.trim().toUpperCase();

            if (role.equals("CUSTOMER")) {
                role = "BUYER";
            }

            boolean registered =
                    UserDAO.registerUser(
                            request.name.trim(),
                            request.email.trim(),
                            request.password,
                            role
                    );

            if (registered) {

                response.put(
                        "success",
                        true
                );

                response.put(
                        "message",
                        "Registration successful."
                );

            } else {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Registration failed."
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            response.put(
                    "success",
                    false
            );

            response.put(
                    "message",
                    "Server error during registration."
            );
        }

        return response;
    }


    // =========================
    // LOGIN
    // =========================
    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody UserRequest request) {

        Map<String, Object> response =
                new HashMap<>();

        try {

            // Check email
            if (request.email == null ||
                    request.email.trim().isEmpty()) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Email is required."
                );

                return response;
            }


            // Check password
            if (request.password == null ||
                    request.password.trim().isEmpty()) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Password is required."
                );

                return response;
            }


            // Login through DAO
            Map<String, Object> user =
                    UserDAO.loginUser(
                            request.email.trim(),
                            request.password
                    );


            // Invalid login
            if (user == null) {

                response.put(
                        "success",
                        false
                );

                response.put(
                        "message",
                        "Invalid email or password."
                );

                return response;
            }


            // Get actual role
            String actualRole =
                    String.valueOf(
                            user.get("role")
                    );


            // CUSTOMER → BUYER
            if ("CUSTOMER".equalsIgnoreCase(
                    actualRole)) {

                actualRole = "BUYER";
            }


            actualRole =
                    actualRole.toUpperCase();


            // Check selected role
            if (request.role != null &&
                    !request.role.trim().isEmpty()) {

                String selectedRole =
                        request.role
                                .trim()
                                .toUpperCase();


                if ("CUSTOMER".equals(
                        selectedRole)) {

                    selectedRole = "BUYER";
                }


                if (!selectedRole.equals(
                        actualRole)) {

                    response.put(
                            "success",
                            false
                    );

                    response.put(
                            "message",
                            "Selected role does not match this account."
                    );

                    return response;
                }
            }


            // LOGIN SUCCESS
            response.put(
                    "success",
                    true
            );

            response.put(
                    "message",
                    "Login successful."
            );

            response.put(
                    "id",
                    user.get("id")
            );

            response.put(
                    "name",
                    user.get("name")
            );

            response.put(
                    "email",
                    user.get("email")
            );

            response.put(
                    "role",
                    actualRole
            );


        } catch (Exception e) {

            e.printStackTrace();

            response.put(
                    "success",
                    false
            );

            response.put(
                    "message",
                    "Server error during login."
            );
        }

        return response;
    }


    // =========================
    // GET ALL USERS
    // =========================
    @GetMapping
    public ArrayList<Map<String, Object>> getAllUsers() {

        return UserDAO.getAllUsers();
    }


    // =========================
    // DELETE USER
    // =========================
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteUser(
            @PathVariable int id) {

        Map<String, Object> response =
                new HashMap<>();

        boolean deleted =
                UserDAO.deleteUser(id);

        response.put(
                "success",
                deleted
        );

        if (deleted) {

            response.put(
                    "message",
                    "User deleted successfully."
            );

        } else {

            response.put(
                    "message",
                    "User deletion failed."
            );
        }

        return response;
    }


    // =========================
    // REQUEST CLASS
    // =========================
    public static class UserRequest {

        public String name;

        public String email;

        public String password;

        public String role;
    }
}