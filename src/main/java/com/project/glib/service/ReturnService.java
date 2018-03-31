package com.project.glib.service;

import com.project.glib.model.Booking;
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
    public static final String DOC_TYPE_EXCEPTION = " invalid type of document";

    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;
    private final DocumentPhysicalService docPhysService;
    private final CheckoutService checkoutService;
    private final UserService userService;
    private final BookingService bookingService;
    // TODO modify to service
    private final MessageService messageService;

    @Autowired
    public ReturnService(BookService bookService,
                         JournalService journalService,
                         AudioVideoService avService,
                         DocumentPhysicalService docPhysService,
                         CheckoutService checkoutService,
                         UserService userService,
                         BookingService bookingService,
                         MessageService messageService) {
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
        this.docPhysService = docPhysService;
        this.checkoutService = checkoutService;
        this.userService = userService;
        this.bookingService = bookingService;
        this.messageService = messageService;
    }

    public Pair<Checkout, Integer> toReturnDocument(Checkout checkout) throws Exception {
        checkoutService.remove(checkout.getId());

        messageService.removeOneByUserID(checkout.getUserId(),
                checkout.getDocPhysId(),
                MessageService.RETURN_DOCUMENT);

        long docVirId = docPhysService.getDocIdByID(checkout.getDocPhysId());
        String docType = docPhysService.getTypeByID(checkout.getDocPhysId());
        Booking bookingOnThisDocument = bookingService.getBookingOnThisDocument(checkout.getDocPhysId());
        boolean hasNotActiveBooking = bookingService.hasNotActiveBooking(checkout.getDocPhysId());

        if (bookingOnThisDocument == null && !hasNotActiveBooking) {
            docPhysService.inverseCanBooked(checkout.getDocPhysId());
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
        } else if (bookingOnThisDocument != null) {
            // TODO Maybe change method parameters to (Booking booking, String message)
            messageService.addMes(
                    bookingOnThisDocument.getId(),
                    docVirId,
                    docType,
                    MessageService.CHECKOUT_DOCUMENT
            );
        } else {
            Booking bookingWithMaxPriority = bookingService.getBookingWithMaxPriority(docVirId, docType);
            bookingService.setBookingActiveToTrue(bookingWithMaxPriority);

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
    public int getOverdue(Checkout checkout) throws Exception {
        int overdue = 0;
        long difference = checkout.getReturnTime() - System.nanoTime();
        if (difference < 0) {
            int days = convertToDays(difference);
            int price;
            switch (docPhysService.getTypeByID(checkout.getDocPhysId())) {
                case Document.BOOK:
                    long bookId = docPhysService.getDocIdByPhysDocument(checkout.getDocPhysId());
                    price = bookService.getPriceById(bookId);
                    break;
                case Document.JOURNAL:
                    long journalId = docPhysService.getDocIdByPhysDocument(checkout.getDocPhysId());
                    price = journalService.getPriceById(journalId);
                    break;
                case Document.AV:
                    long avId = docPhysService.getDocIdByPhysDocument(checkout.getDocPhysId());
                    price = avService.getPriceById(avId);
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
        for (Checkout currentCheckout : checkoutService.getCheckoutsByUser(userId)) {
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
        List<Checkout> checkouts = checkoutService.getList();

        for (Checkout checkout : checkouts) {
            long userId = checkout.getUserId();
            if (getTotalOverdueByUser(userId) > 0) {
                listOverdue.add(new Pair<>(userService.getById(userId), getTotalOverdueByUser(userId)));
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
