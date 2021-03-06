package com.project.glib.service;

import com.project.glib.dao.implementations.BookingDaoImplementation;
import com.project.glib.model.Booking;
import com.project.glib.model.Checkout;
import com.project.glib.model.Document;
import com.project.glib.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.thymeleaf.util.ListUtils.sort;

@Service
public class BookingService implements ModifyByLibrarianService<Booking> {
    public static final String TYPE = Booking.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    public static final String USER_ID_EXCEPTION = " invalid id user ";
    public static final String DOC_TYPE_EXCEPTION = " invalid type of document";
    public static final String BOOKING_DATE_EXCEPTION = " booking date cannot be in future ";
    public static final String PRIORITY_EXCEPTION = " priority must be not negative ";
    public static final String REFERENCE_EXCEPTION = "Sorry, you try to book reference ";
    public static final String ALREADY_HAS_THIS_BOOKING_EXCEPTION = "Sorry, but your already have this booking ";
    public static final String ALREADY_HAS_THIS_CHECKOUT_EXCEPTION = "Sorry, but your already have this check out ";
    public static final String AUTH_EXCEPTION = "Sorry, but your registration is not approved yet.";
    public static final long DAY_IN_MILLISECONDS = 86400000L;
    public static final String OUTSTANDING = "OUTSTANDING REQUEST";
    public static final String ACTIVE = "ACTIVE";
    public static final HashMap<String, Integer> PRIORITY = new HashMap<>();
    private static final String EXPECTED = "EXPECTED";
    private static final String EMPTY_SHELF = "EMPTY";
    private static final long EMPTY_ID = 0L;

    static {
        PRIORITY.put(OUTSTANDING, 2000000);
        PRIORITY.put(ACTIVE, 10000);
        PRIORITY.put(EXPECTED, 5000);
        PRIORITY.put(User.STUDENT, 21);
        PRIORITY.put(User.INSTRUCTOR, 14);
        PRIORITY.put(User.TA, 7);
        PRIORITY.put(User.PROFESSOR_VISITING, 3);
        PRIORITY.put(User.PROFESSOR, 1);
    }

    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;
    private final UserService userService;
    private final DocumentPhysicalService docPhysService;
    private final CheckoutService checkoutService;
    private final MessageService messageService;
    private final BookingDaoImplementation bookingDao;
    private final LoggerService loggerService;

    @Autowired
    public BookingService(BookService bookService,
                          JournalService journalService,
                          AudioVideoService avService,
                          DocumentPhysicalService docPhysService,
                          @Lazy UserService userService,
                          @Lazy CheckoutService checkoutService,
                          MessageService messageService,
                          BookingDaoImplementation bookingDao, LoggerService loggerService) {
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
        this.docPhysService = docPhysService;
        this.userService = userService;
        this.checkoutService = checkoutService;
        this.messageService = messageService;
        this.bookingDao = bookingDao;
        this.loggerService = loggerService;
    }

    /**
     * Creates an outstanding request for the document.
     * In this case all priority queue about this document will be deleted
     *
     * @param booking outstanding booking from the librarian for the user
     * @throws Exception run-time exception
     */
    public void outstandingRequest(Booking booking) throws Exception {
        long userId = booking.getUserId();
        long docVirId = booking.getDocVirId();
        // TODO rewrite this code
        Checkout checkout = checkoutService.getList().get(0);
        long docPhysId = checkout.getDocPhysId();
        String shelf = checkout.getShelf();

        booking.setDocPhysId(docPhysId);
        booking.setShelf(shelf);

        String docType = booking.getDocType();

        if (!userService.getIsAuthById(userId)) {
            throw new Exception(AUTH_EXCEPTION);
        }

        if (alreadyHasThisBooking(docVirId, docType, userId)) {
            throw new Exception(ALREADY_HAS_THIS_BOOKING_EXCEPTION);
        }

        if (checkoutService.alreadyHasThisCheckout(docPhysId, userId)) {
            throw new Exception(ALREADY_HAS_THIS_CHECKOUT_EXCEPTION);
        }

        checkValidParameters(booking);

        for (Booking b : getListBookingsByDocVirIdAndDocType(docVirId, docType)) {
            if (b.getUserId() != booking.getUserId()) {
                messageService.addMes(b.getUserId(),
                        b.getDocVirId(),
                        b.getDocType(),
                        MessageService.DELETED_QUEUE
                );
            }
        }

        for (Checkout c : checkoutService.getByDocVirIdAndDocType(docVirId, docType)) {
            long virId = docPhysService.getDocVirIdById(c.getDocPhysId());
            String type = docPhysService.getTypeById(c.getDocPhysId());

            if (c.getUserId() != booking.getUserId()) {
                messageService.addMes(c.getUserId(),
                        virId,
                        type,
                        MessageService.RETURN_DOCUMENT
                );
            }
            c.setReturnTime(System.currentTimeMillis());
            checkoutService.update(c);
        }

        deletePriority(docVirId, docType);

        // add outstanding booking
        booking.setActive(true);
        booking.setBookingDate(System.currentTimeMillis());
        booking.setPriority(PRIORITY.get(OUTSTANDING));
        add(booking);
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
        if (!userService.getIsAuthById(userId)) {
            throw new Exception(AUTH_EXCEPTION);
        }

        if (alreadyHasThisBooking(docVirId, docType, userId)) {
            throw new Exception(ALREADY_HAS_THIS_BOOKING_EXCEPTION);
        }

        checkValidToBook(docVirId, docType);

        long docPhysId = EMPTY_ID;
        String shelf = EMPTY_SHELF;
        int priority = PRIORITY.get(userService.getTypeById(userId));
        boolean isActive = false;
        try {
            try {
                docPhysId = docPhysService.getValidPhysId(docVirId, docType);
            } catch (Exception ignored) {
            }

            if (checkoutService.alreadyHasThisCheckout(docPhysId, userId)) {
                throw new Exception(ALREADY_HAS_THIS_CHECKOUT_EXCEPTION);
            }

            shelf = docPhysService.getShelfById(docPhysId);
            priority = PRIORITY.get(ACTIVE);
            isActive = true;
            switch (docType) {
                case Document.BOOK:
                    bookService.decrementCountById(docVirId);
                    docPhysService.inverseCanBooked(docPhysId);
                    loggerService.addLog(userId, docPhysId, LoggerService.BOOKED_BOOK, System.currentTimeMillis(), Document.BOOK, true);
                    break;
                case Document.JOURNAL:
                    journalService.decrementCountById(docVirId);
                    docPhysService.inverseCanBooked(docPhysId);
                    loggerService.addLog(userId, docPhysId, LoggerService.BOOKED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL, true);
                    break;
                case Document.AV:
                    avService.decrementCountById(docVirId);
                    docPhysService.inverseCanBooked(docPhysId);
                    loggerService.addLog(userId, docPhysId, LoggerService.BOOKED_AV, System.currentTimeMillis(), Document.AV, true);
                    break;
                default:
                    throw new Exception(TYPE_EXCEPTION);
            }
        } catch (NullPointerException | NoSuchElementException ignore) {
        }

        recalculatePriority(docVirId, docType);
        add(new Booking(userId, docVirId, docType, docPhysId, System.currentTimeMillis(), isActive, priority, shelf));
        if (docPhysId == EMPTY_ID){
            for (Checkout c : checkoutService.getByDocVirIdAndDocType(docVirId, docType)) {
                long virId = docPhysService.getDocVirIdById(c.getDocPhysId());
                String type = docPhysService.getTypeById(c.getDocPhysId());

                if (c.getUserId() != userId) {
                    messageService.addMes(c.getUserId(),
                            virId,
                            type,
                            MessageService.RETURN_DOCUMENT
                    );
                }
                c.setReturnTime(System.currentTimeMillis());
                checkoutService.update(c);
            }
            throw new Exception("You are in the queue now. Wait a notification when you can take a book");
        }
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
        Booking booking = getById(bookingId);
        if (booking.getPriority() == PRIORITY.get(ACTIVE)) {
            switch (booking.getDocType()) {
                case Document.BOOK:
                    bookService.incrementCountById(booking.getDocVirId());
                    break;
                case Document.JOURNAL:
                    journalService.incrementCountById(booking.getDocVirId());
                    break;
                case Document.AV:
                    avService.incrementCountById(booking.getDocVirId());
                    break;
                default:
                    throw new Exception(DOC_TYPE_EXCEPTION);
            }
        }
        try {
            try {
                // Priority more than Expected or equal
                if (bookingCanCheckout(booking)) {
                    Booking bookingWithMaxPriority = getBookingWithMaxPriority(booking.getDocVirId(), booking.getDocType());
                    setBookingActiveToTrue(bookingWithMaxPriority, booking.getDocPhysId(), booking.getShelf());
                }
            } catch (NullPointerException ignore) {
            }

            recalculatePriority(booking.getDocVirId(), booking.getDocType());
            bookingDao.remove(bookingId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    public void removeBecauseCheckout(long bookingId) {
        bookingDao.remove(bookingId);
    }

    /**
     * Checks all parameters of the booking in case of appropriation
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

        if (booking.getBookingDate() > System.currentTimeMillis()) {
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
     * Checks the ability to book this document
     *
     * @param docVirId
     * @param docType
     * @throws Exception
     */
    private void checkValidToBook(long docVirId, String docType) throws Exception {
        switch (docType) {
            case Document.BOOK:
                if (bookService.getNote(docVirId).equals(Document.REFERENCE)) {
                    throw new Exception(REFERENCE_EXCEPTION + docType.toLowerCase());
                }
                break;
            case Document.JOURNAL:
                if (journalService.getNote(docVirId).equals(Document.REFERENCE)) {
                    throw new Exception(REFERENCE_EXCEPTION + docType.toLowerCase());
                }
                break;
            case Document.AV:
                break;
            default:
                throw new Exception(TYPE_EXCEPTION);
        }
    }

    /**
     * Recalculates all priority in DB on this document
     *
     * @param docVirId virtual ID of document
     * @param docType  type of document
     */
    private void recalculatePriority(long docVirId, String docType) {
        List<Booking> bookings = getListBookingsByDocVirIdAndDocType(docVirId, docType);
        for (Booking booking : bookings) {
            if (!booking.isActive()) {
                int waitingDays = convertToDays(System.currentTimeMillis() - booking.getBookingDate());
                booking.setPriority(booking.getPriority() + waitingDays);
                bookingDao.update(booking);
            }
        }
    }

    /**
     * Deletes the priority
     *
     * @param docVirId
     * @param docType
     */
    private void deletePriority(long docVirId, String docType) {
        List<Booking> bookings = getListBookingsByDocVirIdAndDocType(docVirId, docType);
        for (Booking booking : bookings) {
            // Priority less than Expected
            if (bookingInQueue(booking)) {
                bookingDao.remove(booking.getId());
            }
        }
    }

    @Scheduled(fixedDelay = DAY_IN_MILLISECONDS / 2)
    private void recalculateAll() {
        for (Booking booking : getList()) {
            // Priority less than Expected
            if (bookingInQueue(booking)) {
                int waitingDays = convertToDays(System.currentTimeMillis() - booking.getBookingDate());
                booking.setPriority(booking.getPriority() + waitingDays);
                bookingDao.update(booking);
            }
        }
    }

    @Scheduled(fixedDelay = DAY_IN_MILLISECONDS / 2)
    private void deleteLateBookings() throws Exception {
        for (Booking booking : getList()) {
            boolean isLate = System.currentTimeMillis() - booking.getBookingDate() > DAY_IN_MILLISECONDS;
            // Priority more than Expected or equal
            if (bookingCanCheckout(booking) && isLate) {
                messageService.addMes(booking.getUserId(),
                        booking.getDocPhysId(),
                        booking.getDocType(),
                        MessageService.LATE_DELETED
                );
                remove(booking.getId());
            }
        }
    }

    public List<Booking> getPriorityQueueByDocVirIdAndDocType(long docVirId, String docType) {
        return sort(getListBookingsByDocVirIdAndDocType(docVirId, docType), Booking::compareTo);
    }

    private boolean bookingInQueue(Booking booking) {
        return booking.getPriority() < PRIORITY.get(ACTIVE);
    }

    private boolean bookingCanCheckout(Booking booking) {
        return booking.getPriority() >= PRIORITY.get(EXPECTED);
    }

    void setBookingActiveToTrue(Booking booking, long docPhysId, String shelf) {
        booking.setDocPhysId(docPhysId);
        booking.setShelf(shelf);
        booking.setActive(true);
        booking.setBookingDate(System.currentTimeMillis());
        booking.setPriority(PRIORITY.get(EXPECTED));
        recalculatePriority(booking.getDocVirId(), booking.getDocType());
    }

    private int convertToDays(long milliseconds) {
        return (int) ((double) milliseconds / 1000 / 60 / 60 / 24);
    }

    @Override
    public Booking getById(long bookingId) {
        return bookingDao.getById(bookingId);
    }

    @Override
    public long getId(Booking booking) throws Exception {
        try {
            return bookingDao.getId(booking);
        } catch (NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> getList() {
        try {
            return bookingDao.getList().stream()
                    .filter(booking -> !booking.getShelf().equals( EMPTY_SHELF))
                    .collect(Collectors.toList());
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }


    public long getNumberOfBookingsDocumentsByUser(long userId) {
        return getBookingsByUser(userId).size();
    }

    public List<Booking> getBookingsByUser(long userId) {
        try {
            return   getList().stream()
                    .filter(booking -> booking.getUserId() == userId)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public boolean alreadyHasThisBooking(long docVirId, String docType, long userId) {
        return getList().stream()
                .filter(booking -> booking.getUserId() == userId)
                .filter(booking -> booking.getDocVirId() == docVirId)
                .anyMatch(booking -> booking.getDocType().equals(docType));
    }

    public List<Booking> getListBookingsByDocVirIdAndDocType(long docVirId, String docType) {
        try {
            return bookingDao.getList().stream()
                    .filter(booking -> booking.getDocVirId() == docVirId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public Booking getBookingWithMaxPriority(long docVirId, String docType) {
        try {
            return bookingDao.getBookingWithMaxPriority(docVirId, docType);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    public boolean hasActiveBooking(long docPhysId) {
        try {
            return getList().stream()
                    .filter(booking -> booking.getDocPhysId() == docPhysId)
                    .anyMatch(Booking::isActive);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if someone in the queue on this document
     *
     * @param docPhysId document physical ID
     * @return true if no one wait it
     */
    public boolean hasNotActiveBooking(long docPhysId) {
        try {
            long id = docPhysService.getDocVirIdById(docPhysId);
            String type = docPhysService.getTypeById(docPhysId);
            List<Booking> bookings = getList().stream()
                    .filter(booking -> booking.getDocType().equals(type))
                    .filter(booking -> booking.getDocVirId() == id)
                    .collect(Collectors.toList());

            return bookings.size() == 0;
        } catch (NoSuchElementException e) {
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public Booking getBookingOnThisDocument(long docPhysId) {
        try {
            return getList().stream()
                    .filter(booking -> booking.getDocPhysId() == docPhysId)
                    .findFirst().get();
        } catch (NullPointerException | NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Deletes all bookings from the DB
     *
     * @throws Exception
     */
    public void deleteAllBookings() throws Exception {
        List<Booking> bookings = getList();
        for (Booking booking : bookings) {
            remove(booking.getId());
        }
    }
}