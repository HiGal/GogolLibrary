package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Checkout;
import com.project.glib.model.Document;
import com.project.glib.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class CheckOutService {
    public final long FOUR_WEEKS = 2419200000000000L;
    public final long THREE_WEEKS = 1814400000000000L;
    public final long TWO_WEEKS = 1209600000000000L;
    public final int PENNY = 100;

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

    public boolean toCheckoutDocument(Booking booking) {
        long additionalTime;

        if (booking.getDoc_type().equals(Document.BOOK)) {
            switch (usersDao.getTypeById(booking.getId_user())) {
                case User.FACULTY:
                    additionalTime = FOUR_WEEKS;
                    break;
                case User.PATRON:
                    long bookId = docPhysDao.getIdByDocument(booking.getId_doc(), booking.getDoc_type());
                    if (bookDao.getIsBestseller(bookId)) {
                        additionalTime = TWO_WEEKS;
                    } else {
                        additionalTime = THREE_WEEKS;
                    }
                    break;
                default:
                    additionalTime = 0;
                    return false;
            }
        } else {
            additionalTime = TWO_WEEKS;
        }

        checkoutDao.add(new Checkout(booking.getId_user(), booking.getId_doc(),
                booking.getDoc_type(), System.nanoTime(), System.nanoTime() + additionalTime,
                false, booking.getShelf()));
        bookingDao.remove(booking.getId());

        return true;
    }

    public int toReturnDocument(Checkout checkout) {
        int payoff = 0;
        long difference = checkout.getCheckout_time() - System.nanoTime();
        if (difference < 0) {
            int days = convertToDays(difference);
            int price;
            switch (checkout.getDoc_type()) {
                case Document.BOOK:
                    long bookId = docPhysDao.getIdByDocument(checkout.getId_doc(), checkout.getDoc_type());
                    price = bookDao.getPriceById(bookId);
                    break;
                case Document.JOURNAL:
                    long journalId = docPhysDao.getIdByDocument(checkout.getId_doc(), checkout.getDoc_type());
                    price = journalDao.getPriceById(journalId);
                    break;
                case Document.AV:
                    long avId = docPhysDao.getIdByDocument(checkout.getId_doc(), checkout.getDoc_type());
                    price = avDao.getPriceById(avId);
                    break;
                default:
                    return payoff;
            }
            payoff = Math.max(days * PENNY, price);
        }

        checkoutDao.remove(checkout.getId());

        return payoff;
    }

    private int convertToDays(long nanoseconds) {
        return (int) nanoseconds / 1000 / 1000 / 1000 / 60 / 60 / 24;
    }
}
