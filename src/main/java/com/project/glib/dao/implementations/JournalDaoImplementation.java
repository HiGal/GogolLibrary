package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentDao;
import com.project.glib.dao.interfaces.JournalRepository;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import com.project.glib.model.Journal;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class JournalDaoImplementation implements DocumentDao<Journal> {
    private static final org.slf4j.Logger logger = (org.slf4j.Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private final JournalRepository journalRepository;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    @Autowired
    public JournalDaoImplementation(JournalRepository journalRepository, DocumentPhysicalDaoImplementation docPhysDao) {
        this.journalRepository = journalRepository;
        this.docPhysDao = docPhysDao;
    }

    /**
     * Add new item of Journal in library
     *
     * @param journal new Journal
     * @throws Exception
     */
    @Override
    public void add(Journal journal) throws Exception {
        checkValidParameters(journal);
        try {
            Journal existedJournal = isAlreadyExist(journal);
            if (existedJournal == null) {
                journalRepository.saveAndFlush(journal);
                logger.info("Journal successfully saved. Journal details : " + journal);
            } else {
                logger.info("Try to add " + journal.getCount() + " copies of journal : " + existedJournal);
                existedJournal.setCount(existedJournal.getCount() + journal.getCount());
                existedJournal.setPrice(journal.getPrice());
                update(existedJournal);
            }
        } catch (Exception e) {
            logger.info("Error in method add() in class JournalDaoImplementation");
            throw new Exception("Can't add this journal, something wrong");
        }
    }

    @Override
    public void add(Journal journal, String shelf) throws Exception {
        if (shelf.equals("")) throw new Exception("Shelf must exist");
        add(journal);
        for (int i = 0; i < journal.getCount(); i++) {
            // TODO add keywords options
            docPhysDao.add(new DocumentPhysical(shelf, true, journal.getId(), Document.JOURNAL, null));
        }
    }

    /**
     * Update existed journal or create if it not exist
     *
     * @param journal - updated Journal
     * @throws Exception
     */
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

    /**
     * Remove Journal from library
     *
     * @param journalId id of Journal
     * @throws Exception
     */
    @Override
    public void remove(long journalId) throws Exception {
        try {
            logger.info("Try to delete journal with journal id = " + journalId);
            docPhysDao.removeAllByDocId(journalId);
            journalRepository.delete(journalId);
        } catch (Exception e) {
            logger.info("Try to delete journal with wrong journal id = " + journalId);
            throw new Exception("Delete this journal not available, journal don't exist");
        }
    }

    @Override
    public void checkValidParameters(Journal journal) throws Exception {
        if (journal.getPrice() < 0) {
            throw new Exception("Price must be not negative");
        }

        if (journal.getCount() < 0) {
            throw new Exception("Count must be not negative");
        }

        if (journal.getIssue() <= 0) {
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

        if (!isNote(journal.getNote())) {
            throw new Exception("Invalid note");
        }
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
            Journal journal = journalRepository.findOne(journalId);
            journal.setCount(journal.getCount() - 1);
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
            Journal journal = journalRepository.findOne(journalId);
            journal.setCount(journal.getCount() + 1);
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

    public String getNote(long journalId) throws Exception {
        try {
            logger.info("Try to get note journal with journal id = " + journalId);
            return journalRepository.findOne(journalId).getNote();
        } catch (Exception e) {
            logger.info("Try to get note journal with wrong journal id = " + journalId);
            throw new Exception("Information not available, journal don't exist");
        }
    }

    @Override
    public List<Journal> getListCountNotZeroOrRenewed() {
        try {
            List<Journal> journals = journalRepository.findAll().stream().filter(journal -> journal.getCount() > 0).collect(Collectors.toList());

            for (Journal journal : journals) {
                logger.info("Journal list : " + journal);
            }

            return journals;
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Journal> getList() {
        try {
            List<Journal> journals = journalRepository.findAll();

            for (Journal journal : journals) {
                logger.info("Journal list : " + journal);
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
        } catch (NoSuchElementException | NullPointerException e) {
            throw new Exception("Journal does not exist");
        }
    }

    @Override
    public boolean isNote(String note) {
        return note.equals(Document.DEFAULT_NOTE) || note.equals(Document.REFERENCE);
    }
}
