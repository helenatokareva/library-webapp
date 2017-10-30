package com.library.model;

import org.springframework.test.util.ReflectionTestUtils;

public class BookBuilder {
    private Book model;

    public BookBuilder() {
        model = new Book();
    }

    public BookBuilder bookId(Long bookId) {
        ReflectionTestUtils.setField(model, "bookId", bookId);
        return this;
    }

    public BookBuilder isbn(String isbn) {
        ReflectionTestUtils.setField(model, "isbn", isbn);
        return this;
    }

    public BookBuilder author(String author) {
        ReflectionTestUtils.setField(model, "author", author);
        return this;
    }

    public BookBuilder title(String title) {
        ReflectionTestUtils.setField(model, "title", title);
        return this;
    }

    public BookBuilder takenBy(User user) {
        ReflectionTestUtils.setField(model, "takenBy", user);
        return this;
    }

    public Book build() {
        return model;
    }
}
