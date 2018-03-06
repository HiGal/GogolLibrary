package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookingRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookingDaoImplementation implements ModifyByLibrarian<Booking> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingDaoImplementation(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void add(Booking booking) throws Exception {
        try {
            bookingRepository.save(booking);
            logger.info("Booking successfully saved. Booking details : " + booking);
        } catch (Exception e) {
            logger.info("Try to add booking with wrong parameters. New booking information : " + booking);
            throw new Exception("Can't add this booking, some parameters are wrong");
        }
    }

    public void update(Booking booking) throws Exception {
        try {
            bookingRepository.saveAndFlush(booking);
            logger.info("Booking successfully update. Booking details : " + booking);
        } catch (Exception e) {
            logger.info("Try to update this booking, booking don't exist or some new booking parameters are wrong. " +
                    "Update booking information : " + booking);
            throw new Exception("Can't update this booking, booking don't exist or some new booking parameters are wrong");
        }
    }

    public void remove(long bookingId) throws Exception {
        try {
            logger.info("Try to delete booking with booking id = " + bookingId);
            bookingRepository.delete(bookingId);
        } catch (Exception e) {
            logger.info("Try to delete booking with wrong booking id = " + bookingId);
            throw new Exception("Delete this booking not available, booking don't exist");
        }
    }

    public Booking getById(long bookingId) throws Exception {
        try {
            logger.info("Try to get booking by booking id = " + bookingId);
            return bookingRepository.findOne(bookingId);
        } catch (Exception e) {
            logger.info("Try to get booking with wrong booking id = " + bookingId);
            throw new Exception("Information not available, booking don't exist");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Booking> getList() {
        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            logger.info("Booking list : " + booking);
        }

        logger.info("Booking list successfully printed");
        return bookings;
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

}