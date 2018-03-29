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

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class BookDaoImplementation implements DocumentDao<Book> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = Document.BOOK;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
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
        try {
            return bookRepository.findAll().stream()
                    .filter(b -> b.getTitle().equals(book.getTitle()) &&
                            b.getAuthor().equals(book.getAuthor()) &&
                            b.getPublisher().equals(book.getPublisher()) &&
                            b.getEdition().equals(book.getEdition()) &&
                            b.getYear() == book.getYear() &&
                            b.getNote().equals(book.getNote()))
                    .findFirst().get();
        } catch (NullPointerException | NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Book getById(long bookId) {
        return bookRepository.findOne(bookId);
    }

    /**
     * get how many copies of books we already have in library
     *
     * @param bookId id of book
     * @return count of copies
     * @throws Exception invalid id
     */
    @Override
    public int getCountById(long bookId) throws Exception {
        try {
            return bookRepository.findOne(bookId).getCount();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    /**
     * set count to count - 1 for book
     *
     * @param bookId id of book
     */
    @Override
    public void decrementCountById(long bookId) {
        Book book = bookRepository.findOne(bookId);
        book.setCount(book.getCount() - 1);
        bookRepository.saveAndFlush(book);
    }

    /**
     * set count to count + 1 for book
     *
     * @param bookId id of book
     */
    @Override
    public void incrementCountById(long bookId) {
        Book book = bookRepository.findOne(bookId);
        book.setCount(book.getCount() + 1);
        bookRepository.saveAndFlush(book);
    }

    /**
     * get price of book by id
     *
     * @param bookId id of AudioVideo
     * @return price of book
     * @throws Exception got invalid id
     */
    @Override
    public int getPriceById(long bookId) throws Exception {
        try {
            return bookRepository.findOne(bookId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getNote(long bookId) throws Exception {
        try {
            return bookRepository.findOne(bookId).getNote();
        } catch (Exception e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

//    /**
//     * get list of all books with count bigger than zero or renewed
//     *
//     * @return list of books with count bigger than zero or renewed
//     */
//    @Override
//    public List<Book> getListCountNotZeroOrRenewed() {
//        try {
//            List<Book> books = bookRepository.findAll().stream()
//                    .filter(book -> book.getCount() > 0).collect(Collectors.toList());
//
//            for (Book book : books) {
//                logger.info("Book list : " + book);
//            }
//
//            return books;
//        } catch (NoSuchElementException | NullPointerException e) {
//            return new ArrayList<>();
//        }
//    }

    /**
     * get list of all books
     *
     * @return list of books
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Book> getList() {
        try {
            List<Book> books = bookRepository.findAll();

            for (Book book : books) {
                logger.info(LIST + book);
            }

            return books;
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public long getId(Book book) throws Exception {
        try {
            return isAlreadyExist(book).getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
