package com.project.glib.service;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Checkout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RenewService {
    private final CheckoutDaoImplementation checkoutDao;

    @Autowired
    public RenewService(CheckoutDaoImplementation checkoutDao) {
        this.checkoutDao = checkoutDao;
    }

    /**
     * renew document by check out
     * @param checkout current check out
     * @return update check out if it possible
     */
    //TODO check rules to renew document
    public Checkout toRenewDocument(Checkout checkout) throws Exception {
        if (checkoutDao.getIsRenewedById(checkout.getId())) {
            throw new Exception("Sorry, but you try to renew already renewed " +
                    checkout.getDocType().toLowerCase() + ".");
        }

        checkout.setRenewed(true);
        checkout.setReturnTime(2 * checkout.getReturnTime() - checkout.getCheckoutTime());
        return checkout;
    }
}
