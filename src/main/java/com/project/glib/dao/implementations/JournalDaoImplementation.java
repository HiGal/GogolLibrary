package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.model.Journal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class JournalDaoImplementation implements DocumentDao<Journal> {
    private static final org.slf4j.Logger logger = (org.slf4j.Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final JournalRepository journalRepository;

    @Autowired
    public JournalDaoImplementation(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    @Override
    public void add(Journal journal) throws Exception {
        try {
            journalRepository.save(journal);
            logger.info("Journal successfully saved. Journal details : " + journal);
        } catch (Exception e) {
            logger.info("Try to add journal with wrong parameters. New journal information : " + journal);
            throw new Exception("Can't add this journal, some parameters are wrong");
        }
    }

    @Override
    public void update(Journal journal) throws Exception {
        try {
            journalRepository.saveAndFlush(journal);
            logger.info("Journal successfully update. Journal details : " + journal);
        } catch (Exception e) {
            logger.info("Try to update this journal, journal don't exist or some new journal parameters are wrong. " +
                    "Update journal information : " + journal);
            throw new Exception("Can't update this journal, journal don't exist or some new journal parameters are wrong");
        }
    }

    @Override
    public void remove(long journalId) throws Exception {
        try {
            logger.info("Try to delete journal with journal id = " + journalId);
            journalRepository.delete(journalId);
        } catch (Exception e) {
            logger.info("Try to delete journal with wrong journal id = " + journalId);
            throw new Exception("Delete this journal not available, journal don't exist");
        }
    }

    @Override
    public Journal getById(long journalId) throws Exception {
        try {
            logger.info("Try to get count of journal with journal id = " + journalId);
            return journalRepository.findOne(journalId);
        } catch (Exception e) {
            logger.info("Try to get count of journal with wrong journal id = " + journalId);
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public int getCountById(long journalId) throws Exception {
        try {
            logger.info("Try to get count of journal with journal id = " + journalId);
            return journalRepository.findOne(journalId).getCount();
        } catch (Exception e) {
            logger.info("Try to get count of journal with wrong journal id = " + journalId);
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public void decrementCountById(long journalId) throws Exception {
        try {
            logger.info("Try to decrement count of journal with journal id = " + journalId);
            journalRepository.findOne(journalId).setCount(journalRepository.findOne(journalId).getCount() - 1);
        } catch (Exception e) {
            logger.info("Try to decrement count of journal with wrong journal id = " + journalId);
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public void incrementCountById(long journalId) throws Exception {
        try {
            logger.info("Try to increment count of journal with journal id = " + journalId);
            int i = journalRepository.findOne(journalId).getCount();
            Journal journal = journalRepository.findOne(journalId);
            journal.setCount(i + 1);
            journalRepository.save(journal);
        } catch (Exception e) {
            logger.info("Try to increment count of journal with wrong journal id = " + journalId);
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public int getPriceById(long journalId) throws Exception {
        try {
            logger.info("Try to get price of journal with journal id = " + journalId);
            return journalRepository.findOne(journalId).getPrice();
        } catch (Exception e) {
            logger.info("Try to get price of journal with wrong journal id = " + journalId);
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public List<Journal> getListCountNotZeroOrRenewed() {
        List<Journal> journals = journalRepository.findAll().stream().filter(journal -> journal.getCount() > 0).collect(Collectors.toList());

        for (Journal journal : journals) {
            logger.info("Journal list : " + journal);
        }

        return journals;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Journal> getList() {
        List<Journal> journals = journalRepository.findAll();

        for (Journal journal : journals) {
            logger.info("Journal list : " + journal);
        }

        return journals;
    }
}
