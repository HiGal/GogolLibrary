package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Book;
import com.project.glib.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDaoImplementation implements DocumentDao<Book> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = Document.BOOK;
    private static final String ADD_BOOK = TYPE + ADD;
    private static final String UPDATE_BOOK = TYPE + UPDATE;
    private static final String REMOVE_BOOK = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImplementation(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Add new item of Book in library
     *
     * @param book new Book
     */
    @Override
    public void add(Book book) {
        bookRepository.saveAndFlush(book);
        logger.info(ADD_BOOK + book);
    }

    /**
     * Update existed Book or create if it not exist
     *
     * @param book - updated Book
     */
    @Override
    public void update(Book book) {
        bookRepository.saveAndFlush(book);
        logger.info(UPDATE_BOOK + book);
    }

    /**
     * Remove Book from library
     *
     * @param bookId id of Book
     */
    @Override
    public void remove(long bookId) {
        bookRepository.delete(bookId);
        logger.info(REMOVE_BOOK + bookId);
    }

    @Override
    public Book isAlreadyExist(Book book) {
        return bookRepository.findAll().stream()
                .filter(b -> b.getTitle().equals(book.getTitle()) &&
                        b.getAuthor().equals(book.getAuthor()) &&
                        b.getPublisher().equals(book.getPublisher()) &&
                        b.getEdition().equals(book.getEdition()) &&
                        b.getYear() == book.getYear() &&
                        b.getNote().equals(book.getNote()))
                .findFirst().get();
    }

    @Override
    public Book getById(long bookId) {
        return bookRepository.findOne(bookId);
    }

    /**
     * get list of all books
     *
     * @return list of books
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Book> getList() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            logger.info(LIST + book);
        }

        return books;
    }

    @Override
    public long getId(Book book) {
        return isAlreadyExist(book).getId();
    }
}
