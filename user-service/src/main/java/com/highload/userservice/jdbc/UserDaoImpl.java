package com.highload.userservice.jdbc;

import com.highload.feign.model.Role;
import com.highload.feign.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    private final String SQL_GET_ALL = "SELECT * FROM `USER`";
    private final String SQL_GET_BY_ID = "SELECT * FROM `USER` WHERE user_id = ?";
    private final String SQL_DELETE_USER = "DELETE FROM `USER` WHERE user_id = ?";
    private final String SQL_GET_BY_USERNAME = "SELECT * FROM `USER` WHERE username = ?";
    private final String SQL_INSERT_USER = "INSERT INTO `USER` (user_id, username, password) VALUES (?, ?, ?)";
    private final String SQL_INSERT_ROLE = "INSERT INTO `USER_ROLE` (user_id, role_id) VALUES (?, ?)";
    private final String SQL_GET_ROLES = "SELECT * FROM `USER_ROLE` JOIN `ROLE` ON `ROLE`.role_id = `USER_ROLE`.role_id WHERE user_id = ?";


    @Autowired
    public UserDaoImpl() {
        jdbcTemplate = new JdbcTemplate();
    }

    public class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setUserId(rs.getObject("user_id", java.util.UUID.class));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            List<Role> roles = jdbcTemplate.query(SQL_GET_ROLES, (rs1, rowNum1) -> {
                Role role = new Role();
                role.setRoleId(rs.getObject("role_id", java.util.UUID.class));
                role.setRolename(rs.getString("rolename"));
                return role;
            });
            user.setRoles(roles);
            return user;
        }
    }

    public List<User> findAll() {
        return jdbcTemplate.query(SQL_GET_ALL, new UserMapper());
    }

    public Optional<User> findUserByUserId(UUID id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_BY_ID, new Object[] {id}, new UserMapper()));
    }

    public User insertUser(User user) {
        jdbcTemplate.update(SQL_INSERT_USER, user.getUserId(), user.getUsername(), user.getPassword());
        for (Role role : user.getRoles()) {
            jdbcTemplate.update(SQL_INSERT_ROLE, user.getUserId(), role.getRoleId());
        }
        return user;
    }

    public boolean deleteUserByUserId(UUID id) {
        return jdbcTemplate.update(SQL_DELETE_USER, id) > 0;
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_GET_BY_USERNAME, new Object[] {username}, new UserMapper()));
    }

    public boolean save(User user) {
        return jdbcTemplate.update(SQL_INSERT_USER, user.getUserId(), user.getUsername(), user.getPassword()) > 0;
    }
}
