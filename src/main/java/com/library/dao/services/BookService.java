package com.library.dao.services;

import com.library.dao.interfaces.BookDao;
import com.library.dao.mappers.BookMapper;
import com.library.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BookService implements BookDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BookMapper bookMapper;

    @Override
    public Book getById(Long bookId) throws DataAccessException {
        Book book;
        try {
            book = jdbcTemplate.queryForObject( "select b.* from books b where b.book_id = ?", bookMapper, bookId);
        }
        catch(EmptyResultDataAccessException ex) {
            book = null;
        }
        return book;
    }

    @Override
    @Transactional
    public boolean update(Book book) throws DataAccessException {
        Long userId = (book.getTakenBy() != null) ? book.getTakenBy().getUserId() : null;
        int result = jdbcTemplate.update("update books set isbn = ?, author = ?, title = ?, taken_by = ? where book_id = ?",
                new Object[]{book.getIsbn(), book.getAuthor(), book.getTitle(), userId, book.getBookId()});
        return (result != 0) ? true : false;
    }

    @Override
    @Transactional
    public boolean delete(Long bookId) throws DataAccessException {
        int result = jdbcTemplate.update("delete from books where book_id = ?", bookId);
        return (result != 0) ? true : false;
    }

    @Override
    @Transactional
    public boolean create(Book book) throws DataAccessException {
        Long userId = (book.getTakenBy() != null) ? book.getTakenBy().getUserId() : null;
        int result = jdbcTemplate.update("insert into books (isbn, author, title, taken_by) values (?,?,?,?)",
                new Object[]{book.getIsbn(), book.getAuthor(), book.getTitle(), userId});
        return (result != 0) ? true : false;
    }

    @Override
    public List<Book> getAll() throws DataAccessException {
        List<Book> books;
        try {
            books = jdbcTemplate.query("select * from books order by author, title", bookMapper);
        }
        catch(EmptyResultDataAccessException ex) {
            books = null;
        }
        return books;
    }

    @Override
    public List<Book> getPartition(Integer currentQuantity, String column, String sort) throws DataAccessException {
        String sql = "select * from books order by " + column + " " + sort + " limit 5 offset ?";
        List<Book> books;
        try {
            books = jdbcTemplate.query(sql, bookMapper, currentQuantity);
        }
        catch(EmptyResultDataAccessException ex) {
            books = null;
        }
        return books;
    }

    @Override
    public Integer getBooksQuantity() {
        Integer quantity = jdbcTemplate.queryForObject( "select count(book_id) from books", Integer.class);
        return ((quantity != null) ? quantity.intValue() : 0);
    }

    @Override
    public Book getByIsbn(String isbn) throws DataAccessException {
        Book book;
        try {
            book = jdbcTemplate.queryForObject( "select b.* from books b where b.isbn = ?", bookMapper, isbn);
        }
        catch(EmptyResultDataAccessException ex) {
            book = null;
        }
        return book;
    }
}
