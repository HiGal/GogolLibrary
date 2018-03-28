package com.project.glib.service;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.stereotype.Service;

@Service
public class DocumentPhysicalService implements ModifyByLibrarianService<DocumentPhysical> {
    public static final String TYPE = DocumentPhysical.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation avDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    public DocumentPhysicalService(BookDaoImplementation bookDao,
                                   JournalDaoImplementation journalDao,
                                   AudioVideoDaoImplementation avDao,
                                   DocumentPhysicalDaoImplementation docPhysDao) {
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.avDao = avDao;
        this.docPhysDao = docPhysDao;
    }

    public void add(DocumentPhysical docPhys) throws Exception {
        checkValidParameters(docPhys);
        try {
            switch (docPhys.getDocType()) {
                case Document.BOOK:
                    bookDao.incrementCountById(docPhys.getIdDoc());
                    break;
                case Document.JOURNAL:
                    journalDao.incrementCountById(docPhys.getIdDoc());
                    break;
                case Document.AV:
                    avDao.incrementCountById(docPhys.getIdDoc());
                    break;
                default:
                    throw new Exception(TYPE_EXCEPTION);
            }
            docPhysDao.add(docPhys);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION + docPhys);
        }
    }

    public void update(DocumentPhysical docPhys) throws Exception {
        // TODO if librarian change count?
        checkValidParameters(docPhys);
        try {
            docPhysDao.update(docPhys);
        } catch (Exception e) {
            throw new Exception(UPDATE_EXCEPTION + docPhys);
        }
    }

    public void remove(long docPhysId) throws Exception {
        try {
            switch (docPhysDao.getTypeByID(docPhysId)) {
                case Document.BOOK:
                    bookDao.decrementCountById(docPhysId);
                    break;
                case Document.JOURNAL:
                    journalDao.decrementCountById(docPhysId);
                    break;
                case Document.AV:
                    avDao.decrementCountById(docPhysId);
                    break;
                default:
                    throw new Exception(TYPE_EXCEPTION);
            }
            docPhysDao.remove(docPhysId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION + docPhysId);
        }
    }

    @Override
    public void checkValidParameters(DocumentPhysical documentPhysical) throws Exception {
        if (documentPhysical.getIdDoc() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }

        if (!Document.isType(documentPhysical.getDocType())) {
            throw new Exception(TYPE_EXCEPTION);
        }

        if (documentPhysical.getShelf().equals("")) {
            throw new Exception(SHELF_EXCEPTION);
        }
    }
}
