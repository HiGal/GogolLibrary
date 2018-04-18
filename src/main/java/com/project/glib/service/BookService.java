package com.project.glib.service;

import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.model.Book;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class BookService implements DocumentServiceInterface<Book> {
    public static final String TYPE = Document.BOOK;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private final BookDaoImplementation bookDao;
    private final DocumentPhysicalService docPhysService;
    private final LoggerService loggerService;

    public BookService(BookDaoImplementation bookDao, DocumentPhysicalService docPhysService, LoggerService loggerService) {
        this.bookDao = bookDao;
        this.docPhysService = docPhysService;
        this.loggerService = loggerService;
    }

    @Override
    public void add(Book book, String shelf) throws Exception {
        if (shelf.equals("")) throw new Exception(SHELF_EXCEPTION);
        add(book);
        for (int i = 0; i < book.getCount(); i++) {
            // TODO add keywords options
            docPhysService.add(new DocumentPhysical(shelf, true, book.getId(), Document.BOOK));
        }
    }

    private void add(Book book) throws Exception {
        checkValidParameters(book);
        try {
            Book existedBook = isAlreadyExist(book);
            if (existedBook == null) {
                bookDao.add(book);
            } else {
                existedBook.setCount(existedBook.getCount() + book.getCount());
                existedBook.setPrice(book.getPrice());
                bookDao.update(existedBook);
            }
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    @Override
    public void update(Book book) throws Exception {
        checkValidParameters(book);
        // TODO solve case then librarian change count of books
        try {
            Book existedBook = isAlreadyExist(book);
            if (existedBook != null && !existedBook.equals(book)) {
                remove(book.getId());
                existedBook.setCount(existedBook.getCount() + book.getCount());
                existedBook.setPrice(book.getPrice());
                book = existedBook;
            }
            bookDao.update(book);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    @Override
    public void remove(long bookId) throws Exception {
        try {
            docPhysService.removeAllByDocVirIdAndDocType(bookId, Document.BOOK);
            bookDao.remove(bookId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    @Override
    public void removeCopy(long bookId, long copyId) throws Exception {
        docPhysService.remove(copyId);
        decrementCountById(bookId);
    }

    @Override
    public void removeAllCopiesByShelf(long bookId, String shelf) throws Exception {
        List<DocumentPhysical> docPhysList = docPhysService.getByDocVirIdAndDocType(bookId, Document.BOOK)
                .stream()
                .filter(doc -> doc.getShelf().equals(shelf))
                .collect(Collectors.toList());

        for (DocumentPhysical docPhys : docPhysList) removeCopy(bookId, docPhys.getId());
    }

    /**
     * Validator for book model
     *
     * @param book book model
     * @throws Exception
     */
    @Override
    public void checkValidParameters(Book book) throws Exception {
        if (book.getPrice() <= 0) {
            throw new Exception(PRICE_EXCEPTION);
        }

        if (book.getCount() < 0) {
            throw new Exception(COUNT_EXCEPTION);
        }

        if (book.getTitle().equals("")) {
            throw new Exception(TITLE_EXCEPTION);
        }

        if (book.getAuthor().equals("")) {
            throw new Exception(AUTHOR_EXCEPTION);
        }

        if (book.getEdition().equals("")) {
            throw new Exception(EDITION_EXCEPTION);
        }

        if (book.getPublisher().equals("")) {
            throw new Exception(PUBLISHER_EXCEPTION);
        }

        if (!isNote(book.getNote())) {
            throw new Exception(NOTE_EXCEPTION);
        }

        if (book.getYear() > Calendar.getInstance().get(Calendar.YEAR)) {
            throw new Exception(YEAR_EXCEPTION);
        }
    }

    @Override
    public boolean isNote(String note) {
        return note.equals(Document.DEFAULT_NOTE) || note.equals(Document.REFERENCE) || note.equals(Document.BESTSELLER);
    }

    @Override
    public Book isAlreadyExist(Book book) {
        try {
            return bookDao.isAlreadyExist(book);
        } catch (NullPointerException | NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Book getById(long bookId) {
        return bookDao.getById(bookId);
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
            return getById(bookId).getCount();
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
        Book book = getById(bookId);
        book.setCount(book.getCount() - 1);
        bookDao.update(book);
    }

    /**
     * set count to count + 1 for book
     *
     * @param bookId id of book
     */
    @Override
    public void incrementCountById(long bookId) {
        Book book = getById(bookId);
        book.setCount(book.getCount() + 1);
        bookDao.update(book);
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
            return getById(bookId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getNote(long bookId) throws Exception {
        try {
            return getById(bookId).getNote();
        } catch (Exception e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    /**
     * get list of all books
     *
     * @return list of books
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<Book> getList() {
        try {
            return bookDao.getList();
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public long getId(Book book) throws Exception {
        try {
            return bookDao.getId(book);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
