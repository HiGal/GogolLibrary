//package com.project.glib.dao.implementations;
//
//import com.project.glib.model.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.List;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BookingDaoImplementationTest {
//    @Autowired
//    private BookingDaoImplementation bookingDao;
//    @Autowired
//    private BookDaoImplementation bookDao;
//    @Autowired
//    private JournalDaoImplementation journalDao;
//    @Autowired
//    private AudioVideoDaoImplementation avDao;
//    @Autowired
//    private UsersDaoImplementation userDao;
//    private Booking booking1;
//    private Booking booking2;
//    private Booking booking3;
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
//        booking1 = new Booking(user.getId(), book.getId(), Document.BOOK,
//                shelf1, System.nanoTime(), false, 0, shelf);
//        booking2 = new Booking(user.getId(), journal.getId(), Document.JOURNAL,
//                shelf1, System.nanoTime(), false, 0, shelf);
//        booking3 = new Booking(user.getId(), av.getId(), Document.AV,
//                shelf1, System.nanoTime(), false, 0, shelf);
//    }
//
//    @After
//    public void tearDown() throws Exception {
//
//        try {
//            bookingDao.remove(booking1.getId());
//        } catch (Exception ignore) {
//        }
//
//        try {
//            bookingDao.remove(booking2.getId());
//        } catch (Exception ignore) {
//        }
//
//        try {
//            bookingDao.remove(booking3.getId());
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
//    public void addNotExistedBooking() throws Exception {
//        bookingDao.add(booking1);
//        assertEquals(bookingDao.getById(booking1.getId()), booking1);
//
//        bookingDao.add(booking2);
//        assertEquals(bookingDao.getById(booking2.getId()), booking2);
//
//        bookingDao.add(booking3);
//        assertEquals(bookingDao.getById(booking3.getId()), booking3);
//    }
//
//    @Test(expected = Exception.class)
//    public void addExistedBooking1() throws Exception {
//        bookingDao.add(booking1);
//        bookingDao.add(booking1);
//    }
//
//    @Test(expected = Exception.class)
//    public void addExistedBooking2() throws Exception {
//        bookingDao.add(booking2);
//        bookingDao.add(booking2);
//    }
//
//    @Test(expected = Exception.class)
//    public void addExistedBooking3() throws Exception {
//        bookingDao.add(booking3);
//        bookingDao.add(booking3);
//    }
//
//    private void update(Booking booking) throws Exception {
//        // don't change count when using update
//        bookingDao.add(booking);
//        // do smth with booking
//        bookingDao.update(booking);
//        assertEquals(bookingDao.getById(booking.getId()), booking);
//    }
//
//    @Test
//    public void update() throws Exception {
//        update(booking1);
//        update(booking2);
//        update(booking3);
//    }
//
//    private void remove(Booking booking) throws Exception {
//        bookingDao.add(booking);
//        bookingDao.remove(booking.getId());
//        assertNull(bookingDao.getById(booking.getId()));
//    }
//
//    @Test
//    public void remove() throws Exception {
//        remove(booking1);
//        remove(booking2);
//        remove(booking3);
//    }
//
//    private void getIdReturnId(Booking booking) throws Exception {
//        bookingDao.add(booking);
//        assertTrue(bookingDao.getId(booking) == booking.getId());
//    }
//
//    @Test
//    public void getIdReturnId() throws Exception {
//        getIdReturnId(booking1);
//        getIdReturnId(booking2);
//        getIdReturnId(booking3);
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException1() throws Exception {
//        bookingDao.getId(booking1);
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException2() throws Exception {
//        bookingDao.getId(booking2);
//    }
//
//    @Test(expected = Exception.class)
//    public void getIdReturnException3() throws Exception {
//        bookingDao.getId(booking3);
//    }
//
//    @Test
//    public void alreadyHasThisBookingTrue() throws Exception {
//        bookingDao.add(booking1);
//        bookingDao.add(booking2);
//        bookingDao.add(booking3);
//        assertTrue(bookingDao
//                .alreadyHasThisBooking(booking1.getDocPhysId(), booking1.getDocType(), booking1.getUserId()));
//        assertTrue(bookingDao
//                .alreadyHasThisBooking(booking2.getDocPhysId(), booking2.getDocType(), booking2.getUserId()));
//        assertTrue(bookingDao
//                .alreadyHasThisBooking(booking3.getDocPhysId(), booking3.getDocType(), booking3.getUserId()));
//
//    }
//
//    @Test
//    public void alreadyHasThisBookingFalse() {
//        assertFalse(bookingDao
//                .alreadyHasThisBooking(booking1.getDocPhysId(), booking1.getDocType(), booking1.getUserId()));
//        assertFalse(bookingDao
//                .alreadyHasThisBooking(booking2.getDocPhysId(), booking2.getDocType(), booking2.getUserId()));
//        assertFalse(bookingDao
//                .alreadyHasThisBooking(booking3.getDocPhysId(), booking3.getDocType(), booking3.getUserId()));
//    }
//
//    @Test
//    public void hasActiveBookingFalse() throws Exception {
//        setBookingsNotActiveAndAdd();
//        assertFalse(bookingDao.hasActiveBooking(booking1.getDocPhysId(), booking1.getDocType()));
//        assertFalse(bookingDao.hasActiveBooking(booking2.getDocPhysId(), booking2.getDocType()));
//        assertFalse(bookingDao.hasActiveBooking(booking3.getDocPhysId(), booking3.getDocType()));
//    }
//
//    @Test
//    public void hasActiveBookingTrue() throws Exception {
//        setBookingsActiveAndAdd();
//        assertTrue(bookingDao.hasActiveBooking(booking1.getDocPhysId(), booking1.getDocType()));
//        assertTrue(bookingDao.hasActiveBooking(booking2.getDocPhysId(), booking2.getDocType()));
//        assertTrue(bookingDao.hasActiveBooking(booking3.getDocPhysId(), booking3.getDocType()));
//    }
//
//    @Test
//    public void hasNotActiveBookingFalse() throws Exception {
//        setBookingsActiveAndAdd();
//        assertFalse(bookingDao.hasNotActiveBooking(booking1.getDocPhysId(), booking1.getDocType()));
//        assertFalse(bookingDao.hasNotActiveBooking(booking2.getDocPhysId(), booking2.getDocType()));
//        assertFalse(bookingDao.hasNotActiveBooking(booking3.getDocPhysId(), booking3.getDocType()));
//    }
//
//    @Test
//    public void hasNotActiveBookingTrue() throws Exception {
//        setBookingsNotActiveAndAdd();
//        assertTrue(bookingDao.hasNotActiveBooking(booking1.getDocPhysId(), booking1.getDocType()));
//        assertTrue(bookingDao.hasNotActiveBooking(booking2.getDocPhysId(), booking2.getDocType()));
//        assertTrue(bookingDao.hasNotActiveBooking(booking3.getDocPhysId(), booking3.getDocType()));
//    }
//
//    private void setBookingsActiveAndAdd() throws Exception {
//        booking1.setActive(true);
//        booking2.setActive(true);
//        booking3.setActive(true);
//
//        bookingDao.add(booking1);
//        bookingDao.add(booking2);
//        bookingDao.add(booking3);
//    }
//
//    private void setBookingsNotActiveAndAdd() throws Exception {
//        booking1.setActive(false);
//        booking2.setActive(false);
//        booking3.setActive(false);
//
//        bookingDao.add(booking1);
//        bookingDao.add(booking2);
//        bookingDao.add(booking3);
//    }
//
//    @Test
//    public void getBookingWithMaxPriority() throws Exception {
//        getBookingWithMaxPriority(booking1);
//        getBookingWithMaxPriority(booking2);
//        getBookingWithMaxPriority(booking3);
//    }
//
//    private void getBookingWithMaxPriority(Booking booking) throws Exception {
//        int startPriority = booking.getPriority();
//        int cycles = 10;
//        int additionalPriority = 100;
//
//        for (int i = 0; i < cycles; i++) {
//            User user1 = new User("login" + i, "password", "passconf",
//                    "name", "surname", "address", "79134562845672", User.STUDENT, true);
//            userDao.add(user1);
//            booking.setUserId(user1.getId());
//            booking.setPriority(booking.getPriority() + additionalPriority);
//            bookingDao.add(booking);
//        }
//
//        Booking bookingWithMaxPriority = bookingDao.getBookingWithMaxPriority(booking.getDocPhysId(), booking.getDocType());
//        assertTrue(bookingWithMaxPriority.getPriority() == startPriority + cycles * additionalPriority);
//
//        for (int i = 0; i < cycles; i++) {
//            long userId = userDao.getIdByLogin("login" + i);
//            List<Booking> list = bookingDao.getBookingsByUser(userId);
//            for (Booking b : list) {
//                bookingDao.remove(b.getId());
//            }
//            userDao.remove(userId);
//        }
//    }
//}