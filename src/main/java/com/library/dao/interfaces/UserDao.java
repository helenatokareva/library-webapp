package com.library.dao.interfaces;

import com.library.model.User;
import java.util.List;

public interface UserDao {
    User getById(Long userId);
    boolean update(User user);
    boolean delete(Long userId);
    boolean create(User user);
    List<User> getAll();
    User getByLogin(String login);
    boolean updateLogin(Long userId, String login);
}