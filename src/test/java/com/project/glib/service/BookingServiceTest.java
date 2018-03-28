//package com.project.glib.service;
//
//import com.project.glib.dao.implementations.*;
//import com.project.glib.model.*;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class BookingServiceTest {
//    private final BookDaoImplementation bookDao;
//    private final JournalDaoImplementation journalDao;
//    private final AudioVideoDaoImplementation avDao;
//    private final BookingDaoImplementation bookingDao;
//    private final UsersDaoImplementation usersDao;
//    private final DocumentPhysicalDaoImplementation docPhysDao;
//    private final CheckoutDaoImplementation checkoutDao;
//    private final String FAIL = "TEST FAILED";
//
//    private BookingService bookingService;
//    private User user;
//    private Book book;
//    private Journal journal;
//    private AudioVideo audioVideo;
//    private DocumentPhysical doc1, doc2, doc3;
//    private Booking booking;
//    private Checkout checkout;
//    private long currentTime;
//    private long userId, bookId, journalId, aVId, docId1, docId2, docId3, bookingId, checkountId;
//
//    @Autowired
//    public BookingServiceTest(BookDaoImplementation bookDao,
//                              JournalDaoImplementation journalDao,
//                              AudioVideoDaoImplementation avDao,
//                              BookingDaoImplementation bookingDao,
//                              UsersDaoImplementation usersDao,
//                              DocumentPhysicalDaoImplementation docPhysDao,
//                              CheckoutDaoImplementation checkoutDao) {
//        this.bookDao = bookDao;
//        this.journalDao = journalDao;
//        this.avDao = avDao;
//        this.bookingDao = bookingDao;
//        this.usersDao = usersDao;
//        this.docPhysDao = docPhysDao;
//        this.checkoutDao = checkoutDao;
//    }
//
//    @BeforeClass
//    public void setupClass() {
//        currentTime = System.nanoTime();
//        bookingService = new BookingService(bookDao,
//                journalDao, avDao, bookingDao, usersDao,
//                docPhysDao, checkoutDao);
//        user = new User("login", "password", "conf_password",
//                "name", "surname", "address",
//                "71231233434", User.STUDENT, false);
//        book = new Book("title", "author", "publisher",
//                "edition", 2017, Document.DEFAULT_NOTE,
//                100, 2);
//        journal = new Journal("title", "author", "name",
//                4, "editor", Document.REFERENCE, 100, 3);
//        audioVideo = new AudioVideo("title", "author", 100, 1);
//        doc1 = new DocumentPhysical("SH", true, 0, Document.BOOK, null);
//        doc2 = new DocumentPhysical("SH", true, 0, Document.JOURNAL, null);
//        doc3 = new DocumentPhysical("SH", true, 0, Document.AV, null);
//        booking = new Booking(0, 0, Document.BOOK, "SH", currentTime, false, 0);
//        checkout = new Checkout(0, 0, Document.BOOK, currentTime, currentTime + 1, false, "SH");
//    }
//
//    @Before
//    public void setup() throws Exception {
//        try {
//            usersDao.add(user);
//            bookDao.add(book);
//            journalDao.add(journal);
//            avDao.add(audioVideo);
//            docPhysDao.add(doc1);
//            docPhysDao.add(doc2);
//            docPhysDao.add(doc3);
//            bookingDao.add(booking);
//            checkoutDao.add(checkout);
//        } catch (Exception e) {
//            System.out.println(FAIL);
//            e.printStackTrace();
//        }
//
//        userId = usersDao.getIdByLogin(user.getLogin());
//        bookId = bookDao.getId(book);
//        journalId = journalDao.getId(journal);
//        aVId = avDao.getId(audioVideo);
//        docId1 = docPhysDao.getIdByDocument(bookId, Document.BOOK);
//        docId2 = docPhysDao.getIdByDocument(journalId, Document.JOURNAL);
//        docId3 = docPhysDao.getIdByDocument(aVId, Document.AV);
//        bookingId = bookingDao.getId(booking);
//        checkountId = checkoutDao.getId(checkout);
//    }
//
//    @After
//    public void teamDown() {
//        try {
//            usersDao.remove(userId);
//            bookDao.remove(bookId);
//            journalDao.remove(journalId);
//            avDao.remove(aVId);
//            docPhysDao.remove(docId1);
//            docPhysDao.remove(docId2);
//            docPhysDao.remove(docId3);
//            bookingDao.remove(bookingId);
//            checkoutDao.remove(checkountId);
//        } catch (Exception e) {
//            System.out.println(FAIL);
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void toBookDocument() throws Exception {
//        try {
//            bookingService.toBookDocument(docId1, doc1.getDocType(), userId);
//        } catch (Exception e) {
//            assertTrue(e.getMessage().equals(BookingService.AUTH_EXCEPTION));
//        }
//
//        user.setAuth(true);
//        usersDao.update(user);
//
//        // TODO test for queue
//
//        assertTrue(alreadyHasThisBookingException(doc1, docId1, userId));
//        assertTrue(alreadyHasThisBookingException(doc2, docId2, userId));
//        assertTrue(alreadyHasThisBookingException(doc3, docId3, userId));
//    }
//
//    public boolean alreadyHasThisBookingException(DocumentPhysical doc, long docId, long userId) throws Exception {
//        booking.setDocPhysId(docId);
//        booking.setDocType(doc.getDocType());
//        booking.setUserId(userId);
//        bookingDao.update(booking);
//
//        try {
//            bookingService.toBookDocument(docId, doc.getDocType(), userId);
//        } catch (Exception e) {
//            return e.getMessage().equals(BookingService.ALREADY_HAS_THIS_BOOKING_EXCEPTION + doc.getDocType().toLowerCase());
//        }
//        return false;
//    }
//
//    public boolean alreadyHasThisCheckoutException(DocumentPhysical doc, long docId, long userId) throws Exception {
//        checkout.setDocPhysId(docId);
//        checkout.setDocType(doc.getDocType());
//        checkout.setUserId(userId);
//        checkoutDao.update(checkout);
//
//        try {
//            bookingService.toBookDocument(docId, doc.getDocType(), userId);
//        } catch (Exception e) {
//            return e.getMessage().equals(BookingService.ALREADY_HAS_THIS_CHECKOUT_EXCEPTION +
//                    BookingService.RENEW + doc.getDocType().toLowerCase());
//        }
//        return false;
//    }
//
//    @Test
//    public void recalculatePriority() {
//
//    }
//
//    @Test
//    public void toBookDocument1() {
//    }
//
//    @Test
//    public void setBookingActiveToTrue() {
//    }
//
//    @Test
//    public void numberOfBookedDocumentsByUser() {
//    }
//
//    @Test
//    public void getBookingsByUser() {
//    }
//}
