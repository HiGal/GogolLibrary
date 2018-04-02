package com.project.glib.service;

import com.project.glib.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.project.glib.service.CheckoutService.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CheckoutServiceTest {
    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private BookService bookService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private AudioVideoService avService;
    @Autowired
    private DocumentPhysicalService docPhysService;
    @Autowired
    private UserService userService;
    private Checkout checkout1;
    private Checkout checkout2;
    private Checkout checkout3;
    private User user;
    private Book book;
    private Journal journal;
    private AudioVideo av;

    @Before
    public void setup() throws Exception {
        String shelf1 = "SH010";

        book = new Book("title", "author", "publisher",
                "edition", 2017, Document.DEFAULT_NOTE, 100, 2, "picture");
        journal = new Journal("title", "author", "name", 2,
                "editor", Document.REFERENCE, 200, 3, "picture");
        av = new AudioVideo("title", "author", 400, 2, "picture");
        user = new User("login", "password",
                "name", "surname", "address", "79134562845", User.STUDENT, true, "picture");

        bookService.add(book, shelf1);
        journalService.add(journal, shelf1);
        avService.add(av, shelf1);
        userService.add(user);

        long docPhysId1 = docPhysService.getValidPhysId(book.getId(), Document.BOOK);
        long docPhysId2 = docPhysService.getValidPhysId(journal.getId(), Document.JOURNAL);
        long docPhysId3 = docPhysService.getValidPhysId(av.getId(), Document.AV);

        checkout1 = new Checkout(user.getId(), docPhysId1, System.nanoTime(),
                System.nanoTime() + WEEK_IN_MILLISECONDS * 3, shelf1);
        checkout2 = new Checkout(user.getId(), docPhysId2, System.nanoTime(),
                System.nanoTime() + WEEK_IN_MILLISECONDS * 2, shelf1);
        checkout3 = new Checkout(user.getId(), docPhysId3, System.nanoTime(),
                System.nanoTime() + WEEK_IN_MILLISECONDS * 2, shelf1);
    }

    @After
    public void tearDown() throws Exception {
        try {
            checkoutService.remove(checkout1.getId());
        } catch (Exception ignore) {
        }

        try {
            checkoutService.remove(checkout2.getId());
        } catch (Exception ignore) {
        }

        try {
            checkoutService.remove(checkout3.getId());
        } catch (Exception ignore) {
        }

        bookService.remove(book.getId());
        journalService.remove(journal.getId());
        avService.remove(av.getId());
        userService.remove(user.getId());
    }

    @Test
    public void addNotExistedCheckout() throws Exception {
        checkoutService.add(checkout1);
        assertEquals(checkout1, checkoutService.getById(checkout1.getId()));

        checkoutService.add(checkout2);
        assertEquals(checkout2, checkoutService.getById(checkout2.getId()));

        checkoutService.add(checkout3);
        assertEquals(checkout3, checkoutService.getById(checkout3.getId()));
    }

    @Test
    public void addExistedCheckout() {
        addExistedCheckout(checkout1);
        addExistedCheckout(checkout2);
        addExistedCheckout(checkout3);
    }

    private void addExistedCheckout(Checkout checkout) {
        try {
            checkoutService.add(checkout);
            checkoutService.add(checkout);
        } catch (Exception e) {
            assertEquals(ALREADY_HAS_THIS_CHECKOUT_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void remove() throws Exception {
        remove(checkout1);
        remove(checkout2);
        remove(checkout3);
    }

    private void remove(Checkout checkout) throws Exception {
        checkoutService.add(checkout);
        checkoutService.remove(checkout.getId());
        assertNull(checkoutService.getById(checkout.getId()));
    }

    private void getIdReturnId(Checkout checkout) throws Exception {
        checkoutService.add(checkout);
        assertEquals(checkoutService.getId(checkout), checkout.getId());
    }

    @Test
    public void getIdReturnId() throws Exception {
        getIdReturnId(checkout1);
        getIdReturnId(checkout2);
        getIdReturnId(checkout3);
    }

    @Test
    public void getIdReturnException() {
        try {
            checkoutService.getId(checkout1);
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }

        try {
            checkoutService.getId(checkout2);
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }

        try {
            checkoutService.getId(checkout3);
        } catch (Exception e) {
            assertEquals(EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void alreadyHasThisCheckoutTrue() throws Exception {
        checkoutService.add(checkout1);
        checkoutService.add(checkout2);
        checkoutService.add(checkout3);

        assertTrue(checkoutService
                .alreadyHasThisCheckout(checkout1.getDocPhysId(), checkout1.getUserId()));
        assertTrue(checkoutService
                .alreadyHasThisCheckout(checkout2.getDocPhysId(), checkout2.getUserId()));
        assertTrue(checkoutService
                .alreadyHasThisCheckout(checkout3.getDocPhysId(), checkout3.getUserId()));

    }

    @Test
    public void alreadyHasThisCheckoutFalse() {
        assertFalse(checkoutService
                .alreadyHasThisCheckout(checkout1.getDocPhysId(), checkout1.getUserId()));
        assertFalse(checkoutService
                .alreadyHasThisCheckout(checkout2.getDocPhysId(), checkout2.getUserId()));
        assertFalse(checkoutService
                .alreadyHasThisCheckout(checkout3.getDocPhysId(), checkout3.getUserId()));
    }
}