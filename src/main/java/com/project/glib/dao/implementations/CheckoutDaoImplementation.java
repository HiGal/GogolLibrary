package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.CheckoutRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Checkout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CheckoutDaoImplementation implements ModifyByLibrarian<Checkout> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CheckoutDaoImplementation.class);
    private final CheckoutRepository checkoutRepository;

    @Autowired
    public CheckoutDaoImplementation(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    public void add(Checkout checkout) {
        checkoutRepository.save(checkout);
        logger.info("Checkout successfully saved. Checkout details : " + checkout);
    }

    public void update(Checkout checkout) {
        checkoutRepository.save(checkout);
        logger.info("Checkout successfully update. Checkout details : " + checkout);
    }

    public void remove(long checkoutId) {
        checkoutRepository.delete(checkoutId);
    }

    public Checkout getById(long checkoutId) {
        return checkoutRepository.findOne(checkoutId);
    }

    public boolean getIsRenewedById(long checkoutId) {
        return checkoutRepository.findOne(checkoutId).isRenewed();
    }

    @SuppressWarnings("unchecked")
    public List<Checkout> getList() {
        List<Checkout> checkouts = checkoutRepository.findAll();

        for (Checkout checkout : checkouts) {
            logger.info("Checkout list : " + checkout);
        }

        return checkouts;
    }

    public long getNumberOfCheckoutDocumentsByUser(long userId) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdUser() == userId)
                .count();
    }

    public Checkout[] getCheckoutsByUser(long userId) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdUser() == userId)
                .toArray(Checkout[]::new);
    }
}
