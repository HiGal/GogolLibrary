package com.project.glib.service;

import com.project.glib.dao.implementations.MessageDaoImplementation;
import com.project.glib.model.Booking;
import com.project.glib.model.Checkout;
import com.project.glib.model.Messages;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    private Messages m1, m2, m3, m4;
    private MessageService messageService;
    private MessageDaoImplementation messageDao;
    private Booking booking;
    private BookService bookService;
    private CheckoutService checkoutService;
    private Checkout checkout;
    private ReturnService returnService;

    @Before
    public void setUp() throws Exception {
        m1 = new Messages(1, MessageService.CHECKOUT_DOCUMENT, 4, "BOOK", false);
        m2 = new Messages(2, MessageService.RETURN_DOCUMENT, 5, "BOOK", false);
        m3 = new Messages(3, MessageService.LATE_DELETED, 6, "BOOK", true);
        messageService.addMes(1, 4, "BOOK", MessageService.CHECKOUT_DOCUMENT);
        messageService.addMes(2, 5, "BOOK", MessageService.RETURN_DOCUMENT);
        messageService.addMes(3, 6, "BOOK", MessageService.LATE_DELETED);
        booking = new Booking(1, 1, "BOOK", 4, 125, true, 20000, "ASD");
        checkout = new Checkout(2, 5, 884144, 5541114, "ASDL");
    }

    @After
    public void tearDown() {
        try {
            messageService.removeAllByUserID(1);
            messageService.removeAllByUserID(2);
            messageService.removeAllByUserID(3);
            bookService.remove(1);
            checkoutService.remove(1);
            checkoutService.remove(2);
        } catch (Exception ignore) {
        }
    }

    @Test(expected = NullPointerException.class)
    public void emulateCheckout() throws Exception {
        try {
            checkoutService.toCheckoutDocument(booking);
        } catch (Exception ignore) {
        }
        messageDao.getById(1);
    }

    @Test(expected = NullPointerException.class)
    public void emulateReturn() throws Exception {
        try {
            returnService.toReturnDocument(checkout);
        } catch (Exception ignore) {
        }
        messageDao.getById(2);
    }

    @Test(expected = NullPointerException.class)
    public void deleteLazyMes() throws Exception {
        messageService.deleteReadMes();
        messageDao.getById(3);
    }
}