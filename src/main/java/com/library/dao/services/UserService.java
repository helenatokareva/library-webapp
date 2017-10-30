package com.library.dao.services;

import com.library.dao.interfaces.UserDao;
import com.library.dao.mappers.UserMapper;
import com.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService implements UserDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getById(Long userId) throws DataAccessException {
        User user;
        try {
            user = jdbcTemplate.queryForObject( "select u.* from users u where u.user_id = ?", new UserMapper(), userId);
        }
        catch(EmptyResultDataAccessException ex) {
            user = null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean update(User user) throws DataAccessException {
        int result = jdbcTemplate.update("update users set login = ?, password = ? where user_id = ?",
                new Object[]{user.getLogin().toLowerCase(), user.getPassword(), user.getUserId()});
        return (result != 0) ? true : false;
    }

    @Override
    @Transactional
    public boolean delete(Long userId) throws DataAccessException {
        int result = jdbcTemplate.update("delete from users where user_id = ?", userId);
        return (result != 0) ? true : false;
    }

    @Override
    @Transactional
    public boolean create(User user) throws DataAccessException {
        int result = jdbcTemplate.update("insert into users (login, password) values (?,?)",
                new Object[]{user.getLogin().toLowerCase(), user.getPassword()});
        return (result != 0) ? true : false;
    }

    @Override
    public List<User> getAll() throws DataAccessException {
        List<User> users;
        try {
            users = jdbcTemplate.query("select * from users order by login", new UserMapper());
        }
        catch(EmptyResultDataAccessException ex) {
            users = null;
        }
        return users;
    }

    @Override
    public User getByLogin(String login) throws DataAccessException {
        User user;
        try {
            user = jdbcTemplate.queryForObject( "select u.* from users u where u.login = ?", new UserMapper(), login.toLowerCase());
        }
        catch(EmptyResultDataAccessException ex) {
            user = null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean updateLogin(Long userId, String login) throws DataAccessException {
        int result = jdbcTemplate.update("update users set login = ? where user_id = ?",
                new Object[]{login.toLowerCase(), userId});
        return (result != 0) ? true : false;
    }
}
