package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.model.Book;
import com.project.glib.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class BookDaoImplementation implements DocumentDao<Book> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookRepository bookRepository;
    private final DocumentPhysicalDaoImplementation documentPhysDao;

    @Autowired
    public BookDaoImplementation(BookRepository bookRepository, DocumentPhysicalDaoImplementation documentPhysDao) {
        this.bookRepository = bookRepository;
        this.documentPhysDao = documentPhysDao;
    }

    /**
     * Add new item of Book in library
     *
     * @param book new Book
     * @throws Exception
     */
    @Override
    public void add(Book book) throws Exception {
        checkValidParameters(book);
        try {
            Book existedBook = isAlreadyExist(book);
            if (existedBook == null) {
                bookRepository.saveAndFlush(book);
                logger.info("Book successfully saved. Book details : " + book);
            } else {
                logger.info("Try to add " + book.getCount() + " copies of book : " + existedBook);
                existedBook.setCount(existedBook.getCount() + book.getCount());
                existedBook.setPrice(book.getPrice() );
                update(existedBook);
            }
        } catch (Exception e) {
            logger.info("Error in method add() in class BookDaoImplementation");
            throw new Exception("Can't add this book, something wrong");
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
        checkValidParameters(book);
        // TODO solve case then librarian change count of books
        try {
            bookRepository.saveAndFlush(book);
            logger.info("Book successfully update. Book details : " + book);
        } catch (Exception e) {
            logger.info("Try to update this book, book don't exist or something wrong. " +
                    "Update book information : " + book);
            throw new Exception("Can't update this book, book don't exist or something wrong");
        }
    }

    /**
     * Remove Book from library
     *
     * @param bookId id of Book
     * @throws Exception
     */
    @Override
    public void remove(long bookId) throws Exception {
        try {
            logger.info("Try to delete book with book id = " + bookId);
            documentPhysDao.removeAllByDocId(bookId);
            bookRepository.delete(bookId);
        } catch (Exception e) {
            logger.info("Try to delete book with wrong book id = " + bookId);
            throw new Exception("Delete this book not available, book don't exist");
        }
    }

    @Override
    public void checkValidParameters(Book book) throws Exception {
        if (book.getPrice() < 0) {
            throw new Exception("Price must be positive");
        }

        if (book.getCount() < 0) {
            throw new Exception("Count must be positive");
        }

        if (book.getTitle().equals("")) {
            throw new Exception("Title must exist");
        }

        if (book.getBookAuthor().equals("")) {
            throw new Exception("Author must exist");
        }

        if (book.getEdition().equals("")) {
            throw new Exception("Edition must exist");
        }

        if (book.getPublisher().equals("")) {
            throw new Exception("Publisher must exist");
        }

        // TODO not accuracy
        if (!book.getNote().equals(Document.REFERENCE) && !book.getNote().equals(Document.DEFAULT_NOTE) && !book.getNote().equals(Book.BESTSELLER)) {
            throw new Exception("Invalid note");
        }

        // TODO reduce method getYear()
        if (book.getYear() > new Date(System.nanoTime()).getYear()) {
            throw new Exception("Year must be less or equal than current");
        }
    }

    @Override
    public Book isAlreadyExist(Book book) {
        try {
            return bookRepository.findAll().stream()
                    .filter(b -> b.getTitle().equals(book.getTitle()) &&
                            b.getBookAuthor().equals(book.getBookAuthor()) &&
                            b.getPublisher().equals(book.getPublisher()) &&
                            b.getEdition().equals(book.getEdition()) &&
                            b.getYear() == book.getYear() &&
                            b.getNote().equals(book.getNote()))
                    .findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
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
            throw new Exception("Information not available, book doesn't exist. Get by ID failed");
        }
    }

    @Override
    public int getCountById(long bookId) {
        try {
            logger.info("Try to get count of book with book id = " + bookId);
            return bookRepository.findOne(bookId).getCount();
        } catch (Exception e) {
//            logger.info("Try to get count of book with wrong book id = " + bookId);
//            throw new Exception("Information not available, book don't exist");
            return 0;
        }
    }

    @Override
    public void decrementCountById(long bookId) throws Exception {
        try {
            logger.info("Try to decrement count of book with book id = " + bookId);
            int i = bookRepository.findOne(bookId).getCount() - 1;
            bookRepository.findOne(bookId).setCount(i);
            bookRepository.saveAndFlush(bookRepository.findOne(bookId));
        } catch (Exception e) {
            logger.info("Try to decrement count of book with wrong book id = " + bookId);
            throw new Exception("Information not available, book don't exist");
        }
    }

    @Override
    public void incrementCountById(long bookId) throws Exception {
        try {
            logger.info("Try to increment count of book with book id = " + bookId);
            int i = bookRepository.findOne(bookId).getCount() + 1;
            bookRepository.findOne(bookId).setCount(i);
            bookRepository.saveAndFlush(bookRepository.findOne(bookId));
        } catch (Exception e) {
            logger.info("Try to increment count of book with wrong book id = " + bookId);
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

    public String getNote(long bookId) throws Exception {
        try {
            logger.info("Try to get note book with book id = " + bookId);
            return bookRepository.findOne(bookId).getNote();
        } catch (Exception e) {
            logger.info("Try to get note book with wrong book id = " + bookId);
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
}
