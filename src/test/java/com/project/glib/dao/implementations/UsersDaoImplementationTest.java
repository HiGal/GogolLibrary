package com.project.glib.dao.implementations;

import com.project.glib.model.*;
import com.project.glib.service.CheckOutService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UsersDaoImplementationTest {
    @Autowired
    private BookDaoImplementation bookDao;
    @Autowired
    private JournalDaoImplementation journalDao;
    @Autowired
    private AudioVideoDaoImplementation avDao;
    @Autowired
    private DocumentPhysicalDaoImplementation docPhysDao;
    @Autowired
    private CheckoutDaoImplementation checkoutDao;
    @Autowired
    private UsersDaoImplementation usersDao;
    private User user;
    private String shelf;


    @Before
    public void setUp() {
        user = new User("login", "password", "confpass", "name",
                "surname", "address", "78953534634", User.STUDENT, true);
        shelf = "SHELF";
    }

    @After
    public void tearDown() {
        try {
            usersDao.remove(user.getId());
        } catch (Exception ignore) {
        }
    }

    @Test
    public void addNotExistedUser() throws Exception {
        usersDao.add(user);
        assertEquals(usersDao.getById(user.getId()), user);
    }

    @Test(expected = Exception.class)
    public void addExistedUser() throws Exception {
        usersDao.add(user);
        usersDao.add(user);
    }

    @Test(expected = Exception.class)
    public void addUserWithExistedLogin() throws Exception {
        usersDao.add(user);
        usersDao.add(new User(user.getLogin(), "pass", "cnfrpass", "n",
                "surn", "ad", "ph", User.PROFESSOR, true));
    }

    @Test
    public void update() throws Exception {
        usersDao.add(user);
        // do smth with user
        usersDao.update(user);
        assertEquals(usersDao.getById(user.getId()), user);
    }

    @Test
    public void removeSuccessful() throws Exception {
        usersDao.add(user);
        usersDao.remove(user.getId());
        assertNull(usersDao.getById(user.getId()));
    }

    @Test
    @Ignore
    public void removeHasCheckout1() throws Exception {
        usersDao.add(user);
        Book book = new Book("title", "author", "publisher",
                "edition", 2017, Document.DEFAULT_NOTE, 100, 2);
        bookDao.add(book, shelf);
        DocumentPhysical docPhys = docPhysDao.getById(docPhysDao.getValidPhysicalId(book.getId(), Document.BOOK));
        Checkout checkout = new Checkout(user.getId(), docPhys.getId(), docPhys.getDocType(),
                System.nanoTime(), System.nanoTime() + CheckOutService.WEEK_IN_MILLISECONDS * 3,
                false, docPhys.getShelf());
        checkoutDao.add(checkout);

        try {
            usersDao.remove(user.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), UsersDaoImplementation.REMOVE_USER_HAS_CHECKOUTS);
        }

        checkoutDao.remove(checkout.getId());
    }

    @Test
    @Ignore
    public void removeHasCheckout2() throws Exception {
        usersDao.add(user);
        Journal journal = new Journal("title", "author", "name", 2,
                "editor", Document.REFERENCE, 200, 3);
        journalDao.add(journal, shelf);
        DocumentPhysical docPhys = docPhysDao.getById(docPhysDao.getValidPhysicalId(journal.getId(), Document.JOURNAL));
        Checkout checkout = new Checkout(user.getId(), docPhys.getId(), docPhys.getDocType(),
                System.nanoTime(), System.nanoTime() + CheckOutService.WEEK_IN_MILLISECONDS * 3,
                false, docPhys.getShelf());
        checkoutDao.add(checkout);

        try {
            usersDao.remove(user.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), UsersDaoImplementation.REMOVE_USER_HAS_CHECKOUTS);
        }

        checkoutDao.remove(checkout.getId());
    }

    @Test
    @Ignore
    public void removeHasCheckout3() throws Exception {
        usersDao.add(user);
        AudioVideo av = new AudioVideo("title", "author", 400, 2);
        avDao.add(av, shelf);
        DocumentPhysical docPhys = docPhysDao.getById(docPhysDao.getValidPhysicalId(av.getId(), Document.AV));
        Checkout checkout = new Checkout(user.getId(), docPhys.getId(), docPhys.getDocType(),
                System.nanoTime(), System.nanoTime() + CheckOutService.WEEK_IN_MILLISECONDS * 3,
                false, docPhys.getShelf());
        checkoutDao.add(checkout);

        try {
            usersDao.remove(user.getId());
        } catch (Exception e) {
            assertEquals(e.getMessage(), UsersDaoImplementation.REMOVE_USER_HAS_CHECKOUTS);
        }

        checkoutDao.remove(checkout.getId());
    }

    @Test
    public void getByIdReturnUser() throws Exception {
        usersDao.add(user);
        assertEquals(usersDao.getById(user.getId()), user);
    }

    @Test
    public void getByIdReturnNull() {
        assertNull(usersDao.getById(user.getId()));
    }

    @Test(expected = Exception.class)
    public void checkValidParameters1() throws Exception {
        user.setLogin("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters2() throws Exception {
        user.setPassword("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters3() throws Exception {
        user.setPasswordConfirm("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters4() throws Exception {
        user.setName("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters5() throws Exception {
        user.setSurname("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters6() throws Exception {
        user.setAddress("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters7() throws Exception {
        user.setPhone("");
        usersDao.checkValidParameters(user);
    }

    @Test(expected = Exception.class)
    public void checkValidParameters8() throws Exception {
        user.setRole("NOT_ROLE");
        usersDao.checkValidParameters(user);
    }
}