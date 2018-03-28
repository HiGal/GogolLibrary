package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Document;
import com.project.glib.model.Journal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Repository
public class JournalDaoImplementation implements DocumentDao<Journal> {
    private static final org.slf4j.Logger logger = (org.slf4j.Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = Document.JOURNAL;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private static final String ADD_JOURNAL = TYPE + ADD;
    private static final String UPDATE_JOURNAL = TYPE + UPDATE;
    private static final String REMOVE_JOURNAL = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final JournalRepository journalRepository;

    @Autowired
    public JournalDaoImplementation(JournalRepository journalRepository) {
        this.journalRepository = journalRepository;
    }

    /**
     * Add new item of Journal in library
     *
     * @param journal new Journal
     */
    @Override
    public void add(Journal journal) {
        journalRepository.saveAndFlush(journal);
        logger.info(ADD_JOURNAL + journal);
    }

    /**
     * Update existed journal or create if it not exist
     *
     * @param journal - updated Journal
     */
    @Override
    public void update(Journal journal) {
        journalRepository.saveAndFlush(journal);
        logger.info(UPDATE_JOURNAL + journal);
    }

    /**
     * Remove Journal from library
     *
     * @param journalId id of Journal
     */
    @Override
    public void remove(long journalId) {
        journalRepository.delete(journalId);
        logger.info(REMOVE_JOURNAL + journalId);
    }

    @Override
    public Journal isAlreadyExist(Journal journal) {
        try {
            return journalRepository.findAll().stream()
                    .filter(j -> j.getName().equals(journal.getName()) &&
                            j.getAuthor().equals(journal.getAuthor()) &&
                            j.getEditor().equals(journal.getEditor()) &&
                            j.getTitle().equals(journal.getTitle()) &&
                            j.getIssue() == journal.getIssue() &&
                            j.getNote().equals(journal.getNote()))
                    .findFirst().get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Journal getById(long journalId) {
        return journalRepository.findOne(journalId);
    }

    @Override
    public int getCountById(long journalId) throws Exception {
        try {
            return journalRepository.findOne(journalId).getCount();
        } catch (NullPointerException e) {
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public void decrementCountById(long journalId) {
        Journal journal = journalRepository.findOne(journalId);
        journal.setCount(journal.getCount() - 1);
        journalRepository.saveAndFlush(journal);
    }

    @Override
    public void incrementCountById(long journalId) {
            Journal journal = journalRepository.findOne(journalId);
            journal.setCount(journal.getCount() + 1);
            journalRepository.saveAndFlush(journal);
    }

    @Override
    public int getPriceById(long journalId) throws Exception {
        try {
            return journalRepository.findOne(journalId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getNote(long journalId) throws Exception {
        try {
            return journalRepository.findOne(journalId).getNote();
        } catch (Exception e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

//    @Override
//    public List<Journal> getListCountNotZeroOrRenewed() {
//        try {
//            List<Journal> journals = journalRepository.findAll().stream().filter(journal -> journal.getCount() > 0).collect(Collectors.toList());
//
//            for (Journal journal : journals) {
//                logger.info("Journal list : " + journal);
//            }
//
//            return journals;
//        } catch (NoSuchElementException | NullPointerException e) {
//            return new ArrayList<>();
//        }
//    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Journal> getList() {
        try {
            List<Journal> journals = journalRepository.findAll();

            for (Journal journal : journals) {
                logger.info(LIST + journal);
            }

            return journals;
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public long getId(Journal journal) throws Exception {
        try {
            return isAlreadyExist(journal).getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
