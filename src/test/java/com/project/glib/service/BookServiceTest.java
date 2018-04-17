package com.project.glib.service;

import com.project.glib.model.Book;
import com.project.glib.model.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;

import static com.project.glib.service.BookService.EXIST_EXCEPTION;
import static com.project.glib.service.DocumentServiceInterface.*;
import static com.project.glib.service.ModifyByLibrarianService.SHELF_EXCEPTION;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private DocumentPhysicalService docPhysService;
    private Book book, bookInDao;
    private String shelf1, shelf2;
    private int count1, count2;

    @Before
    public void setup() {
        book = new Book("title", "author", "publisher",
                "edition", 2017, Document.DEFAULT_NOTE, 100, 2, "picture", null);
        shelf1 = "Shelf1";
        shelf2 = "Shelf2";
        count1 = 2;
        count2 = 4;
    }

    @After
    public void tearDown() {
        try {
            bookService.remove(book.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void isAlreadyExistReturnNull() {
        bookInDao = bookService.isAlreadyExist(book);

        assertNull(bookInDao);
    }

    @Test
    public void isAlreadyExistReturnBook() throws Exception {
        bookService.add(book, shelf1);
        bookInDao = bookService.isAlreadyExist(book);

        assertNotNull(bookInDao);
        assertEquals(book, bookInDao);
    }

    @Test
    public void addNotExistedBook() throws Exception {
        bookService.add(book, shelf1);
        bookInDao = bookService.isAlreadyExist(book);

        assertEquals(book, bookInDao);
        assertEquals(docPhysService.getCount(bookInDao.getId(), Document.BOOK), bookInDao.getCount());
    }

    @Test
    public void addExistedBook() throws Exception {
        book.setCount(count1);
        bookService.add(book, shelf1);
        book.setCount(count2);
        bookService.add(book, shelf2);
        bookInDao = bookService.isAlreadyExist(book);

        assertEquals(count1 + count2, bookInDao.getCount());
        assertEquals(docPhysService.getCount(bookInDao.getId(), Document.BOOK), bookInDao.getCount());
    }

    @Test
    public void addNullShelf() {
        try {
            bookService.add(book, "");
        } catch (Exception e) {
            assertEquals(SHELF_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void update() throws Exception {
        // don't change count when using update
        bookService.add(book, shelf1);

        book.setTitle("title2");
        book.setAuthor("author2");
        book.setPublisher("publisher2");
        book.setEdition("edition2");
        book.setYear(2010);
        book.setNote(Document.REFERENCE);
        book.setPrice(200);

        bookService.update(book);
        bookInDao = bookService.isAlreadyExist(book);

        assertEquals(book, bookInDao);
        assertEquals(docPhysService.getCount(bookInDao.getId(), Document.BOOK), bookInDao.getCount());

    }

    @Test
    public void remove() throws Exception {
        bookService.add(book, shelf1);
        bookService.remove(book.getId());

        bookInDao = bookService.isAlreadyExist(book);
        assertNull(bookInDao);
        assertEquals(0, docPhysService.getCount(book.getId(), Document.BOOK));
    }

    @Test
    public void getIdReturnId() throws Exception {
        bookService.add(book, shelf1);
        bookInDao = bookService.isAlreadyExist(book);

        assertEquals(book.getId(), bookService.getId(book));
    }

    @Test
    public void getIdReturnException() {
        try {
            bookService.getId(book);
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void decrementCountById() throws Exception {
        bookService.add(book, shelf1);

        int before = bookService.getCountById(book.getId());
        bookService.decrementCountById(book.getId());

        assertEquals(before - 1, bookService.getCountById(book.getId()));
    }

    @Test
    public void incrementCountById() throws Exception {
        bookService.add(book, shelf1);

        int before = bookService.getCountById(book.getId());
        bookService.incrementCountById(book.getId());

        assertEquals(before + 1, bookService.getCountById(book.getId()));
    }

    @Test
    public void checkValidParametersReturnException1() {
        try {
            book.setTitle("");
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(TITLE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException2() {
        try {
            book.setAuthor("");
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(AUTHOR_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException3() {
        try {
            book.setPublisher("");
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(PUBLISHER_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException4() {
        try {
            book.setEdition("");
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(EDITION_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException5() {
        try {
            book.setYear(Calendar.getInstance().get(Calendar.YEAR) + 1);
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(YEAR_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException6() {
        try {
            book.setNote("NOT_NOTE");
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(NOTE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException7() {
        try {
            book.setPrice(-1);
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(PRICE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParametersReturnException9() {
        try {
            book.setCount(-10);
            bookService.checkValidParameters(book);
        } catch (Exception e) {
            assertEquals(COUNT_EXCEPTION, e.getMessage());
        }
    }
}