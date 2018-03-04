package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Checkout;
import com.project.glib.model.Document;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnService {
    public static final int PENNY = 100;

    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final CheckoutDaoImplementation checkoutDao;

    @Autowired
    public ReturnService(BookDaoImplementation bookDao,
                           JournalDaoImplementation journalDao,
                           AudioVideoDaoImplementation avDao,
                           DocumentPhysicalDaoImplementation docPhysDao,
                           CheckoutDaoImplementation checkoutDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
        this.checkoutDao = checkoutDao;
    }

    public Pair<String, Integer> toReturnDocument(Checkout checkout) {
        String checkoutInfo = checkout.toString();
        checkoutDao.remove(checkout.getId());
        return new Pair<>(checkoutInfo, getOverdue(checkout));
    }

    /**
     * get overdue for current check out
     * @param checkout current check out
     * @return overdue
     */
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

    /**
     * get total overdue by current user
     * @param userId id of current user
     * @return total overdue
     */
    public int getTotalOverdueByUser(long userId) {
        int totalOverdue = 0;
        for (Checkout currentCheckout : checkoutDao.getCheckoutsByUser(userId)) {
            totalOverdue += getOverdue(currentCheckout);
        }

        return totalOverdue;
    }

    /**
     * convert nanoseconds to days in integer value
     * @param nanoseconds nanoseconds
     * @return integers days in nanoseconds
     */
    private int convertToDays(long nanoseconds) {
        return (int) nanoseconds / 1000 / 1000 / 1000 / 60 / 60 / 24;
    }
}
