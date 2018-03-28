//package com.project.glib.dao.implementations;
//
//import com.project.glib.model.*;
//import com.project.glib.service.CheckOutService;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class CheckoutDaoImplementationTest {
//    @Autowired
//    private CheckoutDaoImplementation checkoutDao;
//    @Autowired
//    private BookDaoImplementation bookDao;
//    @Autowired
//    private JournalDaoImplementation journalDao;
//    @Autowired
//    private AudioVideoDaoImplementation avDao;
//    @Autowired
//    private UsersDaoImplementation userDao;
//    private Checkout checkout1;
//    private Checkout checkout2;
//    private Checkout checkout3;
//    private User user;
//    private Book book;
//    private Journal journal;
//    private AudioVideo av;
//
//    @Before
//    public void setup() throws Exception {
//        String shelf1 = "SH010";
//
//        book = new Book("title", "author", "publisher",
//                "edition", 2017, Document.DEFAULT_NOTE, 100, 2);
//        journal = new Journal("title", "author", "name", 2,
//                "editor", Document.REFERENCE, 200, 3);
//        av = new AudioVideo("title", "author", 400, 2);
//        user = new User("login", "password", "passconf",
//                "name", "surname", "address", "79134562845672", User.STUDENT, true);
//
//        bookDao.add(book, shelf1);
//        journalDao.add(journal, shelf1);
//        avDao.add(av, shelf1);
//        userDao.add(user);
//
//        checkout1 = new Checkout(user.getId(), book.getId(), Document.BOOK, System.nanoTime(),
//                System.nanoTime() + CheckOutService.WEEK_IN_MILLISECONDS * 3, false, shelf1);
//        checkout2 = new Checkout(user.getId(), journal.getId(), Document.JOURNAL, System.nanoTime(),
//                System.nanoTime() + CheckOutService.WEEK_IN_MILLISECONDS * 2, false, shelf1);
//        checkout3 = new Checkout(user.getId(), av.getId(), Document.AV, System.nanoTime(),
//                System.nanoTime() + CheckOutService.WEEK_IN_MILLISECONDS * 2, false, shelf1);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        try {
//            checkoutDao.remove(checkout1.getId());
//        } catch (Exception ignore) {
//        }
//
//        try {
//            checkoutDao.remove(checkout2.getId());
//        } catch (Exception ignore) {
//        }
//
//        try {
//            checkoutDao.remove(checkout3.getId());
//        } catch (Exception ignore) {
//        }
//
//        bookDao.remove(book.getId());
//        journalDao.remove(journal.getId());
//        avDao.remove(av.getId());
//        userDao.remove(user.getId());
//    }
//
//    @Test
//    public void addNotExistedCheckout() throws Exception {
//        checkoutDao.add(checkout1);
//        assertEquals(checkoutDao.getById(checkout1.getId()), checkout1);
//
//        checkoutDao.add(checkout2);
//        assertEquals(checkoutDao.getById(checkout2.getId()), checkout2);
//
//        checkoutDao.add(checkout3);
//        assertEquals(checkoutDao.getById(checkout3.getId()), checkout3);
//    }
//
//    @Test(expected = Exception.class)
//    public void addExistedCheckout1() throws Exception {
//        checkoutDao.add(checkout1);
//        checkoutDao.add(checkout1);
//    }
//
//    @Test(expected = Exception.class)
//    public void addExistedCheckout2() throws Exception {
//        checkoutDao.add(checkout2);
//        checkoutDao.add(checkout2);
//    }
//
//    @Test(expected = Exception.class)
//    public void addExistedCheckout3() throws Exception {
//        checkoutDao.add(checkout3);
//        checkoutDao.add(checkout3);
//    }
//
//    private void update(Checkout checkout) throws Exception {
//        // don't change count when using update
//        checkoutDao.add(checkout);
//        // do smth with booking
//        checkoutDao.update(checkout);
//        assertEquals(checkoutDao.getById(checkout.getId()), checkout);
//    }
//
//    @Test
//    public void update() throws Exception {
//        update(checkout1);
//        update(checkout2);
//        update(checkout3);
//    }
//
//    private void remove(Checkout checkout) throws Exception {
//        checkoutDao.add(checkout);
//        checkoutDao.remove(checkout.getId());
//        assertNull(checkoutDao.getById(checkout.getId()));
//    }
//
//    @Test
//    public void remove() throws Exception {
//        remove(checkout1);
//        remove(checkout2);
//        remove(checkout3);
//    }
//
//    private void getIdReturnId(Checkout checkout) throws Exception {
//        checkoutDao.add(checkout);
//        assertTrue(checkoutDao.getId(checkout) == checkout.getId());
//    }
//
//    @Test
//    public void getIdReturnId() throws Exception {
//        getIdReturnId(checkout1);
//        getIdReturnId(checkout2);
//        getIdReturnId(checkout3);
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException1() throws Exception {
//        checkoutDao.getId(checkout1);
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException2() throws Exception {
//        checkoutDao.getId(checkout2);
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException3() throws Exception {
//        checkoutDao.getId(checkout3);
//    }
//
//    @Test
//    public void alreadyHasThisCheckoutTrue() throws Exception {
//        checkoutDao.add(checkout1);
//        checkoutDao.add(checkout2);
//        checkoutDao.add(checkout3);
//
//        assertTrue(checkoutDao
//                .alreadyHasThisCheckout(checkout1.getDocPhysId(), checkout1.getDocType(), checkout1.getUserId()));
//        assertTrue(checkoutDao
//                .alreadyHasThisCheckout(checkout2.getDocPhysId(), checkout2.getDocType(), checkout2.getUserId()));
//        assertTrue(checkoutDao
//                .alreadyHasThisCheckout(checkout3.getDocPhysId(), checkout3.getDocType(), checkout3.getUserId()));
//
//    }
//
//    @Test
//    public void alreadyHasThisCheckoutFalse() {
//        assertFalse(checkoutDao
//                .alreadyHasThisCheckout(checkout1.getDocPhysId(), checkout1.getDocType(), checkout1.getUserId()));
//        assertFalse(checkoutDao
//                .alreadyHasThisCheckout(checkout2.getDocPhysId(), checkout2.getDocType(), checkout2.getUserId()));
//        assertFalse(checkoutDao
//                .alreadyHasThisCheckout(checkout3.getDocPhysId(), checkout3.getDocType(), checkout3.getUserId()));
//    }
//}