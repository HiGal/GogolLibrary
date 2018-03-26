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
    public static final String ACTIVE = "ACTIVE";
    public static final String NO_AUTH = "Sorry, but your registration is not approved yet.";
    public static final String ALREADY_HAS_THIS_BOOKING = "Sorry, but your already have this booking. Go to the library and check out ";
    public static final String ALREADY_HAS_THIS_CHECKOUT = "Sorry, but your already have this check out. ";
    public static final String RENEW = "You could renew this ";
    public static final String CHECKOUT_AGAIN = "OR return and check out again.";
    public static final String REFERENCE_DOCUMENT = "Sorry, you try to book reference ";
    public static final String MISTAKE_PART1 = "Sorry, we have a mistake in our library, all copies this ";
    public static final String MISTAKE_PART2 = " already on hand.";
    public static final String INVALID_TYPE_PART1 = "Sorry, but you try to book invalid type of document (";
    public static final String INVALID_TYPE_PART2 = ") maybe it program mistake.";
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
    private final MessageDaoImplementation messageDao;

    @Autowired
    public BookingService(BookDaoImplementation bookDao,
                          JournalDaoImplementation journalDao,
                          AudioVideoDaoImplementation avDao,
                          BookingDaoImplementation bookingDao,
                          UsersDaoImplementation usersDao,
                          DocumentPhysicalDaoImplementation documentPhysDao,
                          CheckoutDaoImplementation checkoutDao, MessageDaoImplementation messageDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.bookingDao = bookingDao;
        this.usersDao = usersDao;
        this.documentPhysDao = documentPhysDao;
        this.checkoutDao = checkoutDao;
        this.messageDao = messageDao;
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
            throw new Exception(NO_AUTH);
        }

        if (bookingDao.alreadyHasThisBooking(docId, docType, userId)) {
            throw new Exception(ALREADY_HAS_THIS_BOOKING + docType.toLowerCase());
        }

        if (checkoutDao.alreadyHasThisCheckout(docId, docType, userId)) {
            throw new Exception(ALREADY_HAS_THIS_CHECKOUT + RENEW + docType.toLowerCase() + CHECKOUT_AGAIN);
        }


        String referenceDoc = REFERENCE_DOCUMENT + docType.toLowerCase();
        String zeroCount = MISTAKE_PART1 + docType.toLowerCase() + MISTAKE_PART2;
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
                throw new Exception(INVALID_TYPE_PART1 + docType.toLowerCase() + INVALID_TYPE_PART2);
        }

        int priority = isActive ? PRIORITY.get(ACTIVE) : PRIORITY.get(usersDao.getTypeById(userId));
        recalculatePriority(docId, docType);
        long physId = documentPhysDao.getValidPhysicalId(docId, docType);

        if (checkoutDao.hasRenewedCheckout(physId)) {
            messageDao.addMes(checkoutDao.getUserIdByDocPhysId(physId),
                    physId,
                    MessageDaoImplementation.RETURN_DOCUMENT
            );
        }

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
        long physId = documentPhysDao.getValidPhysicalId(docId, docType);
        return !bookingDao.hasActiveBooking(docId, docType) && checkoutDao.hasRenewedCheckout(physId);
    }
}