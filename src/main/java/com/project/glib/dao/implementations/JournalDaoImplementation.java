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
        checkValidParameters(journal);
        try {
            if (!isAlreadyExist(journal)) {
                journalRepository.saveAndFlush(journal);
                logger.info("Journal successfully saved. Journal details : " + journal);
            } else {
                // TODO change method for finding existedJournal
                Journal existedJournal = journalRepository.findAll().stream()
                        .filter(j -> j.getTitle().equals(journal.getTitle())).collect(Collectors.toList()).get(0);

                logger.info("Try to add " + journal.getCount() + " copies of journal : " + existedJournal);
                existedJournal.setCount(existedJournal.getCount() + journal.getCount());
                update(existedJournal);
            }
        } catch (Exception e) {
            logger.info("Error in method add() in class JournalDaoImplementation");
            throw new Exception("Can't add this journal, something wrong");
        }
    }

    @Override
    public void update(Journal journal) throws Exception {
        checkValidParameters(journal);
        // TODO solve case then librarian change count of journals
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
    public void checkValidParameters(Journal journal) throws Exception {
        if (journal.getPrice() < 0) {
            throw new Exception("Price must be positive");
        }

        if (journal.getCount() < 0) {
            throw new Exception("Count must be positive");
        }

        if (journal.getIssue() < 0) {
            throw new Exception("Issue must be positive");
        }

        if (journal.getTitle().equals("")) {
            throw new Exception("Title must exist");
        }

        if (journal.getAuthor().equals("")) {
            throw new Exception("Author must exist");
        }

        if (journal.getEditor().equals("")) {
            throw new Exception("Editor must exist");
        }

        if (journal.getName().equals("")) {
            throw new Exception("Name must exist");
        }
    }

    @Override
    public boolean isAlreadyExist(Journal journal) {
        return journalRepository.existsAllByTitle(journal.getTitle());
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
            int i = journalRepository.findOne(journalId).getCount();
            Journal journal = journalRepository.findOne(journalId);
            journal.setCount(i - 1);
            journalRepository.saveAndFlush(journal);
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
            journalRepository.saveAndFlush(journal);
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
