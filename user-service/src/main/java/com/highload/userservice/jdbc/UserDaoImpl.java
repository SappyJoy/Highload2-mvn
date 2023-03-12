package com.highload.userservice.jdbc;

import com.highload.feign.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL = "SELECT * FROM `USER` JOIN `USER_ROLE` ON `USER_ROLE`.user_id = `USER`.user_id";
    private final String SQL_GET_BY_ID = "SELECT * FROM `USER` JOIN `USER_ROLE` ON `USER_ROLE`.user_id = `USER`.user_id WHERE user_id = ?";
    private final String SQL_DELETE_USER = "DELETE FROM `USER` WHERE user_id = ?";
    private final String SQL_GET_BY_USERNAME = "SELECT * FROM `USER` JOIN `USER_ROLE` ON `USER_ROLE`.user_id = `USER`.user_id WHERE username = ?";
    private final String SQL_INSERT_USER = "INSERT INTO `USER`(user_id, username, password)";


    @Autowired
    public UserDaoImpl() {
        jdbcTemplate = new JdbcTemplate();
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getObject("user_id", java.util.UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
//        user.setRoles(rs.getString("roles"));
            return user;
        });
    }

    public Optional<User> findUserByUserId(UUID id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] {id}, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getObject("user_id", java.util.UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
//        user.setRoles(rs.getString("roles"));
            return user;
        }));
    }

    public boolean deleteUserByUserId(UUID id) {
        return jdbcTemplate.update(SQL_DELETE_USER, id) > 0;
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_BY_USERNAME, new Object[] {username}, (rs, rowNum) -> {
            User user = new User();
            user.setUserId(rs.getObject("user_id", java.util.UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
//        user.setRoles(rs.getString("roles"));
            return user;
        }));
    }

    public boolean save(User user) {
        return jdbcTemplate.update(SQL_INSERT_USER, user.getUserId(), user.getUsername(), user.getPassword()) > 0;
    }
}
