package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BookingService implements ModifyByLibrarianService<Booking> {
    //TODO change priorities
    public static final String TYPE = Booking.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String USER_ID_EXCEPTION = " invalid id user ";
    public static final String DOC_TYPE_EXCEPTION = " invalid type of document";
    public static final String DOC_PHYS_ID_EXCEPTION = " invalid id of physical document ";
    public static final String BOOKING_DATE_EXCEPTION = " booking date cannot be in future ";
    public static final String PRIORITY_EXCEPTION = " priority must be not negative ";
    public static final String REFERENCE_EXCEPTION = "Sorry, you try to book reference ";
    public static final String ALREADY_HAS_THIS_BOOKING_EXCEPTION = "Sorry, but your already have this booking ";
    public static final String ALREADY_HAS_THIS_CHECKOUT_EXCEPTION = "Sorry, but your already have this check out ";
    public static final String AUTH_EXCEPTION = "Sorry, but your registration is not approved yet.";
    private static final String ACTIVE = "ACTIVE";
    private static final String OUTSTANDING = "OUTSTANDING REQUEST";
    private static final String EXPECTED = "EXPECTED";
    private static final String EMPTY_SHELF = "EMPTY";
    private static final HashMap<String, Integer> PRIORITY = new HashMap<String, Integer>();

    static {
        PRIORITY.put(OUTSTANDING, 2000000);
        PRIORITY.put(ACTIVE, 10000);
        PRIORITY.put(EXPECTED, 5000);
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
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final CheckoutDaoImplementation checkoutDao;
    private final MessageDaoImplementation messageDao;

    @Autowired
    public BookingService(BookDaoImplementation bookDao,
                          JournalDaoImplementation journalDao,
                          AudioVideoDaoImplementation avDao,
                          BookingDaoImplementation bookingDao,
                          UsersDaoImplementation usersDao,
                          DocumentPhysicalDaoImplementation docPhysDao,
                          CheckoutDaoImplementation checkoutDao, MessageDaoImplementation messageDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.bookingDao = bookingDao;
        this.usersDao = usersDao;
        this.docPhysDao = docPhysDao;
        this.checkoutDao = checkoutDao;
        this.messageDao = messageDao;
    }

    /**
     * Creates an outstanding request for the document.
     * In this case all priority queue about this document will be deleted
     *
     * @param booking outstanding booking from the librarian for the user
     * @throws Exception run-time exception
     */
    public void outstandingRequest(Booking booking) throws Exception {
        checkValidParameters(booking);
        deletePriority(booking.getDocVirId(), booking.getDocType());
        booking.setPriority(PRIORITY.get(OUTSTANDING));
        add(booking);
    }

    /**
     * Adds new booking record in the database
     *
     * @param booking booking to put in the DB
     * @throws Exception run-time exception
     */
    private void add(Booking booking) throws Exception {
        checkValidParameters(booking);
        try {
            bookingDao.add(booking);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    /**
     * Removes a booking by booking ID from the database
     *
     * @param bookingId ID of current booking
     * @throws Exception run-time exception
     */
    public void remove(long bookingId) throws Exception {
        Booking booking = bookingDao.getById(bookingId);
        if (booking.getPriority() == PRIORITY.get(ACTIVE)) {
            switch (booking.getDocType()) {
                case Document.BOOK:
                    bookDao.incrementCountById(booking.getDocVirId());
                    break;
                case Document.JOURNAL:
                    journalDao.incrementCountById(booking.getDocVirId());
                    break;
                case Document.AV:
                    avDao.incrementCountById(booking.getDocVirId());
                    break;
                default:
                    throw new Exception(DOC_TYPE_EXCEPTION);
            }
        }
        try {
            if (booking.isActive()) {
                setBookingActiveToTrue(bookingDao.getBookingWithMaxPriority(booking.getDocVirId(), booking.getDocType()));
            }
            recalculatePriority(booking.getDocVirId(), booking.getDocType());
            bookingDao.remove(bookingId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    /**
     * Checks all parametrs of the booking in case of appropriation
     * to the library system
     *
     * @param booking booking record to put in the DB
     * @throws Exception run-time exception
     */
    @Override
    public void checkValidParameters(Booking booking) throws Exception {
        if (booking.getUserId() <= 0) {
            throw new Exception(USER_ID_EXCEPTION);
        }

        if (!Document.isType(booking.getDocType())) {
            throw new Exception(DOC_TYPE_EXCEPTION);
        }

        if (booking.getDocPhysId() <= 0) {
            throw new Exception(DOC_PHYS_ID_EXCEPTION);
        }

        if (booking.getBookingDate() > System.nanoTime()) {
            throw new Exception(BOOKING_DATE_EXCEPTION);
        }

        if (booking.getPriority() < 0) {
            throw new Exception(PRIORITY_EXCEPTION);
        }

        if (booking.getShelf().equals("")) {
            throw new Exception(SHELF_EXCEPTION);
        }
    }

    /**
     * Booking a document user if it possible
     *
     * @param docVirId ID of this document
     * @param docType  type of this document (valid types wrote in constants in superclass Document.java)
     * @param userId   ID of user whom try to book document
     * @throws Exception run-time exception
     */
    public void toBookDocument(long docVirId, String docType, long userId) throws Exception {
        if (!usersDao.getIsAuthById(userId)) {
            throw new Exception(AUTH_EXCEPTION);
        }

        if (bookingDao.alreadyHasThisBooking(docVirId, docType, userId)) {
            throw new Exception(ALREADY_HAS_THIS_BOOKING_EXCEPTION);
        }

        if (checkoutDao.alreadyHasThisCheckout(docVirId, docType, userId)) {
            throw new Exception(ALREADY_HAS_THIS_CHECKOUT_EXCEPTION);
        }

        checkValidToBook(docVirId, docType);

        long docPhysId = EMPTY_ID;
        String shelf = EMPTY_SHELF;
        int priority = PRIORITY.get(usersDao.getTypeById(userId));
        boolean isActive = false;
        try {
            docPhysId = docPhysDao.getValidPhysId(docVirId, docType);
            shelf = docPhysDao.getShelfById(docPhysId);
            priority = PRIORITY.get(ACTIVE);
            isActive = true;
            switch (docType) {
                case Document.BOOK:
                    bookDao.decrementCountById(docVirId);
                    docPhysDao.inverseCanBooked(docPhysId);
                    break;
                case Document.JOURNAL:
                    journalDao.decrementCountById(docVirId);
                    docPhysDao.inverseCanBooked(docPhysId);
                    break;
                case Document.AV:
                    avDao.decrementCountById(docVirId);
                    docPhysDao.inverseCanBooked(docPhysId);
                    break;
                default:
                    throw new Exception(TYPE_EXCEPTION);
            }
        } catch (Exception e) {
            DocumentPhysical docPhys = getValidDocPhys(docVirId, docType);
            if (docPhys != null) {
                docPhysId = docPhys.getId();
                shelf = docPhys.getShelf();
                priority = PRIORITY.get(EXPECTED);
                isActive = true;
            }
        } finally {
            recalculatePriority(docVirId, docType);
            add(new Booking(userId, docVirId, docType, docPhysId, System.nanoTime(), isActive, priority, shelf));
        }
    }

    private void checkValidToBook(long docVirId, String docType) throws Exception {
        switch (docType) {
            case Document.BOOK:
                if (bookDao.getNote(docVirId).equals(Document.REFERENCE)) {
                    throw new Exception(REFERENCE_EXCEPTION + docType.toLowerCase());
                }
                break;
            case Document.JOURNAL:
                if (journalDao.getNote(docVirId).equals(Document.REFERENCE)) {
                    throw new Exception(REFERENCE_EXCEPTION + docType.toLowerCase());
                }
                break;
            case Document.AV:
                break;
            default:
                throw new Exception(TYPE_EXCEPTION);
        }
    }

    private void recalculatePriority(long docVirId, String docType) {
        List<Booking> bookings = bookingDao.getListBookingsByDocVirIdAndDocType(docVirId, docType);
        for (Booking booking : bookings) {
            if (!booking.isActive()) {
                int waitingDays = convertToDays(System.nanoTime() - booking.getBookingDate());
                booking.setPriority(booking.getPriority() + waitingDays);
                bookingDao.update(booking);
            }
        }
    }

    private void deletePriority(long docVirId, String docType) {
        List<Booking> bookings = bookingDao.getListBookingsByDocVirIdAndDocType(docVirId, docType);
        for (Booking booking : bookings) {
            if (!booking.isActive()) {
                bookingDao.remove(booking.getId());
            }
        }
    }

    public void setBookingActiveToTrue(Booking booking) {
        booking.setActive(true);
        booking.setPriority(PRIORITY.get(EXPECTED));
        recalculatePriority(booking.getDocPhysId(), booking.getDocType());
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
    public long numberOfBookedDocumentsByUser(long userId) {
        return bookingDao.getNumberOfBookingsDocumentsByUser(userId);
    }

    /**
     * get all bookings by current user
     *
     * @param userId id of current user
     * @return array of bookings
     */
    public List<Booking> getBookingsByUser(long userId) {
        return bookingDao.getBookingsByUser(userId);
    }

    // TODO rename method
    private DocumentPhysical getValidDocPhys(long docVirId, String docType) throws Exception {
        List<DocumentPhysical> docPhysList = docPhysDao.getByDocVirIdAndDocType(docVirId, docType);
        for (DocumentPhysical docPhys : docPhysList) {
            long docPhysId = docPhys.getId();
            Checkout checkout = checkoutDao.getByDocPhysId(docPhysId);
            if (checkout.isRenewed()) {
                notifyUserToReturnDocument(checkout.getUserId(), docVirId, docType);
                if (!bookingDao.hasActiveBooking(docPhysId)) {
                    return docPhys;
                }
            }
        }
        return null;
    }

    private void notifyUserToReturnDocument(long userId, long docVirId, String docType) throws Exception {
        messageDao.addMes(userId, docVirId, docType, MessageDaoImplementation.RETURN_DOCUMENT);
    }
}