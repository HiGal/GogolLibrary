package com.project.glib.service;

import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import com.project.glib.model.Journal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class JournalService implements DocumentServiceInterface<Journal> {
    public static final String TYPE = Document.JOURNAL;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private final JournalDaoImplementation journalDao;
    private final DocumentPhysicalService docPhysService;

    public JournalService(JournalDaoImplementation journalDao, DocumentPhysicalService docPhysService) {
        this.journalDao = journalDao;
        this.docPhysService = docPhysService;
    }

    @Override
    public void add(Journal journal, String shelf) throws Exception {
        if (shelf.equals("")) throw new Exception(SHELF_EXCEPTION);
        add(journal);
        for (int i = 0; i < journal.getCount(); i++) {
            // TODO add keywords options
            docPhysService.add(new DocumentPhysical(shelf, true, journal.getId(), Document.JOURNAL));
        }
    }

    private void add(Journal journal) throws Exception {
        checkValidParameters(journal);
        try {
            Journal existedJournal = isAlreadyExist(journal);
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
            Journal existedJournal = isAlreadyExist(journal);
            if (existedJournal != null && !existedJournal.equals(journal)) {
                remove(journal.getId());
                existedJournal.setCount(existedJournal.getCount() + journal.getCount());
                existedJournal.setPrice(journal.getPrice());
                journal = existedJournal;
            }
            journalDao.update(journal);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION);
        }
    }

    @Override
    public void remove(long journalId) throws Exception {
        try {
            docPhysService.removeAllByDocVirIdAndDocType(journalId, Document.JOURNAL);
            journalDao.remove(journalId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION);
        }
    }

    @Override
    public void removeCopy(long journalId, long copyId) throws Exception {
        docPhysService.remove(copyId);
        decrementCountById(journalId);
    }

    @Override
    public void removeAllCopiesByShelf(long journalId, String shelf) throws Exception {
        List<DocumentPhysical> docPhysList = docPhysService.getByDocVirIdAndDocType(journalId, Document.JOURNAL)
                .stream().filter(doc -> doc.getShelf().equals(shelf))
                .collect(Collectors.toList());

        for (DocumentPhysical docPhys : docPhysList) removeCopy(journalId, docPhys.getId());
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

    @Override
    public Journal isAlreadyExist(Journal journal) {
        try {
            return journalDao.isAlreadyExist(journal);
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    @Override
    public Journal getById(long journalId) {
        return journalDao.getById(journalId);
    }

    @Override
    public int getCountById(long journalId) throws Exception {
        try {
            return getById(journalId).getCount();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    public void decrementCountById(long journalId) {
        Journal journal = getById(journalId);
        journal.setCount(journal.getCount() - 1);
        journalDao.update(journal);
    }

    @Override
    public void incrementCountById(long journalId) {
        Journal journal = getById(journalId);
        journal.setCount(journal.getCount() + 1);
        journalDao.update(journal);
    }

    @Override
    public int getPriceById(long journalId) throws Exception {
        try {
            return getById(journalId).getPrice();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getNote(long journalId) throws Exception {
        try {
            return getById(journalId).getNote();
        } catch (Exception e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Journal> getList() {
        try {
            return journalDao.getList();
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public long getId(Journal journal) throws Exception {
        try {
            return journalDao.getId(journal);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }
}
