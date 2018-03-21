package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Document;
import com.project.glib.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookingService {
    //TODO change priorities
    private static final String ACTIVE = "ACTIVE";
    private static final Map<String, Integer> PRIORITY = new HashMap<>();

    static {
        PRIORITY.put(ACTIVE, 10000);
        PRIORITY.put(User.STUDENT, 21);
        PRIORITY.put(User.INSTRUCTOR, 14);
        PRIORITY.put(User.TA, 7);
        PRIORITY.put(User.PROFESSOR, 1);
    }

    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final BookingDaoImplementation bookingDao;
    private final UsersDaoImplementation usersDao;
    private final DocumentPhysicalDaoImplementation documentPhysDao;
    private final CheckoutDaoImplementation checkoutDao;

    @Autowired
    public BookingService(BookDaoImplementation bookDao,
                          JournalDaoImplementation journalDao,
                          AudioVideoDaoImplementation avDao,
                          BookingDaoImplementation bookingDao,
                          UsersDaoImplementation usersDao,
                          DocumentPhysicalDaoImplementation documentPhysDao,
                          CheckoutDaoImplementation checkoutDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.bookingDao = bookingDao;
        this.usersDao = usersDao;
        this.documentPhysDao = documentPhysDao;
        this.checkoutDao = checkoutDao;
    }

    /**
     * Booking document by user if it possibility
     *
     * @param docId   id of this document
     * @param docType type of this document (valid types wrote in constants in superclass Document.java)
     * @param userId  id of user whom try to book document
     * @return Booking if all validate
     * @throws Exception if has an exception in run-time
     */
    public Booking toBookDocument(long docId, String docType, long userId) throws Exception {
        if (!usersDao.getIsAuthById(userId)) {
            throw new Exception("Sorry, but your registration is not approved yet.");
        }

        if (bookingDao.alreadyHasThisBooking(docId, docType, userId)) {
            throw new Exception("Sorry, but your already have this booking. " +
                    "Go to the library and check out " + docType.toLowerCase() + ".");
        }

        if (checkoutDao.alreadyHasThisCheckout(docId, docType, userId)) {
            throw new Exception("Sorry, but your already have this check out. " +
                    "You could renew this " + docType.toLowerCase() +
                    "OR return and check out again.");
        }


        String referenceDoc = "Sorry, you try to book reference " + docType.toLowerCase();
        String zeroCount = "Sorry, we have a mistake in our library, " +
                "all copies this " + docType.toLowerCase() + " already on hand.";
        boolean isActive;
        switch (docType) {
            case Document.BOOK:
                if (bookDao.getNote(docId).equals(Document.REFERENCE)) throw new Exception(referenceDoc);
                if (bookDao.getCountById(docId) <= 0) throw new Exception(zeroCount);
                if (bookDao.getCountById(docId) > 0) {
                    isActive = true;
                    bookDao.decrementCountById(docId);
                } else {
                    isActive = getValidIsActive(docId, docType);
                }
                break;
            case Document.JOURNAL:
                if (journalDao.getNote(docId).equals(Document.REFERENCE)) throw new Exception(referenceDoc);
                if (journalDao.getCountById(docId) <= 0) throw new Exception(zeroCount);
                if (journalDao.getCountById(docId) > 0) {
                    isActive = true;
                    journalDao.decrementCountById(docId);
                } else {
                    isActive = getValidIsActive(docId, docType);
                }
                break;
            case Document.AV:
                if (avDao.getCountById(docId) <= 0) throw new Exception(zeroCount);
                if (avDao.getCountById(docId) > 0) {
                    isActive = true;
                    avDao.decrementCountById(docId);
                } else {
                    isActive = getValidIsActive(docId, docType);
                }
                break;
            default:
                throw new Exception("Sorry, but you try to book invalid type of " +
                        "document (" + docType.toLowerCase() + ")" +
                        "maybe it program mistake.");
        }

        // TODO add method to notify user with renewed document
        int priority = isActive ? PRIORITY.get(ACTIVE) : PRIORITY.get(usersDao.getTypeById(userId));
        recalculatePriority(docId, docType);
        long physId = documentPhysDao.getValidPhysicalId(docId, docType);
        String shelf = documentPhysDao.getShelfById(physId);
        documentPhysDao.inverseCanBooked(physId);

        Booking newBooking = new Booking(userId, physId, docType, shelf, System.nanoTime(), isActive, priority);
        bookingDao.add(newBooking);

        return newBooking;
    }

    private void recalculatePriority(long docId, String docType) throws Exception {
        List<Booking> bookings = bookingDao.getListBookingsByIdDocAndDocType(docId, docType);
        for (Booking booking : bookings) {
            if (!booking.isActive()) {
                int waitingDays = convertToDays(System.nanoTime() - booking.getBookingDate());
                booking.setPriority(booking.getPriority() + waitingDays);
                bookingDao.update(booking);
            }
        }
    }

    public void setBookingActiveToTrue(Booking booking) throws Exception {
        booking.setActive(true);
        booking.setPriority(PRIORITY.get(ACTIVE));
        recalculatePriority(booking.getIdDoc(), booking.getDocType());
    }

    private int convertToDays(long milliseconds) {
        return (int) (milliseconds / 1000 / 1000 / 1000 / 60 / 60 / 24);
    }

    /**
     * get number of booked documents by current user
     *
     * @param userId id of current user
     * @return number of booked
     */
    public long numberOfBookedDocumentsByUser(long userId) throws Exception {
        return bookingDao.getNumberOfBookingsDocumentsByUser(userId);
    }

    /**
     * get all bookings by current user
     *
     * @param userId id of current user
     * @return array of bookings
     */
    public List<Booking> getBookingsByUser(long userId) throws Exception {
        return bookingDao.getBookingsByUser(userId);
    }

    private boolean getValidIsActive(long docId, String docType) {
        return !bookingDao.hasActiveBooking(docId, docType) && checkoutDao.hasRenewedCheckout(docId, docType);
    }
}