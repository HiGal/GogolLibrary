package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.CheckoutRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Checkout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CheckoutDaoImplementation implements ModifyByLibrarian<Checkout> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(CheckoutDaoImplementation.class);
    private final CheckoutRepository checkoutRepository;

    @Autowired
    public CheckoutDaoImplementation(CheckoutRepository checkoutRepository) {
        this.checkoutRepository = checkoutRepository;
    }

    @Override
    public void add(Checkout checkout) throws Exception {
        try {
            checkoutRepository.save(checkout);
            logger.info("Checkout successfully saved. Checkout details : " + checkout);
        } catch (Exception e) {
            logger.info("Try to add check out with wrong parameters. New check out information : " + checkout);
            throw new Exception("Can't add this check out, some parameters are wrong");
        }
    }

    @Override
    public void update(Checkout checkout) throws Exception {
        try {
            checkoutRepository.saveAndFlush(checkout);
            logger.info("Checkout successfully update. Checkout details : " + checkout);
        } catch (Exception e) {
            logger.info("Try to update this check out, check out don't exist or some new check out parameters are wrong. " +
                    "Update check out information : " + checkout);
            throw new Exception("Can't update this check out, check out don't exist or some new check out parameters are wrong");
        }
    }

    @Override
    public void remove(long checkoutId) throws Exception {
        try {
            logger.info("Try to delete check out with check out id = " + checkoutId);
            checkoutRepository.delete(checkoutId);
        } catch (Exception e) {
            logger.info("Try to delete check out with wrong check out id = " + checkoutId);
            throw new Exception("Delete this check out not available, check out don't exist");
        }
    }

    @Override
    public Checkout getById(long checkoutId) throws Exception {
        try {
            logger.info("Try to get check out with check out id = " + checkoutId);
            return checkoutRepository.findOne(checkoutId);
        } catch (Exception e) {
            logger.info("Try to get check out with wrong check out id = " + checkoutId);
            throw new Exception("Information not available, check out don't exist");
        }
    }

    public boolean getIsRenewedById(long checkoutId) throws Exception {
        try {
            logger.info("Try to get is renewed check out by check out id = " + checkoutId);
            return checkoutRepository.findOne(checkoutId).isRenewed();
        } catch (Exception e) {
            logger.info("Try to get is renewed check out by wrong check out id = " + checkoutId);
            throw new Exception("Information not available, check out don't exist");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Checkout> getList() {
        List<Checkout> checkouts = checkoutRepository.findAll();

        for (Checkout checkout : checkouts) {
            logger.info("Checkout list : " + checkout);
        }

        logger.info("Check out list successfully printed");
        return checkouts;
    }

    public long getNumberOfCheckoutDocumentsByUser(long userId) throws Exception {
        try {
            logger.info("Try to get numbers of check outs documents by user with user id = " + userId);
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getIdUser() == userId)
                    .count();
        } catch (Exception e) {
            logger.info("Try to get numbers of check outs documents by user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public List<Checkout> getCheckoutsByUser(long userId) throws Exception {
        try {
            logger.info("Try to get list of check out by user with user id = " + userId);
            return checkoutRepository.findAll().stream()
                    .filter(checkout -> checkout.getIdUser() == userId)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.info("Try to get list of check out by user with wrong user id = " + userId);
            throw new Exception("Information not available, user don't exist");
        }
    }

    public boolean alreadyHasThisCheckout(long docId, String docType, long userId) {
        logger.info("Get check has user with id = " + userId +
                " already check out " + docType.toLowerCase() + " with virtual id = " + docId);
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdUser() == userId)
                .filter(checkout -> checkout.getIdDoc() == docId)
                .anyMatch(checkout -> checkout.getDocType().equals(docType));
    }

    public boolean hasRenewedCheckout(long docPhysID) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdDoc() == docPhysID)
                .anyMatch(Checkout::isRenewed);
    }

    public long getUserIdByDocPhysId(long docPhysID) {
        return checkoutRepository.findAll().stream()
                .filter(checkout -> checkout.getIdDoc() == docPhysID)
                .findFirst().get().getIdUser();
    }
}
