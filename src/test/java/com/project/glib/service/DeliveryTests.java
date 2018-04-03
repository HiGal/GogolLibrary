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

import java.util.List;

import static org.junit.Assert.assertNotEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryTests {

    private Book b1, b2;
    private AudioVideo av3;
    private User p1, p2, p3, st, vp, lib;
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
    public void setup() {
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

        try {
            bookService.add(b1, "SH1");
            bookService.add(b1, "SH2");
            bookService.add(b1, "SH3");
            bookService.add(b2, "SH1");
            bookService.add(b2, "SH2");
            bookService.add(b2, "SH3");
            audioVideoService.add(av3, "SH4");
            audioVideoService.add(av3, "SH5");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        try {
            userService.add(p1);
            userService.add(p2);
            userService.add(p3);
            userService.add(st);
            userService.add(lib);
            userService.add(vp);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @After
    public void tearDown() {
        try {
            bookService.remove(bookService.getId(b1));
            bookService.remove(bookService.getId(b2));
            audioVideoService.remove(audioVideoService.getId(av3));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            userService.remove(userService.getId(p1));
            userService.remove(userService.getId(p2));
            userService.remove(userService.getId(p3));
            userService.remove(userService.getId(st));
            userService.remove(userService.getId(vp));
            userService.remove(userService.getId(lib));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        try {
            bookingService.deleteAllBookings();
            checkoutService.deleteAllCheckouts();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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
            checkout.setReturnTime(checkout.getCheckoutTime()-11000000);
            checkoutService.update(checkout);
        }

        List<Checkout> ch = checkoutService.getCheckoutsByUser(id);

        for (Checkout aCh : ch) {
            Pair<Checkout, Integer> pair = returnService.toReturnDocument(aCh);
            j = j + pair.getValue();
        }

        assertNotEquals(0, j);
    }
}
