package com.project.glib.service;

import com.project.glib.dao.implementations.CheckoutDaoImplementation;
import com.project.glib.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class CheckoutService implements ModifyByLibrarianService<Checkout> {
    public static final long WEEK_IN_MILLISECONDS = 604800000000000L;
    public static final String TYPE = Checkout.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    public static final String ALREADY_HAS_THIS_CHECKOUT_EXCEPTION = "Sorry, but your already have this check out ";
    public static final String CHECKOUT_TIME_EXCEPTION = " checkout cannot be in future ";
    public static final String RETURN_TIME_EXCEPTION = " return time cannot be less than checkout time ";
    public static final String DOC_TYPE_EXCEPTION = " invalid document type ";

    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;
    private final DocumentPhysicalService docPhysService;
    private final BookingService bookingService;
    private final UserService userService;
    // TODO modify to service
    private final MessageService messageService;
    private final CheckoutDaoImplementation checkoutDao;

    @Autowired
    public CheckoutService(BookService bookService,
                           JournalService journalService,
                           AudioVideoService avService,
                           DocumentPhysicalService docPhysService,
                           @Lazy BookingService bookingService,
                           @Lazy UserService userService,
                           MessageService messageService,
                           CheckoutDaoImplementation checkoutDao) {
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
        this.docPhysService = docPhysService;
        this.bookingService = bookingService;
        this.checkoutDao = checkoutDao;
        this.userService = userService;
        this.messageService = messageService;
    }

    protected void add(Checkout checkout) throws Exception {
        checkValidParameters(checkout);
        if (alreadyHasThisCheckout(checkout.getDocPhysId(), checkout.getUserId()))
            throw new Exception(ALREADY_HAS_THIS_CHECKOUT_EXCEPTION);
        try {
            checkoutDao.add(checkout);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    public void remove(long checkoutId) throws Exception {
        Checkout checkout = getById(checkoutId);
        String docType = docPhysService.getTypeByID(checkout.getDocPhysId());
        long docVirId = docPhysService.getDocIdByID(checkout.getDocPhysId());
        switch (docType) {
            case Document.BOOK:
                bookService.incrementCountById(docVirId);
                break;
            case Document.JOURNAL:
                journalService.incrementCountById(docVirId);
                break;
            case Document.AV:
                avService.incrementCountById(docVirId);
                break;
            default:
                throw new Exception(DOC_TYPE_EXCEPTION);
        }
        try {
            if (bookingService.hasNotActiveBooking(checkout.getDocPhysId())) {
                bookingService.setBookingActiveToTrue(bookingService.getBookingWithMaxPriority(docVirId, docType));
                bookingService.recalculatePriority(docVirId, docType);
            }
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

        if (checkout.getShelf().equals("")) {
            throw new Exception(SHELF_EXCEPTION);
        }
    }

    /**
     * check out current booking
     *
     * @param booking current booking
     */
    public void toCheckoutDocument(Booking booking) throws Exception {
        long additionalTime;

        switch (booking.getDocType()) {
            case Document.BOOK:
                switch (userService.getTypeById(booking.getUserId())) {
                    case User.STUDENT:
                        long bookId = booking.getDocVirId();
                        if (bookService.getNote(bookId).equals(Book.BESTSELLER)) {
                            additionalTime = 2 * WEEK_IN_MILLISECONDS;
                        } else {
                            additionalTime = 3 * WEEK_IN_MILLISECONDS;
                        }
                        break;
                    case User.INSTRUCTOR:
                    case User.TA:
                    case User.PROFESSOR:
                        additionalTime = 4 * WEEK_IN_MILLISECONDS;
                        break;
                    case User.PROFESSOR_VISITING:
                        additionalTime = WEEK_IN_MILLISECONDS;
                        break;
                    default:
                        throw new Exception(TYPE_EXCEPTION);
                }
                break;
            case Document.JOURNAL:
            case Document.AV:
                switch (userService.getTypeById(booking.getUserId())) {
                    case User.STUDENT:
                    case User.INSTRUCTOR:
                    case User.TA:
                    case User.PROFESSOR:
                        additionalTime = 2 * WEEK_IN_MILLISECONDS;
                        break;
                    case User.PROFESSOR_VISITING:
                        additionalTime = WEEK_IN_MILLISECONDS;
                        break;
                    default:
                        throw new Exception(DOC_TYPE_EXCEPTION);
                }
                break;
            default:
                throw new Exception(DOC_TYPE_EXCEPTION);
        }

        bookingService.remove(booking.getId());
        messageService.removeOneByUserID(booking.getUserId(), booking.getDocPhysId(), MessageService.CHECKOUT_DOCUMENT);
        long docPhysId = booking.getDocPhysId();
        add(new Checkout(booking.getUserId(), docPhysId, System.nanoTime(),
                System.nanoTime() + additionalTime, booking.getShelf()));
    }


    @Override
    public Checkout getById(long checkoutId) {
        return checkoutDao.getById(checkoutId);
    }

    @Override
    public long getId(Checkout checkout) throws Exception {
        try {
            return checkoutDao.getId(checkout);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Checkout> getList() {
        try {
            return checkoutDao.getList();
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public int getNumberOfCheckoutDocumentsByUser(long userId) throws Exception {
        try {
            return getCheckoutsByUser(userId).size();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public List<Checkout> getCheckoutsByUser(long userId) {
        try {
            return getList().stream()
                    .filter(checkout -> checkout.getUserId() == userId)
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public boolean alreadyHasThisCheckout(long docPhysId, long userId) {
        DocumentPhysical docPhys = docPhysService.getById(docPhysId);

        List<DocumentPhysical> docPhysList = docPhysService.getList().stream()
                .filter(doc -> doc.getDocVirId() == docPhys.getDocVirId())
                .filter(doc -> doc.getDocType().equals(docPhys.getDocType()))
                .collect(Collectors.toList());

        List<Checkout> checkoutList = getList().stream()
                .filter(checkout -> checkout.getUserId() == userId)
                .collect(Collectors.toList());

        for (Checkout checkout : checkoutList) {
            for (DocumentPhysical currentDocPhys : docPhysList) {
                if (checkout.getDocPhysId() == currentDocPhys.getId()) return true;
            }
        }

        return false;
    }

    public long getUserIdByDocPhysId(long docPhysId) throws Exception {
        try {
            return getByDocPhysId(docPhysId).getUserId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public Checkout getByDocPhysId(long docPhysId) throws Exception {
        try {
            return getList().stream()
                    .filter(checkout -> checkout.getDocPhysId() == docPhysId)
                    .findFirst().get();
        } catch (NoSuchElementException | NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
