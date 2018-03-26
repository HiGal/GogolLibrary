package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Checkout;
import com.project.glib.model.Document;
import com.project.glib.model.User;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReturnService {
    public static final int PENNY = 100;

    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final BookingDaoImplementation bookingDao;
    private final CheckoutDaoImplementation checkoutDao;
    private final UsersDaoImplementation usersDao;
    private final BookingService bookingService;
    private final MessageDaoImplementation messageDao;

    @Autowired
    public ReturnService(BookDaoImplementation bookDao,
                         JournalDaoImplementation journalDao,
                         AudioVideoDaoImplementation avDao,
                         DocumentPhysicalDaoImplementation docPhysDao,
                         BookingDaoImplementation bookingDao,
                         CheckoutDaoImplementation checkoutDao,
                         UsersDaoImplementation usersDao,
                         BookingService bookingService, MessageDaoImplementation messageDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
        this.bookingDao = bookingDao;
        this.checkoutDao = checkoutDao;
        this.usersDao = usersDao;
        this.bookingService = bookingService;
        this.messageDao = messageDao;
    }

    public Pair<Checkout, Integer> toReturnDocument(Checkout checkout) throws Exception {
        checkoutDao.remove(checkout.getId());

        messageDao.removeOneByUserID(checkout.getIdUser(), checkout.getIdDoc());

        long docId = docPhysDao.getDocIdByID(checkout.getIdDoc());
        String docType = checkout.getDocType();
        if (bookingDao.hasNotActiveBooking(docId, docType)) {
            docPhysDao.inverseCanBooked(checkout.getIdDoc());
            switch (checkout.getDocType()) {
                case Document.BOOK:
                    bookDao.incrementCountById(docId);
                    break;
                case Document.JOURNAL:
                    journalDao.incrementCountById(docId);
                    break;
                case Document.AV:
                    avDao.incrementCountById(docId);
                    break;
                default:
                    return null;
            }
        } else {
            bookingService.setBookingActiveToTrue(bookingDao.getBookingWithMaxPriority(docId, docType));

            // message takes id of virtual book and doc type
            long docID = docPhysDao.getDocIdByID(docId);
            String type = docPhysDao.getTypeByID(docId);
            messageDao.addMes(
                    bookingDao.getBookingWithMaxPriority(docID, type).getIdUser(),
                    docID,
                    MessageDaoImplementation.CHECKOUT_DOCUMENT,
                    type);
        }
        return new Pair<>(checkout, getOverdue(checkout));
    }

    /**
     * get overdue for current check out
     *
     * @param checkout current check out
     * @return overdue
     */
    public int getOverdue(Checkout checkout) throws Exception {
        int overdue = 0;
        long difference = checkout.getReturnTime() - System.nanoTime();
        if (difference < 0) {
            int days = convertToDays(difference);
            int price;
            switch (checkout.getDocType()) {
                case Document.BOOK:
                    long bookId = docPhysDao.getDocIdByPhysDocument(checkout.getIdDoc());
                    price = bookDao.getPriceById(bookId);
                    break;
                case Document.JOURNAL:
                    long journalId = docPhysDao.getDocIdByPhysDocument(checkout.getIdDoc());
                    price = journalDao.getPriceById(journalId);
                    break;
                case Document.AV:
                    long avId = docPhysDao.getDocIdByPhysDocument(checkout.getIdDoc());
                    price = avDao.getPriceById(avId);
                    break;
                default:
                    return overdue;
            }
            overdue = Math.min(days * PENNY, price);
        }
        return overdue;
    }

    public int getOverdueDays(Checkout checkout) {
        int days = 0;
        long difference = checkout.getReturnTime() - System.nanoTime();
        if (difference < 0) {
            days = convertToDays(difference);
        }
        return days;
    }

    /**
     * get total overdue by current user
     *
     * @param userId id of current user
     * @return total overdue
     */
    public int getTotalOverdueByUser(long userId) throws Exception {
        int totalOverdue = 0;
        for (Checkout currentCheckout : checkoutDao.getCheckoutsByUser(userId)) {
            totalOverdue += getOverdue(currentCheckout);
        }

        return totalOverdue;
    }

    /**
     * get list of pairs<User, Integer> with positive total overdue
     *
     * @return list of pairs<User, Integer>
     */
    public List<Pair<User, Integer>> getListOfTotalOverdue() throws Exception {
        List<Pair<User, Integer>> listOverdue = new ArrayList<>();
        List<Checkout> checkouts = checkoutDao.getList();

        for (Checkout checkout : checkouts) {
            long userId = checkout.getIdUser();
            if (getTotalOverdueByUser(userId) > 0) {
                listOverdue.add(new Pair<>(usersDao.getById(userId), getTotalOverdueByUser(userId)));
            }
        }

        for (Pair<User, Integer> pair : listOverdue) {
            System.out.println("User : " + pair.getKey() + " has overdue : " + pair.getValue());
        }

        return listOverdue;
    }

    /**
     * convert nanoseconds to days in integer value
     *
     * @param nanoseconds nanoseconds
     * @return integers days in nanoseconds
     */
    private int convertToDays(long nanoseconds) {
        return (int) nanoseconds / 1000 / 1000 / 1000 / 60 / 60 / 24;
    }
}
