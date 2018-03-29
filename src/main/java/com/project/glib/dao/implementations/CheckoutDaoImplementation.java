package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.CheckoutRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Checkout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CheckoutDaoImplementation implements ModifyByLibrarian<Checkout> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CheckoutDaoImplementation.class);
    private static final String TYPE = Checkout.TYPE;
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
    public long getId(Checkout checkout) {
        return checkoutRepository.findAll().stream()
                .filter(c -> c.getDocPhysId() == checkout.getDocPhysId() &&
                        c.getUserId() == checkout.getUserId() &&
                        c.getCheckoutTime() == checkout.getCheckoutTime() &&
                        c.getReturnTime() == checkout.getReturnTime() &&
                        c.getShelf().equals(checkout.getShelf()))
                .findFirst().get().getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Checkout> getList() {
        List<Checkout> checkouts = checkoutRepository.findAll();

        for (Checkout checkout : checkouts) {
            logger.info(LIST + checkout);
        }

        return checkouts;
    }
}