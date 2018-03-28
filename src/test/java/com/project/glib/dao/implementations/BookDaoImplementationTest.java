//package com.project.glib.dao.implementations;
//
//import com.project.glib.model.Book;
//import com.project.glib.model.Document;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.Calendar;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BookDaoImplementationTest {
//    @Autowired
//    private BookDaoImplementation bookDao;
//    @Autowired
//    private DocumentPhysicalDaoImplementation docPhysDao;
//    private Book book, bookInDao;
//    private String shelf1, shelf2;
//    private int count1, count2;
//
//    @Before
//    public void setup() {
//        book = new Book("title", "author", "publisher",
//                "edition", 2017, Document.DEFAULT_NOTE, 100, 2);
//        shelf1 = "Shelf1";
//        shelf2 = "Shelf2";
//        count1 = 2;
//        count2 = 4;
//    }
//
//    @After
//    public void tearDown() {
//        try {
//            bookDao.remove(book.getId());
//        } catch (Exception ignore) {
//        }
//    }
//
//    @Test
//    public void isAlreadyExistReturnNull() {
//        bookInDao = bookDao.isAlreadyExist(book);
//
//        assertNull(bookInDao);
//    }
//
//    @Test
//    public void isAlreadyExistReturnBook() throws Exception {
//        bookDao.add(book, shelf1);
//        bookInDao = bookDao.isAlreadyExist(book);
//
//        assertNotNull(bookInDao);
//        assertEquals(bookInDao, book);
//    }
//
//    @Test
//    public void addNotExistedBook() throws Exception {
//        bookDao.add(book, shelf1);
//        bookInDao = bookDao.isAlreadyExist(book);
//
//        assertEquals(bookInDao, book);
//        assertTrue(docPhysDao.getCount(bookInDao.getId(), Document.BOOK) == bookInDao.getCount());
//    }
//
//    @Test
//    public void addExistedBook() throws Exception {
//        book.setCount(count1);
//        bookDao.add(book, shelf1);
//        book.setCount(count2);
//        bookDao.add(book, shelf2);
//        bookInDao = bookDao.isAlreadyExist(book);
//
//        assertTrue(bookInDao.getCount() == count1 + count2);
//        assertTrue(docPhysDao.getCount(bookInDao.getId(), Document.BOOK) == bookInDao.getCount());
//    }
//
//    @Test(expected = Exception.class)
//    public void addNullShelf() throws Exception {
//        bookDao.add(book, "");
//    }
//
//    @Test
//    public void update() throws Exception {
//        // don't change count when using update
//        bookDao.add(book, shelf1);
//
//        book.setTitle("title2");
//        book.setAuthor("author2");
//        book.setPublisher("publisher2");
//        book.setEdition("edition2");
//        book.setYear(2010);
//        book.setNote(Document.REFERENCE);
//        book.setPrice(200);
//
//        bookDao.update(book);
//        bookInDao = bookDao.isAlreadyExist(book);
//
//        assertEquals(bookInDao, book);
//        assertTrue(docPhysDao.getCount(bookInDao.getId(), Document.BOOK) == bookInDao.getCount());
//
//    }
//
//    @Test
//    public void remove() throws Exception {
//        bookDao.add(book, shelf1);
//        bookDao.remove(book.getId());
//
//        bookInDao = bookDao.isAlreadyExist(book);
//        assertNull(bookInDao);
//        assertTrue(docPhysDao.getCount(book.getId(), Document.BOOK) == 0);
//    }
//
//    @Test
//    public void getIdReturnId() throws Exception {
//        bookDao.add(book, shelf1);
//        bookInDao = bookDao.isAlreadyExist(book);
//
//        assertTrue(bookDao.getId(book) == book.getId());
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException() throws Exception {
//        bookDao.getId(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException1() throws Exception {
//        book.setTitle("");
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException2() throws Exception {
//        book.setAuthor("");
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException3() throws Exception {
//        book.setPublisher("");
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException4() throws Exception {
//        book.setEdition("");
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException5() throws Exception {
//        book.setYear(Calendar.getInstance().get(Calendar.YEAR) + 1);
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException6() throws Exception {
//        book.setNote("NOT_NOTE");
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException7() throws Exception {
//        book.setPrice(-1);
//        bookDao.checkValidParameters(book);
//    }
//
//    @Test(expected = Exception.class)
//    public void checkValidParametersReturnException9() throws Exception {
//        book.setCount(-10);
//        bookDao.checkValidParameters(book);
//    }
//}