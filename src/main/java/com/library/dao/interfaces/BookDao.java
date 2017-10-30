package com.library.dao.interfaces;

import com.library.model.Book;
import java.util.List;

public interface BookDao {

    Book getById(Long bookId);
    boolean update(Book book);
    boolean delete(Long bookId);
    boolean create(Book book);
    List<Book> getAll();
    List<Book> getPartition(Integer currentQuantity, String column, String sort);
    Integer getBooksQuantity();
    Book getByIsbn(String isbn);
}
