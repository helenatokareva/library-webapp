package com.library.dao.mappers;

import com.library.model.User;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        Long userId = resultSet.getLong("user_id");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        return new User(userId, login, password);
    }
}