package com.project.glib.service;

import com.project.glib.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.project.glib.dao.implementations.UserDaoImplementation.REMOVE_USER_HAS_CHECKOUTS_EXCEPTION;
import static com.project.glib.service.UserService.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    private BookService bookService;
    @Autowired
    private JournalService journalService;
    @Autowired
    private AudioVideoService avService;
    @Autowired
    private DocumentPhysicalService docPhysService;
    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private UserService userService;
    private User user;
    private String shelf;


    @Before
    public void setUp() {
        user = new User("login", "password", "name",
                "surname", "address", "78953534634", User.STUDENT, true);
        shelf = "SHELF";
    }

    @After
    public void tearDown() {
        try {
            userService.remove(user.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void addNotExistedUser() throws Exception {
        userService.add(user);
        assertEquals(userService.getById(user.getId()), user);
    }

    @Test
    public void addExistedUser() {
        try {
            userService.add(user);
            userService.add(user);
        } catch (Exception e) {
            assertEquals(LOGIN_ALREADY_EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void addUserWithExistedLogin() {
        try {
            userService.add(user);
            userService.add(new User(user.getLogin(), "pass", "n",
                    "surn", "ad", "78945612358", User.PROFESSOR, true));
        } catch (Exception e) {
            assertEquals(LOGIN_ALREADY_EXIST_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void update() throws Exception {
        userService.add(user);
        // do smth with user
        userService.update(user);
        assertEquals(userService.getById(user.getId()), user);
    }

    @Test
    public void removeSuccessful() throws Exception {
        userService.add(user);
        userService.remove(user.getId());
        assertNull(userService.getById(user.getId()));
    }

    @Test
    public void removeHasCheckout1() throws Exception {
        userService.add(user);
        Book book = new Book("title", "author", "publisher",
                "edition", 2017, Document.DEFAULT_NOTE, 100, 2);
        bookService.add(book, shelf);
        DocumentPhysical docPhys = docPhysService.getById(docPhysService.getValidPhysId(book.getId(), Document.BOOK));
        Checkout checkout = new Checkout(user.getId(), docPhys.getId(), System.nanoTime(),
                System.nanoTime() + CheckoutService.WEEK_IN_MILLISECONDS * 3, docPhys.getShelf());
        checkoutService.add(checkout);

        try {
            userService.remove(user.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), REMOVE_USER_HAS_CHECKOUTS_EXCEPTION);
        }

        checkoutService.remove(checkout.getId());
        bookService.remove(book.getId());
    }

    @Test
    public void removeHasCheckout2() throws Exception {
        userService.add(user);
        Journal journal = new Journal("title", "author", "name", 2,
                "editor", Document.REFERENCE, 200, 3);
        journalService.add(journal, shelf);
        DocumentPhysical docPhys = docPhysService.getById(docPhysService.getValidPhysId(journal.getId(), Document.JOURNAL));
        Checkout checkout = new Checkout(user.getId(), docPhys.getId(), System.nanoTime(),
                System.nanoTime() + CheckoutService.WEEK_IN_MILLISECONDS * 3, docPhys.getShelf());
        checkoutService.add(checkout);

        try {
            userService.remove(user.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), REMOVE_USER_HAS_CHECKOUTS_EXCEPTION);
        }

        checkoutService.remove(checkout.getId());
        journalService.remove(journal.getId());
    }

    @Test
    public void removeHasCheckout3() throws Exception {
        userService.add(user);
        AudioVideo av = new AudioVideo("title", "author", 400, 2);
        avService.add(av, shelf);
        DocumentPhysical docPhys = docPhysService.getById(docPhysService.getValidPhysId(av.getId(), Document.AV));
        Checkout checkout = new Checkout(user.getId(), docPhys.getId(), System.nanoTime(),
                System.nanoTime() + CheckoutService.WEEK_IN_MILLISECONDS * 3, docPhys.getShelf());
        checkoutService.add(checkout);

        try {
            userService.remove(user.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), REMOVE_USER_HAS_CHECKOUTS_EXCEPTION);
        }

        checkoutService.remove(checkout.getId());
        avService.remove(av.getId());
    }

    @Test
    public void getByIdReturnUser() throws Exception {
        userService.add(user);
        assertEquals(userService.getById(user.getId()), user);
    }

    @Test
    public void getByIdReturnNull() {
        assertNull(userService.getById(user.getId()));
    }

    @Test
    public void checkValidParameters1() {
        try {
            user.setLogin("");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(LOGIN_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters2() {
        try {
            user.setPassword("");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(PASSWORD_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters3() {
        try {
            user.setName("");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(NAME_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters4() {
        try {
            user.setSurname("");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(SURNAME_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters5() {
        try {
            user.setAddress("");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(ADDRESS_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters6() {
        try {
            user.setPhone("");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(PHONE_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters7() {
        try {
            user.setPhone("34567899876543456");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(PHONE_LENGTH_EXCEPTION, e.getMessage());
        }
    }

    @Test
    public void checkValidParameters8() {
        try {
            user.setRole("NOT_ROLE");
            userService.checkValidParameters(user);
        } catch (Exception e) {
            assertEquals(ROLE_EXCEPTION, e.getMessage());
        }
    }
}