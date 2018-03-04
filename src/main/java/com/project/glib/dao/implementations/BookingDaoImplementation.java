package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookingRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class BookingDaoImplementation implements ModifyByLibrarian<Booking> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingDaoImplementation(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void add(Booking booking) {
        bookingRepository.save(booking);
        logger.info("Booking successfully saved. Booking details : " + booking);
    }

    public void update(Booking booking) {
        bookingRepository.save(booking);
        logger.info("Booking successfully update. Booking details : " + booking);
    }

    public void remove(long bookingId) {
        bookingRepository.delete(bookingId);
    }

    public Booking getById(long bookingId) {
        return bookingRepository.findOne(bookingId);
    }

    @SuppressWarnings("unchecked")
    public List<Booking> getList() {
        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            logger.info("Booking list : " + booking);
        }

        return bookings;
    }

    public long getNumberOfBookingsDocumentsByUser(long userId) {
        return getBookingsByUser(userId).length;
    }

    public Booking[] getBookingsByUser(long userId) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getIdUser() == userId)
                .toArray(Booking[]::new);
    }

    public boolean alreadyHasThisBooking(long docId, String docType, long userId) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getIdUser() == userId)
                .filter(booking -> booking.getIdDoc() == docId)
                .anyMatch(booking -> booking.getDocType().equals(docType));
    }
}