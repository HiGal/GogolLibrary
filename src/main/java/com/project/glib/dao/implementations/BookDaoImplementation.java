package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookDaoImplementation implements DocumentDao<Book> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookRepository bookRepository;
    private final DocumentPhysicalDaoImplementation documentPhysicalDaoImplementation;

    @Autowired
    public BookDaoImplementation(BookRepository bookRepository, DocumentPhysicalDaoImplementation documentPhysicalDaoImplementation) {
        this.bookRepository = bookRepository;
        this.documentPhysicalDaoImplementation = documentPhysicalDaoImplementation;
    }

    /**
     * Add new item of Book in library
     *
     * @param book new Book
     * @throws Exception
     */
    @Override
    public void add(Book book) throws Exception {
        try {
            if (true){
                bookRepository.save(book);
                logger.info("Book successfully saved. Book details : " + book);
            }else {
                throw new Exception("Can't add this book, some parameters are wrong");
            }
        } catch (Exception e) {
            logger.info("Try to add book with wrong parameters. New book information : " + book);
            throw new Exception("Can't add this book, some parameters are wrong");
        }
    }

    /**
     * Update existed Book or create if it not exist
     *
     * @param book - updated Book
     * @throws Exception
     */
    @Override
    public void update(Book book) throws Exception {
        try {
            bookRepository.saveAndFlush(book);
            logger.info("Book successfully update. Book details : " + book);
        } catch (Exception e) {
            logger.info("Try to update this book, book don't exist or some new book parameters are wrong. " +
                    "Update book information : " + book);
            throw new Exception("Can't update this book, book don't exist or some new book parameters are wrong");
        }
    }

    /**
     * Remove Book from library
     * @param bookId id of Book
     * @throws Exception
     */
    @Override
    public void remove(long bookId) throws Exception {
        try {
            logger.info("Try to delete book with book id = " + bookId);
            documentPhysicalDaoImplementation.removeAllByDocId(bookId);
            bookRepository.delete(bookId);
        } catch (Exception e) {
            logger.info("Try to delete book with wrong book id = " + bookId);
            throw new Exception("Delete this book not available, book don't exist");
        }
    }

    /**
     *
     * @param bookId
     * @return
     * @throws Exception
     */
    @Override
    public Book getById(long bookId) throws Exception {
        try {
            logger.info("Try to get book with book id = " + bookId);
            return bookRepository.findOne(bookId);
        } catch (Exception e) {
            logger.info("Try to get book with wrong book id = " + bookId);
            throw new Exception("Information not available, book don't exist");
        }
    }

    @Override
    public int getCountById(long bookId) throws Exception {
        try {
            logger.info("Try to get count of book with book id = " + bookId);
            return bookRepository.findOne(bookId).getCount();
        } catch (Exception e) {
            logger.info("Try to get count of book with wrong book id = " + bookId);
            throw new Exception("Information not available, book don't exist");
        }
    }

    @Override
    public void decrementCountById(long bookId) throws Exception {
        try {
            logger.info("Try to decrement count of book with book id = " + bookId);
            bookRepository.findOne(bookId).setCount(bookRepository.findOne(bookId).getCount() - 1);
        } catch (Exception e) {
            logger.info("Try to decrement count of book with wrong book id = " + bookId);
            throw new Exception("Information not available, book don't exist");
        }
    }

    @Override
    public int getPriceById(long bookId) throws Exception {
        try {
            logger.info("Try to get price of book with book id = " + bookId);
            return bookRepository.findOne(bookId).getPrice();
        } catch (Exception e) {
            logger.info("Try to get price of book with wrong book id = " + bookId);
            throw new Exception("Information not available, book don't exist");
        }
    }

    public boolean getIsBestseller(long bookId) throws Exception {
        try {
            logger.info("Try to get is bestseller book with book id = " + bookId);
            return bookRepository.findOne(bookId).isBestSeller();
        } catch (Exception e) {
            logger.info("Try to get is bestseller book with wrong book id = " + bookId);
            throw new Exception("Information not available, book don't exist");
        }
    }

    @Override
    public List<Book> getListCountNotZeroOrRenewed() {
        List<Book> books = bookRepository.findAll().stream().filter(book -> book.getCount() > 0).collect(Collectors.toList());

        for (Book book : books) {
            logger.info("Book list : " + book);
        }

        return books;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Book> getList() {
        List<Book> books = bookRepository.findAll();

        for (Book book : books) {
            logger.info("Book list : " + book);
        }

        return books;
    }

    public boolean isAlreadyExist(Book book){
        return bookRepository.existsBookByTitle(book.getTitle());
    }

    public List<Book> getListofAccessibleBooks(){
        return bookRepository.findAll().stream().filter(book -> book.getCount() > 0).collect(Collectors.toList());
    }
}
