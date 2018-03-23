package com.project.glib;

import com.project.glib.controller.BookController;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.model.Book;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BookControllerTest {
    @Autowired
    private BookDaoImplementation bookDao;
    @Autowired
    private BookController bookCtrl;

    private Book newBook;
    private Book existedBook;

    @Before
    public void setup() {
        newBook = new Book("Title", "Author", "Publisher", "Edition", 2017, "BESTSELLER", 100, 2);
        existedBook = new Book(newBook, 3);
    }

    @After
    public void teamDown() throws Exception {
        bookDao.remove(newBook.getId());
        newBook = null;
        existedBook = null;
    }

    @Test
    public void addBook() {
        assertNull(bookDao.isAlreadyExist(newBook));
        bookCtrl.addBook(newBook, "SH0");
        assertTrue(bookDao.isAlreadyExist(newBook).equals(newBook));
        bookCtrl.addBook(existedBook, "SH1");
        long bookId = bookDao.isAlreadyExist(newBook).getId();
        int copies = 0;
        copies = bookDao.getCountById(bookId);
        assertTrue(copies == 2 + 3);
    }

    @Ignore
    @Test
    public void removeBook() {

    }
}
