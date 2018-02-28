package com.project.glib.dao;

import com.project.glib.model.Book;

import java.util.List;

public interface BookDao {
    public void addBook(Book book);

    public void updateBook(Book book);

    public void removeBook(long bookId);

    public Book getBookById(long bookId);

    public List<Book> listBooks();
}
