package com.library.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Book {
    private Long bookId;
    private String isbn;
    private String author;
    private String title;
    private User takenBy;

    public Book() {
    }

    public Book(Long bookId, String isbn, String author, String title, User takenBy) {
        this.bookId = bookId;
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.takenBy = takenBy;
    }

    public Book(String isbn, String author, String title, User takenBy) {
        this.isbn = isbn;
        this.author = author;
        this.title = title;
        this.takenBy = takenBy;
    }

    public Long getBookId() {
        return bookId;
    }
    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public User getTakenBy() {
        return takenBy;
    }
    public void setTakenBy(User takenBy) {
        this.takenBy = takenBy;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getBookId())
                .toHashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || !(o instanceof Book))
            return false;
        Book book = (Book) o;
        return new EqualsBuilder()
                .append(getBookId(), book.getBookId())
                .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("ISBN", getIsbn())
                .append("Author", getAuthor())
                .append("Title", getTitle())
                .append("TakenBy", getTakenBy())
                .toString();
    }
}