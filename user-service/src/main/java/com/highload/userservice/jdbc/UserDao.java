package com.highload.userservice.jdbc;

import com.highload.feign.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserDao {

    List<User> findAll();

    Optional<User> findUserByUserId(UUID id);

    boolean deleteUserByUserId(UUID id);

    Optional<User> findByUsername(String username);

    User insertUser(User user);
}
