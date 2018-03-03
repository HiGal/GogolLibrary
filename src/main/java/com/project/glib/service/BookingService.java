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

    public boolean toBookDocument(long docId, String docType, long userId) {
        if (usersDao.getIsAuthById(userId)) {
            return false;
        }

        switch (docType) {
            case Document.BOOK:
                if (bookDao.getCountById(docId) <= 0) return false;
                bookDao.decrementCountById(docId);
                break;
            case Document.JOURNAL:
                if (journalDao.getCountById(docId) <= 0) return false;
                journalDao.decrementCountById(docId);
                break;
            case Document.AV:
                if (avDao.getCountById(docId) <= 0) return false;
                avDao.decrementCountById(docId);
                break;
            default:
                return false;
        }

        long physId = documentPhysDao.getValidPhysicalId(docId, docType);
        String shelf = documentPhysDao.getShelfById(physId);
        documentPhysDao.inverseCanBooked(physId);
        bookingDao.add(new Booking(userId, physId, docType, shelf, System.nanoTime()));

        return true;
    }

}
