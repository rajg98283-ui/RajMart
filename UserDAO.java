package com.raj.rajmart.dao;

import com.raj.rajmart.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data access interface for User entity.
 */
public interface UserDAO {

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    List<User> findAll();

    long create(User user);

    boolean update(User user);

    boolean delete(long id);

    boolean emailExists(String email);
}
