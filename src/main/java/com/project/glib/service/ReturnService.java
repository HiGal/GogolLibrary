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
    private final CheckoutDaoImplementation checkoutDao;
    private final UsersDaoImplementation usersDao;

    @Autowired
    public ReturnService(BookDaoImplementation bookDao,
                         JournalDaoImplementation journalDao,
                         AudioVideoDaoImplementation avDao,
                         DocumentPhysicalDaoImplementation docPhysDao,
                         CheckoutDaoImplementation checkoutDao,
                         UsersDaoImplementation usersDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
        this.checkoutDao = checkoutDao;
        this.usersDao = usersDao;
    }

    public Pair<Checkout, Integer> toReturnDocument(Checkout checkout) throws Exception {
        checkoutDao.remove(checkout.getId());
        long docId = docPhysDao.getDocIdByID(checkout.getIdDoc());
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
     * @throws Exception
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
