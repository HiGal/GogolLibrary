package com.project.glib.dao;

import com.project.glib.dao.BookDao;
import com.project.glib.dao.BookRepository;
import com.project.glib.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookDaoImplementation implements BookDao {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImplementation(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void addBook(Book book) {
        bookRepository.save(book);
        logger.info("Book successfully saved. Book details : " + book);
    }

    @Override
    public void updateBook(Book book) {
        bookRepository.save(book);
        logger.info("Book successfully update. Book details : " + book);
    }

    @Override
    public void removeBook(long bookId) {
        bookRepository.delete(bookId);
    }

    @Override
    public Book getBookById(long bookId) {
      return bookRepository.findOne(bookId);
    }

    /*
    public List<Book> getBooksByAuthor(String author){
        return bookRepository.findAll().stream()
                .filter(book->book.getBookAuthor().equals(author))
                .collect(Collectors.toCollection(HashSet::new));
        return bookRepository.findAll().stream()
                .filter(book->book.getBookAuthor().equals("Kiplings"))
                .anyMatch(book->book.isBestSeller());
    }
               */

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> listBooks() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books){
            logger.info("Book list : " + book);
        }

        return books;
    }
}
