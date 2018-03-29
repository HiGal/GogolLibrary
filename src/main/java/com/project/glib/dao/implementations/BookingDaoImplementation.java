package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookingRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Booking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingDaoImplementation implements ModifyByLibrarian<Booking> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = Booking.TYPE;
    private static final String ADD_BOOKING = TYPE + ADD;
    private static final String UPDATE_BOOKING = TYPE + UPDATE;
    private static final String REMOVE_BOOKING = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingDaoImplementation(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void add(Booking booking) {
        bookingRepository.saveAndFlush(booking);
        logger.info(ADD_BOOKING + booking);
    }

    @Override
    public void update(Booking booking) {
        bookingRepository.saveAndFlush(booking);
        logger.info(UPDATE_BOOKING + booking);
    }

    @Override
    public void remove(long bookingId) {
        bookingRepository.delete(bookingId);
        logger.info(REMOVE_BOOKING + bookingId);
    }

    @Override
    public Booking getById(long bookingId) {
        return bookingRepository.findOne(bookingId);
    }

    @Override
    public long getId(Booking booking) {
        return bookingRepository.findAll().stream()
                .filter(b -> b.getPriority() == booking.getPriority() &&
                        b.getDocVirId() == booking.getDocVirId() &&
                        b.getDocPhysId() == booking.getDocPhysId() &&
                        b.getUserId() == booking.getUserId() &&
                        b.getBookingDate() == booking.getBookingDate() &&
                        b.isActive() == booking.isActive() &&
                        b.getDocType().equals(booking.getDocType()) &&
                        b.getShelf().equals(booking.getShelf()))
                .findFirst().get().getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> getList() {
        List<Booking> bookings = bookingRepository.findAll();

        for (Booking booking : bookings) {
            logger.info(LIST + booking);
        }

        return bookings;
    }

    public Booking getBookingWithMaxPriority(long docVirId, String docType) {
        return bookingRepository.findByDocVirIdAndDocTypeOrderByPriority(docVirId, docType);
    }
}