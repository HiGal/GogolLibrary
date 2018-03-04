package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class BookingService {
    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final BookingDaoImplementation bookingDao;
    private final UsersDaoImplementation usersDao;
    private final DocumentPhysicalDaoImplementation documentPhysDao;

    @Autowired
    public BookingService(BookDaoImplementation bookDao,
                          JournalDaoImplementation journalDao,
                          AudioVideoDaoImplementation avDao,
                          BookingDaoImplementation bookingDao,
                          UsersDaoImplementation usersDao,
                          DocumentPhysicalDaoImplementation documentPhysDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.bookingDao = bookingDao;
        this.usersDao = usersDao;
        this.documentPhysDao = documentPhysDao;
    }

    public Booking toBookDocument(long docId, String docType, long userId) {
        if (!usersDao.getIsAuthById(userId) || bookingDao.alreadyHasThisBooking(docId, docType, userId)) {
            return null;
        }

        switch (docType) {
            case Document.BOOK:
                if (bookDao.getCountById(docId) <= 0) return null;
                bookDao.decrementCountById(docId);
                break;
            case Document.JOURNAL:
                if (journalDao.getCountById(docId) <= 0) return null;
                journalDao.decrementCountById(docId);
                break;
            case Document.AV:
                if (avDao.getCountById(docId) <= 0) return null;
                avDao.decrementCountById(docId);
                break;
            default:
                return null;
        }

        long physId = documentPhysDao.getValidPhysicalId(docId, docType);
        String shelf = documentPhysDao.getShelfById(physId);
        documentPhysDao.inverseCanBooked(physId);

        Booking newBooking = new Booking(userId, physId, docType, shelf, System.nanoTime());
        bookingDao.add(newBooking);

        return newBooking;
    }

    public long numberOfCheckoutDocumentsByUser(long userId) {
        return bookingDao.getNumberOfBookingsDocumentsByUser(userId);
    }

    public Booking[] getCheckoutsByUser(long userId) {
        return bookingDao.getBookingsByUser(userId);
    }
}