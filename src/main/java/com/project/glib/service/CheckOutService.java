package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Checkout;
import com.project.glib.model.Document;
import com.project.glib.model.User;
import javafx.util.Pair;
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

    public Checkout toCheckoutDocument(Booking booking) {
        long additionalTime;

        if (booking.getDocType().equals(Document.BOOK)) {
            switch (usersDao.getTypeById(booking.getIdUser())) {
                case User.FACULTY:
                    additionalTime = FOUR_WEEKS;
                    break;
                case User.STUDENT:
                    long bookId = docPhysDao.getIdByDocument(booking.getIdDoc(), booking.getDocType());
                    if (bookDao.getIsBestseller(bookId)) {
                        additionalTime = TWO_WEEKS;
                    } else {
                        additionalTime = THREE_WEEKS;
                    }
                    break;
                default:
                    return null;
            }
        } else {
            additionalTime = TWO_WEEKS;
        }

        Checkout newCheckout = new Checkout(booking.getIdUser(), booking.getIdDoc(),
                booking.getDocType(), System.nanoTime(), System.nanoTime() + additionalTime,
                false, booking.getShelf());
        bookingDao.remove(booking.getId());
        checkoutDao.add(newCheckout);

        return newCheckout;
    }

    //TODO check rules to renew document
    public Checkout toRenewDocument(Checkout checkout) {
        if (checkoutDao.getIsRenewedById(checkout.getId())) {
            return null;
        }

        checkout.setRenewed(true);
        checkout.setReturnTime(2 * checkout.getReturnTime() - checkout.getCheckoutTime());
        return checkout;
    }

    public Pair<String, Integer> toReturnDocument(Checkout checkout) {
        String checkoutInfo = checkout.toString();
        checkoutDao.remove(checkout.getId());
        return new Pair<>(checkoutInfo, getOverdue(checkout));
    }

    private int getOverdue(Checkout checkout) {
        int overdue = 0;
        long difference = checkout.getCheckoutTime() - System.nanoTime();
        if (difference < 0) {
            int days = convertToDays(difference);
            int price;
            switch (checkout.getDocType()) {
                case Document.BOOK:
                    long bookId = docPhysDao.getIdByDocument(checkout.getIdDoc(), checkout.getDocType());
                    price = bookDao.getPriceById(bookId);
                    break;
                case Document.JOURNAL:
                    long journalId = docPhysDao.getIdByDocument(checkout.getIdDoc(), checkout.getDocType());
                    price = journalDao.getPriceById(journalId);
                    break;
                case Document.AV:
                    long avId = docPhysDao.getIdByDocument(checkout.getIdDoc(), checkout.getDocType());
                    price = avDao.getPriceById(avId);
                    break;
                default:
                    return overdue;
            }
            overdue = Math.min(days * PENNY, price);
        }
        return overdue;
    }

    public long numberOfCheckoutDocumentsByUser(long userId) {
        return checkoutDao.getNumberOfCheckoutDocumentsByUser(userId);
    }

    public int getTotalOverdueByUser(long userId) {
        int totalOverdue = 0;
        for (Checkout currentCheckout : checkoutDao.getCheckoutsByUser(userId)) {
            totalOverdue += getOverdue(currentCheckout);
        }

        return totalOverdue;
    }

    public Checkout[] getCheckoutsByUser(long userId) {
        return checkoutDao.getCheckoutsByUser(userId);
    }

    private int convertToDays(long nanoseconds) {
        return (int) nanoseconds / 1000 / 1000 / 1000 / 60 / 60 / 24;
    }
}
