package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Document;
import org.springframework.beans.factory.annotation.Autowired;

public class BookingService {
//TODO move this piece of code to check out system
//    public final long FOUR_WEEKS = 2419200000L;
//    public final long THREE_WEEKS = 1814400000L;
//    public final long TWO_WEEKS = 1209600000L;

    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final BookingDaoImplementation bookingDao;
    private final UsersDaoImplementation usersDao;
    //    private final BookPhysicalDaoImplementation bookPhysDao;
//    private final JournalPhysicalDaoImplementation journalPhysDao;
//    private final AVPhysicalDaoImplementation avPhysDao;
    private final DocumentPhysicalDaoImplementation documentPhysDao;

//    @Autowired
//    BookingService(BookDaoImplementation bookDao,
//                   JournalDaoImplementation journalDao,
//                   AudioVideoDaoImplementation avDao,
//                   BookingDaoImplementation bookingDao,
//                   UsersDaoImplementation usersDao,
//                   BookPhysicalDaoImplementation bookPhysDao,
//                   JournalPhysicalDaoImplementation journalPhysDao,
//                   AVPhysicalDaoImplementation avPhysDao) {
//        this.bookDao = bookDao;
//        this.journalDao = journalDao;
//        this.avDao = avDao;
//        this.bookingDao = bookingDao;
//        this.usersDao = usersDao;
//        this.bookPhysDao = bookPhysDao;
//        this.journalPhysDao = journalPhysDao;
//        this.avPhysDao = avPhysDao;
//    }

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
//        long physId;

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
        documentPhysDao.inverseCanBooked(physId);

//        switch (docType) {
//            case Document.BOOK:
//                physId = toBookBook(docId);
//                break;
//            case Document.JOURNAL:
//                physId = toBookJournal(docId);
//                break;
//            case Document.AV:
//                physId = toBookAV(docId);
//                break;
//            default:
//                return false;
//        }

        bookingDao.add(new Booking(userId, physId, docType, System.nanoTime()));

        return true;
    }

//    private long toBookBook(long bookId) {
//        if (bookDao.getCountById(bookId) <= 0){
//            return 0;
//        }
//
//        long validId = bookPhysDao.getValidPhysicalId(bookId);
//
//        bookDao.decrementCountById(bookId);
//        bookPhysDao.inverseCanBooked(validId);
//
//        return validId;
//    }
//
//    private long toBookJournal(long journalId) {
//        if (journalDao.getCountById(journalId) <= 0){
//            return 0;
//        }
//
//        long validId = journalPhysDao.getValidPhysicalId(journalId);
//
//        journalDao.decrementCountById(journalId);
//        journalPhysDao.inverseCanBooked(validId);
//
//        return validId;
//    }
//
//    private long toBookAV(long avId) {
//        if (avDao.getCountById(avId) <= 0){
//            return 0;
//        }
//
//        long validId = avPhysDao.getValidPhysicalId(avId);
//
//        avDao.decrementCountById(avId);
//        avPhysDao.inverseCanBooked(validId);
//
//        return validId;
//    }

//        String shelf = bookPhysDao.getShelfById(physId);
//TODO move this piece of code to check out system
//        long additionalTime;
//        if (bookDao.getIsBestseller(bookId)){
//            additionalTime = TWO_WEEKS;
//        }else {
//            switch (usersDao.getTypeById(userId)) {
//                case "faculty":
//                    additionalTime = FOUR_WEEKS;
//                    break;
//                case "patron":
//                    additionalTime = THREE_WEEKS;
//                    break;
//                default:
//                    additionalTime = 0;
//            }
//        }
//
//        Timestamp timeToReturn = new Timestamp(System.nanoTime() + additionalTime);
}
