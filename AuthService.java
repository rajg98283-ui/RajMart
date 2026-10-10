package com.raj.rajmart.service;

import com.raj.rajmart.dao.UserDAO;
import com.raj.rajmart.model.User;
import com.raj.rajmart.util.PasswordUtil;

import java.util.Optional;

/**
 * Authentication and registration business logic.
 */
public class AuthService {

    private final UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public Optional<User> login(String email, String password) {
        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            return Optional.empty();
        }
        Optional<User> userOpt = userDAO.findByEmail(email.trim().toLowerCase());
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        User user = userOpt.get();
        if (PasswordUtil.verify(password, user.getPasswordHash())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public long register(String name, String email, String password, User.Role role) {
        if (name == null || name.isBlank() || email == null || email.isBlank()
                || password == null || password.length() < 6) {
            throw new IllegalArgumentException("Invalid registration data");
        }
        String normalizedEmail = email.trim().toLowerCase();
        if (userDAO.emailExists(normalizedEmail)) {
            throw new IllegalArgumentException("Email already registered");
        }
        if (role == null || role == User.Role.ADMIN) {
            role = User.Role.BUYER; // prevent self-signup as admin
        }
        User user = new User();
        user.setName(name.trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setRole(role);
        return userDAO.create(user);
    }
}
