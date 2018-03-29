package com.project.glib.service;

import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import com.project.glib.model.Journal;
import org.springframework.stereotype.Service;

@Service
public class JournalService implements DocumentServiceInterface<Journal> {
    public static final String TYPE = Document.JOURNAL;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    private final JournalDaoImplementation journalDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    public JournalService(JournalDaoImplementation journalDao, DocumentPhysicalDaoImplementation docPhysDao) {
        this.journalDao = journalDao;
        this.docPhysDao = docPhysDao;
    }

    @Override
    public void add(Journal journal, String shelf) throws Exception {
        if (shelf.equals("")) throw new Exception(SHELF_EXCEPTION);
        add(journal);
        for (int i = 0; i < journal.getCount(); i++) {
            // TODO add keywords options
            docPhysDao.add(new DocumentPhysical(shelf, true, journal.getId(), Document.JOURNAL, null));
        }
    }

    private void add(Journal journal) throws Exception {
        checkValidParameters(journal);
        try {
            Journal existedJournal = journalDao.isAlreadyExist(journal);
            if (existedJournal == null) {
                journalDao.add(journal);
            } else {
                existedJournal.setCount(existedJournal.getCount() + journal.getCount());
                existedJournal.setPrice(journal.getPrice());
                journalDao.update(existedJournal);
            }
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    @Override
    public void update(Journal journal) throws Exception {
        checkValidParameters(journal);
        // TODO solve case then librarian change count of journals
        try {
            journalDao.update(journal);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    @Override
    public void remove(long journalId) throws Exception {
        try {
            docPhysDao.removeAllByDocId(journalId);
            journalDao.remove(journalId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    @Override
    public void checkValidParameters(Journal journal) throws Exception {
        if (journal.getPrice() <= 0) {
            throw new Exception(PRICE_EXCEPTION);
        }

        if (journal.getCount() < 0) {
            throw new Exception(COUNT_EXCEPTION);
        }

        if (journal.getIssue() <= 0) {
            throw new Exception(ISSUE_EXCEPTION);
        }

        if (journal.getTitle().equals("")) {
            throw new Exception(TITLE_EXCEPTION);
        }

        if (journal.getAuthor().equals("")) {
            throw new Exception(AUTHOR_EXCEPTION);
        }

        if (journal.getEditor().equals("")) {
            throw new Exception(EDITOR_EXCEPTION);
        }

        if (journal.getName().equals("")) {
            throw new Exception(NAME_EXCEPTION);
        }

        if (!isNote(journal.getNote())) {
            throw new Exception(NOTE_EXCEPTION);
        }
    }

    @Override
    public boolean isNote(String note) {
        return note.equals(Document.DEFAULT_NOTE) || note.equals(Document.REFERENCE);
    }

}
