package com.library.dao.mappers;

import com.library.dao.services.UserService;
import com.library.model.Book;
import com.library.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class BookMapper implements RowMapper<Book> {
    @Autowired
    private UserService userService;

    @Override
    public Book mapRow(ResultSet resultSet, int i) throws SQLException {
        Long bookId = resultSet.getLong("book_id");
        String isbn = resultSet.getString("isbn");
        String author = resultSet.getString("author");
        String title = resultSet.getString("title");
        Long takenBy = resultSet.getLong("taken_by");
        User user;
        if (takenBy != null)
            user = userService.getById(takenBy);
        else
            user = null;
        return new Book(bookId, isbn, author, title, user);
    }
}