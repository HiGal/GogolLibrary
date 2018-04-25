package com.project.glib.service;

import com.project.glib.model.*;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReturnService {
    public static final int PENNY = 100;
    public static final String DOC_TYPE_EXCEPTION = " invalid type of document";

    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;
    private final DocumentPhysicalService docPhysService;
    private final CheckoutService checkoutService;
    private final UserService userService;
    private final BookingService bookingService;
    private final MessageService messageService;
    private final LoggerService loggerService;

    @Autowired
    public ReturnService(BookService bookService,
                         JournalService journalService,
                         AudioVideoService avService,
                         DocumentPhysicalService docPhysService,
                         CheckoutService checkoutService,
                         UserService userService,
                         BookingService bookingService,
                         MessageService messageService,
                         LoggerService loggerService) {
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
        this.docPhysService = docPhysService;
        this.checkoutService = checkoutService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.messageService = messageService;
        this.loggerService = loggerService;
    }

    public Pair<Checkout, Integer> toReturnDocument(Checkout checkout) throws Exception {
        checkoutService.remove(checkout.getId());

        try {
            messageService.removeOneByUserID(checkout.getUserId(),
                    checkout.getDocPhysId(),
                    MessageService.RETURN_DOCUMENT);
        } catch (Exception ignore) {
        }

        long docVirId = docPhysService.getDocVirIdById(checkout.getDocPhysId());
        String docType = docPhysService.getTypeById(checkout.getDocPhysId());

        boolean hasNotActiveBooking = bookingService.hasNotActiveBooking(checkout.getDocPhysId());

        if (hasNotActiveBooking) {
            docPhysService.inverseCanBooked(checkout.getDocPhysId());
            switch (docType) {
                case Document.BOOK:
                    bookService.incrementCountById(docVirId);
                    loggerService.addLog(checkout.getUserId(), checkout.getDocPhysId(),
                            LoggerService.RETURNED_BOOK, System.currentTimeMillis(), Document.BOOK, true);
                    break;
                case Document.JOURNAL:
                    journalService.incrementCountById(docVirId);
                    loggerService.addLog(checkout.getUserId(), checkout.getDocPhysId(),
                            LoggerService.RETURNED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL, true);
                    break;
                case Document.AV:
                    avService.incrementCountById(docVirId);
                    loggerService.addLog(checkout.getUserId(), checkout.getDocPhysId(),
                            LoggerService.RETURNED_AV, System.currentTimeMillis(), Document.AV, true);
                    break;
                default:
                    throw new Exception(DOC_TYPE_EXCEPTION);
            }
        } else {
            Booking bookingWithMaxPriority = bookingService.getBookingWithMaxPriority(docVirId, docType);
            bookingService.setBookingActiveToTrue(bookingWithMaxPriority, checkout.getDocPhysId(), checkout.getShelf());

            messageService.addMes(
                    bookingWithMaxPriority.getId(),
                    docVirId,
                    docType,
                    MessageService.CHECKOUT_DOCUMENT
            );
        }
        return new Pair<>(checkout, getOverdue(checkout));
    }

    /**
     * get overdue for current check out
     *
     * @param checkout current check out
     * @return overdue
     */
    private int getOverdue(Checkout checkout) throws Exception {
        int days = getOverdueDays(checkout);
        String docType = docPhysService.getTypeById(checkout.getDocPhysId());
        int price = getPriceByDocPhysIdAndDocType(checkout.getDocPhysId(), docType);

        return Math.min(days * PENNY, price);
    }

    /**
     * Gets price of document
     *
     * @param docPhysId physical ID of document
     * @param docType   type of document
     * @return price
     * @throws Exception
     */
    private int getPriceByDocPhysIdAndDocType(long docPhysId, String docType) throws Exception {
        switch (docType) {
            case Document.BOOK:
                long bookId = docPhysService.getDocVirIdById(docPhysId);
                return bookService.getPriceById(bookId);
            case Document.JOURNAL:
                long journalId = docPhysService.getDocVirIdById(docPhysId);
                return journalService.getPriceById(journalId);
            case Document.AV:
                long avId = docPhysService.getDocVirIdById(docPhysId);
                return avService.getPriceById(avId);
            default:
                return 0;
        }
    }

    /**
     * Gets number of overdue days by checkout
     *
     * @param checkout chekout model
     * @return
     */
    private int getOverdueDays(Checkout checkout) {
        int days = 0;
        long difference = checkout.getReturnTime() - System.currentTimeMillis();
        if (difference < 0) {
            days = convertToDays(difference);
        }
        return Math.abs(days);
    }

    /**
     * get total overdue by current user
     *
     * @param userId id of current user
     * @return total overdue
     */
    public int getTotalOverdueByUser(long userId) throws Exception {
        int totalOverdue = 0;
        for (Checkout currentCheckout : checkoutService.getCheckoutsByUser(userId)) {
            totalOverdue += getOverdue(currentCheckout);
        }
        return totalOverdue;
    }

    public List<OverDueList> getListofOverdueUsers() throws Exception {
        List<Checkout> overdueCheckouts = checkoutService.getList()
                .stream().filter(checkout -> checkout.getReturnTime() - System.currentTimeMillis() < 0)
                .collect(Collectors.toList());

        ArrayList<OverDueList> overDueArrayList = new ArrayList<>();

        for (Checkout checkout : overdueCheckouts) {
            User user = userService.getById(checkout.getUserId());

            String type = docPhysService.getTypeById(checkout.getDocPhysId());
            long id = docPhysService.getDocVirIdById(checkout.getDocPhysId());

            String title = "";
            String author = "";

            switch (type) {
                case Document.BOOK:
                    Book book = bookService.getById(id);
                    title = book.getTitle();
                    author = book.getAuthor();
                    break;
                case Document.JOURNAL:
                    Journal journal = journalService.getById(id);
                    title = journal.getTitle();
                    author = journal.getAuthor();
                    break;
                case Document.AV:
                    AudioVideo audioVideo = avService.getById(id);
                    title = audioVideo.getTitle();
                    author = audioVideo.getAuthor();
                    break;
            }

            overDueArrayList.add(new OverDueList(
                    user.getName(), user.getSurname(), user.getPhone(),
                    title, author, getOverdueDays(checkout)));
        }
        return overDueArrayList;
    }

    /**
     * Get list of pairs<User, Integer> with positive total overdue
     *
     * @return list of pairs<User, Integer>
     */
    public List<Pair<User, Integer>> getListOfTotalOverdue() throws Exception {
        List<Pair<User, Integer>> listOverdue = new ArrayList<>();

        for (Checkout checkout : checkoutService.getList()) {
            long userId = checkout.getUserId();
            int overdue = getTotalOverdueByUser(userId);
            if (overdue > 0) {
                listOverdue.add(new Pair<>(userService.getById(userId), overdue));
            }
        }

        return listOverdue;
    }

    /**
     * Convert nanoseconds to days in integer value
     *
     * @param milliseconds nanoseconds
     * @return integers days in nanoseconds
     */
    private int convertToDays(long milliseconds) {
        return (int) ((double) milliseconds / 1000 / 60 / 60 / 24);
    }
}
