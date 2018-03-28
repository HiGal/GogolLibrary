package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.CheckoutRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Checkout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class CheckoutDaoImplementation implements ModifyByLibrarian<Checkout> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CheckoutDaoImplementation.class);
    private static final String TYPE = Checkout.TYPE;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private static final String ADD_CHECKOUT = TYPE + ADD;
    private static final String UPDATE_CHECKOUT = TYPE + UPDATE;
    private static final String REMOVE_CHECKOUT = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final CheckoutRepository checkoutRepository;

    @Autowired
    public CheckoutDaoImplementation(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public void add(Checkout checkout) {
        checkoutRepository.saveAndFlush(checkout);
        logger.info(ADD_CHECKOUT + checkout);
    }

    @Override
    public void update(Checkout checkout) {
        checkoutRepository.saveAndFlush(checkout);
        logger.info(UPDATE_CHECKOUT + checkout);
    }

    @Override
    public void remove(long checkoutId) {
        checkoutRepository.delete(checkoutId);
        logger.info(REMOVE_CHECKOUT + checkoutId);
    }

    @Override
    public Checkout getById(long checkoutId) {
        return checkoutRepository.findOne(checkoutId);
    }

    @Override
    public long getId(Checkout checkout) throws Exception {
        try {
            return checkoutRepository.findAll().stream()
                    .filter(c -> c.getDocPhysId() == checkout.getDocPhysId() &&
                            c.getUserId() == checkout.getUserId() &&
                            c.getCheckoutTime() == checkout.getCheckoutTime() &&
                            c.getReturnTime() == checkout.getReturnTime() &&
                            c.isRenewed() == checkout.isRenewed() &&
                            c.getDocType().equals(checkout.getDocType()) &&
                            c.getShelf().equals(checkout.getShelf()))
                    .findFirst().get().getId();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public boolean getIsRenewedById(long checkoutId) throws Exception {
        try {
            return checkoutRepository.findOne(checkoutId).isRenewed();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Checkout> getList() {
        try {
            List<Checkout> checkouts = checkoutRepository.findAll();

            for (Checkout checkout : checkouts) {
                logger.info(LIST + checkout);
            }

            return checkouts;
        } catch (NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public long getNumberOfCheckoutDocumentsByUser(long userId) throws Exception {
        try {
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getUserId() == userId)
                    .count();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public List<Checkout> getCheckoutsByUser(long userId) {
        try {
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getUserId() == userId)
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public boolean alreadyHasThisCheckout(long docId, String docType, long userId) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getUserId() == userId)
                .filter(checkout -> checkout.getDocPhysId() == docId)
                .anyMatch(checkout -> checkout.getDocType().equals(docType));
    }

    public boolean hasRenewedCheckout(long docPhysID) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getDocPhysId() == docPhysID)
                .anyMatch(Checkout::isRenewed);
    }

    public long getUserIdByDocPhysId(long docPhysId) throws Exception {
        try {
            return getByDocPhysId(docPhysId).getUserId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public Checkout getByDocPhysId(long docPhysId) throws Exception {
        try {
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getDocPhysId() == docPhysId)
                    .findFirst().get();
        } catch (NoSuchElementException | NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}