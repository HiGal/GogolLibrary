package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.BookingRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Booking;
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
    private static final String TYPE = Booking.TYPE;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
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
    public long getId(Booking booking) throws Exception {
        try {
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
        } catch (NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Booking> getList() {
        try {
            List<Booking> bookings = bookingRepository.findAll();

            for (Booking booking : bookings) {
                logger.info(LIST + booking);
            }

            return bookings;
        } catch (NullPointerException e) {
            return new ArrayList<>();
        }
    }

    public long getNumberOfBookingsDocumentsByUser(long userId) {
        return getBookingsByUser(userId).size();
    }

    public List<Booking> getBookingsByUser(long userId) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getUserId() == userId)
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public boolean alreadyHasThisBooking(long docVirId, String docType, long userId) {
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getUserId() == userId)
                .filter(booking -> booking.getDocVirId() == docVirId)
                .anyMatch(booking -> booking.getDocType().equals(docType));
    }

    public boolean hasActiveBooking(long docPhysId, String docType) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getDocPhysId() == docPhysId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .anyMatch(Booking::isActive);
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean hasNotActiveBooking(long docPhysId, String docType) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getDocPhysId() == docPhysId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .anyMatch(booking -> !booking.isActive());
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public List<Booking> getListBookingsByDocVirIdAndDocType(long docVirId, String docType) {
        try {
            return bookingRepository.findAll().stream()
                    .filter(booking -> booking.getDocVirId() == docVirId)
                    .filter(booking -> booking.getDocType().equals(docType))
                    .collect(Collectors.toList());
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public Booking getBookingWithMaxPriority(long docVirId, String docType) {
        try {
            return bookingRepository.findByDocVirIdAndDocTypeOrderByPriority(docVirId, docType);
        } catch (NoSuchElementException e) {
            return null;
        }
    }
}