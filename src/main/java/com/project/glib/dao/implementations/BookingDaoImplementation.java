package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookingRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Booking;
import com.project.glib.model.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class BookingDaoImplementation implements ModifyByLibrarian<Booking> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookingRepository bookingRepository;
    private final DocumentPhysicalDaoImplementation documentPhysicalDao;

    @Autowired
    public BookingDaoImplementation(BookingRepository bookingRepository, DocumentPhysicalDaoImplementation documentPhysicalDao) {
        this.bookingRepository = bookingRepository;
        this.documentPhysicalDao = documentPhysicalDao;
    }

    @Override
    public void add(Booking booking) throws Exception {
        checkValidParameters(booking);
        try {
            if (alreadyHasThisBooking(booking.getIdDoc(), booking.getDocType(), booking.getIdUser()))
                throw new Exception();
            bookingRepository.save(booking);
            logger.info("Booking successfully saved. Booking details : " + booking);
        } catch (Exception e) {
            logger.info("Try to add booking with wrong parameters. New booking information : " + booking);
            throw new Exception("Can't add this booking, some parameters are wrong");
        }
    }

    @Override
    public void update(Booking booking) throws Exception {
        checkValidParameters(booking);
        try {
            bookingRepository.saveAndFlush(booking);
            logger.info("Booking successfully update. Booking details : " + booking);
        } catch (Exception e) {
            logger.info("Try to update this booking, booking don't exist or some new booking parameters are wrong. " +
                    "Update booking information : " + booking);
            throw new Exception("Can't update this booking, booking don't exist or some new booking parameters are wrong");
        }
    }

    @Override
    public void remove(long bookingId) throws Exception {
        try {
            logger.info("Try to delete booking with booking id = " + bookingId);
            bookingRepository.delete(bookingId);
        } catch (Exception e) {
            logger.info("Try to delete booking with wrong booking id = " + bookingId);
            throw new Exception("Delete this booking not available, booking don't exist");
        }
    }

    @Override
    public void checkValidParameters(Booking booking) throws Exception {
        if (booking.getIdUser() <= 0) {
            throw new Exception("Invalid id user");
        }

        if (!Document.isType(booking.getDocType())) {
            throw new Exception("Invalid document type");
        }

        if (booking.getIdDoc() <= 0) {
            throw new Exception("Invalid id of physical document");
        }

        if (booking.getBookingDate() > System.nanoTime()) {
            throw new Exception("Booking date cannot be in future");
        }

        if (booking.getPriority() < 0) {
            throw new Exception("Priority must be not negative");
        }

        if (booking.getShelf().equals("")) {
            throw new Exception("Booking must has shelf");
        }
    }

    @Override
    public Booking getById(long bookingId) {
        // return null if not found
        return bookingRepository.findOne(bookingId);

    }

    @Override
    public long getId(Booking booking) throws Exception {
        try {
            return bookingRepository.findAll().stream()
                    .filter(b -> b.getPriority() == booking.getPriority() &&
                            b.getIdDoc() == booking.getIdDoc() &&
                            b.getIdUser() == booking.getIdUser() &&
                            b.getBookingDate() == booking.getBookingDate() &&
                            b.isActive() == booking.isActive() &&
                            b.getDocType().equals(booking.getDocType()) &&
                            b.getShelf().equals(booking.getShelf()))
                    .findFirst().get().getId();
        } catch (NoSuchElementException e) {
            throw new Exception("Booking does not exist");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> getList() {
        try {
            List<Booking> bookings = bookingRepository.findAll();

            for (Booking booking : bookings) {
                logger.info("Booking list : " + booking);
            }

            logger.info("Booking list successfully printed");
            return bookings;
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public long getNumberOfBookingsDocumentsByUser(long userId) throws Exception {
        try {
            return getBookingsByUser(userId).size();
        } catch (Exception e) {
            logger.info("Try to get numbers of bookings documents by user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public List<Booking> getBookingsByUser(long userId) throws Exception {
        try {
            logger.info("Try to get list of  bookings by user with user id = " + userId);
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getIdUser() == userId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("Try to get list of  bookings by user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public boolean alreadyHasThisBooking(long docId, String docType, long userId) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getIdUser() == userId)
                .filter(booking -> booking.getIdDoc() == docId)
                .anyMatch(booking -> booking.getDocType().equals(docType));
    }

    public boolean hasActiveBooking(long docVirtualId, String docType) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getIdDoc() == docVirtualId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .anyMatch(Booking::isActive);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<Booking> getListBookingsByIdDocAndDocType(long docId, String docType) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getIdDoc() == docId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public boolean hasNotActiveBooking(long docId, String docType) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getIdDoc() == docId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .anyMatch(booking -> !booking.isActive());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public Booking getBookingWithMaxPriority(long docVirtualId, String docType) {
        try {
            return bookingRepository.findByIdDocAndDocTypeOrderByPriority(docVirtualId, docType);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}