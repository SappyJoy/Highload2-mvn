package com.highload.userservice.jdbc;

import com.highload.feign.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDaoImpl {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    User findAll() {
        String sql = "SELECT * FROM users JOIN user_roles ON user_roles.user_id = users.id";
        return jdbcTemplate.query(sql)
    }

    Optional<User> findUserByUserId(UUID id);

    void deleteUserByUserId(UUID id);

    Optional<User> findByUsername(String username);
}
