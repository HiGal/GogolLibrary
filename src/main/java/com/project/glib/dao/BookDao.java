package com.project.glib.dao;

import com.project.glib.model.Book;

import java.util.List;

public interface BookDao {
    public void addBook(Book book);

    public void updateBook(Book book);

    public void removeBook(int bookId);

    public Book getBookById(int bookId);

    public List<Book> listBooks();
}
