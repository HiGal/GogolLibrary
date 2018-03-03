package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImplementation implements DocumentDao<Book> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImplementation(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void add(Book book) {
        bookRepository.save(book);
        logger.info("Book successfully saved. Book details : " + book);
    }

    @Override
    public void update(Book book) {
        bookRepository.save(book);
        logger.info("Book successfully update. Book details : " + book);
    }

    @Override
    public void remove(long bookId) {
        bookRepository.delete(bookId);
    }

    @Override
    public Book getById(long bookId) {
        return bookRepository.findOne(bookId);
    }

    @Override
    public int getCountById(long bookId) {
        return bookRepository.findOne(bookId).getCount();
    }

    @Override
    public void decrementCountById(long bookId) {
        bookRepository.findOne(bookId).setCount(bookRepository.findOne(bookId).getCount() - 1);
    }

    @Override
    public int getPriceById(long bookId) {
        return bookRepository.findOne(bookId).getPrice();
    }

    public boolean getIsBestseller(long bookId) {
        return bookRepository.findOne(bookId).isBestSeller();
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
    public List<Book> getList() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            logger.info("Book list : " + book);
        }

        return books;
    }
}
