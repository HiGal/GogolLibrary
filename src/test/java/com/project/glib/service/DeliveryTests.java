package com.project.glib.service;

import com.project.glib.model.*;
import javafx.util.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.project.glib.service.CheckoutService.WEEK_IN_MILLISECONDS;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryTests {

    private Book b1, b2;
    private AudioVideo av3;
    private User p1, p2, p3, p4, st, vp, lib;
    private Checkout checkout1, checkout2;
    private Booking booking1, booking2;
    @Autowired
    private DocumentPhysicalService documentPhysicalService;
    @Autowired
    private BookService bookService;
    @Autowired
    private AudioVideoService audioVideoService;
    @Autowired
    private UserService userService;
    @Autowired
    private BookingService bookingService;
    @Autowired
    private CheckoutService checkoutService;
    @Autowired
    private ReturnService returnService;

    @Before
    public void setup() throws Exception {
        p1 = new User("s.afonso", "123",
                "Sergey", "Afonso",
                "Via Margutta, 3", "12345678900", User.PROFESSOR,
                true, "img");
        p2 = new User("n.teixeira", "123",
                "Nadia", "Teixeira",
                "Via Sacra, 13", "12345678900", User.PROFESSOR,
                true, "img");
        p3 = new User("e.espindola", "123",
                " Elvira", "Espindola",
                "Via del Corso, 22", "12345678900", User.PROFESSOR,
                true, "img");
        p4 = new User("i.person", "123",
                "Important", "Person",
                "Via del Corso, 22", "12345678900", User.PROFESSOR,
                true, "img");
        st = new User("a.velo", "123",
                "Andrey", "Velo",
                "Avenida Mazatlan 250", "12345678900", User.STUDENT,
                true, "img");
        vp = new User("v.rama", "123",
                "Veronika", "Rama",
                "Stret Atocha, 27", "12345678900", User.PROFESSOR_VISITING,
                true, "img");
        lib = new User("h.aslam", "123",
                "Hamna", "Aslam",
                "Kazan", "12345678900", User.LIBRARIAN,
                true, "img");


        userService.add(p1);
        userService.add(p2);
        userService.add(p3);
        userService.add(p4);
        userService.add(st);
        userService.add(lib);
        userService.add(vp);

        b1 = new Book("Introduction to Algorithms",
                "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest and Clifford Stein",
                "MIT Press",
                "Third edition",
                2009,
                Book.DEFAULT_NOTE, 5000, 1, "img");
        b2 = new Book("Design Patterns: Elements of Reusable Object-Oriented Software",
                "Erich Gamma, Ralph Johnson, John Vlissides, Richard Helm",
                "Addison-Wesley Professional",
                "First edition",
                2003,
                Book.BESTSELLER, 1700, 1, "img");
        av3 = new AudioVideo("Null References: The Billion Dollar Mistake",
                "Tony Hoare", 700, 1, "img");


        bookService.add(b1, "SH1");
        bookService.add(b1, "SH2");
        bookService.add(b1, "SH3");
        bookService.add(b2, "SH1");
        bookService.add(b2, "SH2");
        bookService.add(b2, "SH3");

        audioVideoService.add(av3, "SH4");
        audioVideoService.add(av3, "SH5");


    }


    @After
    public void tearDown() throws Exception {
        bookingService.deleteAllBookings();
        checkoutService.deleteAllCheckouts();

        bookService.remove(bookService.getId(b1));
        bookService.remove(bookService.getId(b2));
        audioVideoService.remove(audioVideoService.getId(av3));

        userService.remove(userService.getId(p1));
        userService.remove(userService.getId(p2));
        userService.remove(userService.getId(p3));
        userService.remove(userService.getId(p4));
        userService.remove(userService.getId(st));
        userService.remove(userService.getId(vp));
        userService.remove(userService.getId(lib));


    }

    @Test
    public void test1() throws Exception {
        int j = 0;

        long id = userService.getId(p1);

        bookingService.toBookDocument(bookService.getId(b1),
                Document.BOOK, id);
        bookingService.toBookDocument(bookService.getId(b2),
                Document.BOOK, id);

        List<Booking> bookings = bookingService.getBookingsByUser(id);

        for (Booking booking : bookings) {
            checkoutService.toCheckoutDocument(booking);
        }

        List<Checkout> checkouts = checkoutService.getCheckoutsByUser(id);

        for (Checkout checkout : checkouts) {
            long time = new Date(118, 2, 7).getTime();
            checkout.setReturnTime(time + 4 * WEEK_IN_MILLISECONDS);
            checkoutService.update(checkout);
        }

        List<Checkout> ch = checkoutService.getCheckoutsByUser(id);

        for (Checkout aCh : ch) {
            Pair<Checkout, Integer> pair = returnService.toReturnDocument(aCh);
            System.out.println("--------------------");
            System.out.println(pair.getValue() + "   " + pair.getKey().getUserId());
            System.out.println("--------------------");
            j = j + pair.getValue();
        }

        assertEquals(0, j);
    }

    @Test
    public void test2() throws Exception {
        int p = 0;
        int s = 0;
        int v = 0;

        long id_p1 = userService.getId(p1);
        long id_s = userService.getId(st);
        long id_v = userService.getId(vp);

        bookingService.toBookDocument(bookService.getId(b1),
                Document.BOOK, id_p1);
        bookingService.toBookDocument(bookService.getId(b2),
                Document.BOOK, id_p1);
        bookingService.toBookDocument(bookService.getId(b1),
                Document.BOOK, id_s);
        bookingService.toBookDocument(bookService.getId(b2),
                Document.BOOK, id_s);
        bookingService.toBookDocument(bookService.getId(b1),
                Document.BOOK, id_v);
        bookingService.toBookDocument(bookService.getId(b2),
                Document.BOOK, id_v);

        List<Booking> bookings_p1 = bookingService.getBookingsByUser(id_p1);
        List<Booking> bookings_s = bookingService.getBookingsByUser(id_s);
        List<Booking> bookings_v = bookingService.getBookingsByUser(id_v);

        for (Booking booking : bookings_p1) {
            checkoutService.toCheckoutDocument(booking);
        }
        for (Booking booking : bookings_s) {
            checkoutService.toCheckoutDocument(booking);
        }
        for (Booking booking : bookings_v) {
            checkoutService.toCheckoutDocument(booking);
        }

        List<Checkout> checkouts_p1 = checkoutService.getCheckoutsByUser(id_p1);
        List<Checkout> checkouts_s = checkoutService.getCheckoutsByUser(id_s);
        List<Checkout> checkouts_v = checkoutService.getCheckoutsByUser(id_v);

        for (Checkout checkout : checkouts_p1) {
            long time = new Date(118, 2, 7).getTime();
            checkout.setReturnTime(time + 4 * WEEK_IN_MILLISECONDS);
            checkoutService.update(checkout);
        }

        for (Checkout checkout : checkouts_s) {
            long time = new Date(118, 2, 7).getTime();
            if (bookService.getById(documentPhysicalService.getDocVirIdById(checkout.getDocPhysId())).getNote().equals(Book.BESTSELLER)) {
                System.out.println("------------------------");
                checkout.setReturnTime(time + 2 * WEEK_IN_MILLISECONDS);
                System.out.println(System.currentTimeMillis());
                System.out.println(checkout.getReturnTime());
                System.out.println("------------------------");
            } else {
                System.out.println("------------------------");
                checkout.setReturnTime(time + 3 * WEEK_IN_MILLISECONDS);
                System.out.println(System.currentTimeMillis());
                System.out.println(checkout.getReturnTime());
                System.out.println("------------------------");

            }
            checkoutService.update(checkout);
        }

        for (Checkout checkout : checkouts_v) {
            long time = new Date(118, 2, 7).getTime();
            checkout.setReturnTime(time + WEEK_IN_MILLISECONDS);
            checkoutService.update(checkout);
        }

        List<Checkout> ch_p1 = checkoutService.getCheckoutsByUser(id_p1);
        List<Checkout> ch_s = checkoutService.getCheckoutsByUser(id_s);
        List<Checkout> ch_v = checkoutService.getCheckoutsByUser(id_v);

        for (Checkout checkout_p1 : ch_p1) {
            Pair<Checkout, Integer> pair = returnService.toReturnDocument(checkout_p1);
            System.out.println("--------------------");
            System.out.println(pair.getValue() + "   " + pair.getKey().getUserId());
            System.out.println("--------------------");
            p = p + pair.getValue();
        }

        for (Checkout checkout_s : ch_s) {
            Pair<Checkout, Integer> pair = returnService.toReturnDocument(checkout_s);
            System.out.println("--------------------");
            System.out.println(pair.getValue() + "   " + pair.getKey().getUserId());
            System.out.println("--------------------");
            s = s + pair.getValue();
        }

        for (Checkout checkout_v : ch_v) {
            Pair<Checkout, Integer> pair = returnService.toReturnDocument(checkout_v);
            System.out.println("--------------------");
            System.out.println(pair.getValue() + "   " + pair.getKey().getUserId());
            System.out.println("--------------------");
            v = v + pair.getValue();
        }

        assertEquals(0, p);
        assertEquals(2100, s);
        assertEquals(3800, v);
    }

    @Test
    public void test5() throws Exception {

        long id_p1 = userService.getId(p1);
        long id_s = userService.getId(st);
        long id_v = userService.getId(vp);

        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_p1);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_s);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_v);

        List<Booking> bookings_p1 = bookingService.getBookingsByUser(id_p1);
        List<Booking> bookings_s = bookingService.getBookingsByUser(id_s);

        for (Booking booking : bookings_p1) {
            checkoutService.toCheckoutDocument(booking);
        }
        for (Booking booking : bookings_s) {
            checkoutService.toCheckoutDocument(booking);
        }
        List<Booking> queue = bookingService.getPriorityQueueByDocVirIdAndDocType(audioVideoService.getId(av3), Document.AV);
        ArrayList<Long> usersQueue = new ArrayList<>();

        System.out.println("-----------------------");
        for (int i = 0; i < queue.size(); i++) {
            System.out.println(queue.get(i));
            usersQueue.add(queue.get(i).getUserId());
        }
        System.out.println("-----------------------");
        ArrayList<Long> quq = new ArrayList<>();
        quq.add(userService.getId(vp));

        assertEquals(quq, usersQueue);
    }

    @Test
    public void test6() throws Exception {

        long id_p1 = userService.getId(p1);
        long id_p2 = userService.getId(p2);
        long id_s = userService.getId(st);
        long id_v = userService.getId(vp);
        long id_p3 = userService.getId(p3);

        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_p1);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_p2);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_s);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_v);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV, id_p3);

        List<Booking> bookings_p1 = bookingService.getBookingsByUser(id_p1);
        List<Booking> bookings_p2 = bookingService.getBookingsByUser(id_p2);

        for (Booking booking : bookings_p1) {
            checkoutService.toCheckoutDocument(booking);
        }
        for (Booking booking : bookings_p2) {
            checkoutService.toCheckoutDocument(booking);
        }

        List<Booking> queue = bookingService.getPriorityQueueByDocVirIdAndDocType(audioVideoService.getId(av3), Document.AV);
        ArrayList<Long> usersQueue = new ArrayList<>();

        System.out.println("-----------------------");
        for (Booking aQueue : queue) {
            System.out.println(aQueue);
            usersQueue.add(aQueue.getUserId());
        }
        System.out.println("-----------------------");

        ArrayList<Long> quq = new ArrayList<>();
        quq.add(userService.getId(st));
        quq.add(userService.getId(vp));
        quq.add(userService.getId(p3));

        assertEquals(quq, usersQueue);
    }

    @Test
    public void test7() throws Exception {
        test6();
        long id_lib = userService.getId(lib);
        long id_p4 = userService.getId(p4);
        bookingService.toBookDocument(audioVideoService.getId(av3),
                Document.AV,
                id_p4);
        bookingService.outstandingRequest(bookingService.getBookingsByUser(id_p4).get(0));

        List<Booking> queue = bookingService.getPriorityQueueByDocVirIdAndDocType(audioVideoService.getId(av3), Document.AV);
        ArrayList<Long> usersQueue = new ArrayList<>();

        System.out.println("-----------------------");
        for (Booking aQueue : queue) {
            System.out.println(aQueue);
            usersQueue.add(aQueue.getUserId());
        }
        System.out.println("-----------------------");

        ArrayList<Long> quq = new ArrayList<>();
        quq.add(userService.getId(p4));

        assertEquals(quq, usersQueue);
    }
}
