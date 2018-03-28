package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckOutService implements ModifyByLibrarianService<Checkout> {
    public static final long WEEK_IN_MILLISECONDS = 604800000000000L;
    public static final String TYPE = Checkout.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String ALREADY_HAS_THIS_CHECKOUT_EXCEPTION = "Sorry, but your already have this check out ";
    public static final String CHECKOUT_TIME_EXCEPTION = " checkout cannot be in future ";
    public static final String RETURN_TIME_EXCEPTION = " return time cannot be less than checkout time ";
    public static final String DOC_TYPE_EXCEPTION = " invalid document type ";

    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final BookingService bookingService;
    private final CheckoutDaoImplementation checkoutDao;
    private final UsersDaoImplementation usersDao;

    @Autowired
    public CheckOutService(BookDaoImplementation bookDao,
                           JournalDaoImplementation journalDao,
                           AudioVideoDaoImplementation avDao,
                           DocumentPhysicalDaoImplementation docPhysDao,
                           BookingService bookingService,
                           CheckoutDaoImplementation checkoutDao,
                           UsersDaoImplementation usersDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
        this.bookingService = bookingService;
        this.checkoutDao = checkoutDao;
        this.usersDao = usersDao;
    }

    @Override
    public void add(Checkout checkout) throws Exception {
        checkValidParameters(checkout);
        if (checkoutDao.alreadyHasThisCheckout(checkout.getDocPhysId(), checkout.getDocType(), checkout.getUserId()))
            throw new Exception(ALREADY_HAS_THIS_CHECKOUT_EXCEPTION);
        try {
            checkoutDao.add(checkout);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    @Override
    public void update(Checkout checkout) throws Exception {
        checkValidParameters(checkout);
        try {
            checkoutDao.update(checkout);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    // TODO priority queue!!!
    @Override
    public void remove(long checkoutId) throws Exception {
        try {
            checkoutDao.remove(checkoutId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    @Override
    public void checkValidParameters(Checkout checkout) throws Exception {
        if (checkout.getDocPhysId() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }

        if (checkout.getUserId() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }

        if (checkout.getCheckoutTime() > System.nanoTime()) {
            throw new Exception(CHECKOUT_TIME_EXCEPTION);
        }

        if (checkout.getReturnTime() <= checkout.getCheckoutTime()) {
            throw new Exception(RETURN_TIME_EXCEPTION);
        }

        if (!Document.isType(checkout.getDocType())) {
            throw new Exception(DOC_TYPE_EXCEPTION);
        }

        if (checkout.getShelf().equals("")) {
            throw new Exception(SHELF_EXCEPTION);
        }
    }

    /**
     * check out current booking
     * @param booking current booking
     */
    public void toCheckoutDocument(Booking booking) throws Exception {
        long additionalTime;

        if (booking.getDocType().equals(Document.BOOK)) {
            switch (usersDao.getTypeById(booking.getUserId())) {
                case User.INSTRUCTOR:
                case User.TA:
                case User.PROFESSOR:
                    additionalTime = 4 * WEEK_IN_MILLISECONDS;
                    break;
                case User.STUDENT:
                    long bookId = docPhysDao.getDocIdByPhysDocument(booking.getDocPhysId());
                    if (bookDao.getNote(bookId).equals(Book.BESTSELLER)) {
                        additionalTime = 2 * WEEK_IN_MILLISECONDS;
                    } else {
                        additionalTime = 3 * WEEK_IN_MILLISECONDS;
                    }
                    break;
                default:
                    throw new Exception(DOC_TYPE_EXCEPTION);
            }
        } else {
            additionalTime = 2 * WEEK_IN_MILLISECONDS;
        }

        bookingService.remove(booking.getId());
        add(new Checkout(booking.getUserId(), booking.getDocPhysId(),
                booking.getDocType(), System.nanoTime(), System.nanoTime() + additionalTime,
                false, booking.getShelf()));
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
    public List<Checkout> getCheckoutsByUser(long userId) {
        return checkoutDao.getCheckoutsByUser(userId);
    }
}
