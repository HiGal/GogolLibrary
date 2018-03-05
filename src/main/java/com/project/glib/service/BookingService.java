package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
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
     * @param docId id of this document
     * @param docType type of this document (valid types wrote in constants in superclass Document.java)
     * @param userId id of user whom try to book document
     * @return Booking if all validate
     * @throws Exception if has an exception in run-time
     */
    public Booking toBookDocument(long docId, String docType, long userId) throws Exception {
        if (!usersDao.getIsAuthById(userId)){
            throw new Exception("Sorry, but your registration is not approved yet.");
        }

        if (bookingDao.alreadyHasThisBooking(docId, docType, userId)){
            throw new Exception("Sorry, but your already have this booking. " +
                    "Go to the library and check out " + docType.toLowerCase() + ".");
        }

        if (checkoutDao.alreadyHasThisCheckout(docId, docType, userId)) {
            throw new Exception("Sorry, but your already have this check out. " +
                    "You could renew this " + docType.toLowerCase() +
                    "OR return and check out again.");
        }

        String zeroCount = "Sorry, we have a mistake in our library, " +
                "all copies this " + docType.toLowerCase() + " already on hand.";
        switch (docType) {
            case Document.BOOK:
                if (bookDao.getCountById(docId) <= 0) throw new Exception(zeroCount);
                bookDao.decrementCountById(docId);
                break;
            case Document.JOURNAL:
                if (journalDao.getCountById(docId) <= 0) throw new Exception(zeroCount);
                journalDao.decrementCountById(docId);
                break;
            case Document.AV:
                if (avDao.getCountById(docId) <= 0) throw new Exception(zeroCount);
                avDao.decrementCountById(docId);
                break;
            default:
                throw new Exception("Sorry, but you try to book invalid type of " +
                        "document (" + docType.toLowerCase() + ")" +
                        "maybe it program mistake.");
        }

        long physId = documentPhysDao.getValidPhysicalId(docId, docType);
        String shelf = documentPhysDao.getShelfById(physId);
        documentPhysDao.inverseCanBooked(physId);

        Booking newBooking = new Booking(userId, physId, docType, shelf, System.nanoTime());
        bookingDao.add(newBooking);

        return newBooking;
    }

    /**
     * get number of booked documents by current user
     * @param userId id of current user
     * @return number of booked
     */
    public long numberOfBookedDocumentsByUser(long userId) throws Exception {
        return bookingDao.getNumberOfBookingsDocumentsByUser(userId);
    }

    /**
     * get all bookings by current user
     * @param userId id of current user
     * @return array of bookings
     */
    public List<Booking> getBookingsByUser(long userId) throws Exception {
        return bookingDao.getBookingsByUser(userId);
    }
}