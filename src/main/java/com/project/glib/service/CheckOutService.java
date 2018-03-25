package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckOutService {
    public static final long WEEK_IN_MILLISECONDS = 604800000000000L;

    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final BookingDaoImplementation bookingDao;
    private final CheckoutDaoImplementation checkoutDao;
    private final UsersDaoImplementation usersDao;

    @Autowired
    public CheckOutService(BookDaoImplementation bookDao,
                           JournalDaoImplementation journalDao,
                           AudioVideoDaoImplementation avDao,
                           DocumentPhysicalDaoImplementation docPhysDao,
                           BookingDaoImplementation bookingDao,
                           CheckoutDaoImplementation checkoutDao,
                           UsersDaoImplementation usersDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
        this.bookingDao = bookingDao;
        this.checkoutDao = checkoutDao;
        this.usersDao = usersDao;
    }

    /**
     * check out current booking
     * @param booking current booking
     * @return checkout if it  possible
     */
    public Checkout toCheckoutDocument(Booking booking) throws Exception {
        long additionalTime;

        if (booking.getDocType().equals(Document.BOOK)) {
            switch (usersDao.getTypeById(booking.getIdUser())) {
                case User.INSTRUCTOR:
                case User.TA:
                case User.PROFESSOR:
                    additionalTime = 4 * WEEK_IN_MILLISECONDS;
                    break;
                case User.STUDENT:
                    long bookId = docPhysDao.getIdByDocument(booking.getIdDoc(), booking.getDocType());
                    if (bookDao.getNote(bookId).equals(Book.BESTSELLER)) {
                        additionalTime = 2 * WEEK_IN_MILLISECONDS;
                    } else {
                        additionalTime = 3 * WEEK_IN_MILLISECONDS;
                    }
                    break;
                default:
                    throw new Exception("Sorry, but you try to check out invalid type of " +
                            "document (" + booking.getDocType().toLowerCase() + ")" +
                            "maybe it program mistake.");
            }
        } else {
            additionalTime = 2 * WEEK_IN_MILLISECONDS;
        }

        Checkout newCheckout = new Checkout(booking.getIdUser(), booking.getIdDoc(),
                booking.getDocType(), System.nanoTime(), System.nanoTime() + additionalTime,
                false, booking.getShelf());
        bookingDao.remove(booking.getId());
        checkoutDao.add(newCheckout);

        return newCheckout;
    }

    /**
     * get number of check out documents by current user
     * @param userId id of current user
     * @return number of check out
     */
    public long numberOfCheckoutDocumentsByUser(long userId) throws Exception {
        return checkoutDao.getNumberOfCheckoutDocumentsByUser(userId);
    }

    /**
     * get all check outs by current user
     * @param userId id of current user
     * @return array of check outs
     */
    public List<Checkout> getCheckoutsByUser(long userId) throws Exception {
        return checkoutDao.getCheckoutsByUser(userId);
    }
}
