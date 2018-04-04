package com.project.glib.service;

import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DocumentPhysicalService implements ModifyByLibrarianService<DocumentPhysical> {
    public static final String TYPE = DocumentPhysical.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    public DocumentPhysicalService(DocumentPhysicalDaoImplementation docPhysDao) {
        this.docPhysDao = docPhysDao;
    }

    public void add(DocumentPhysical docPhys) throws Exception {
        checkValidParameters(docPhys);
        try {
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
            docPhysDao.remove(docPhysId);
        } catch (Exception e) {
            throw new Exception(REMOVE_EXCEPTION + docPhysId);
        }
    }

    /**
     * Validator for physical documents
     *
     * @param documentPhysical model of physical documents
     * @throws Exception
     */
    @Override
    public void checkValidParameters(DocumentPhysical documentPhysical) throws Exception {
        if (documentPhysical.getDocVirId() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }

        if (!Document.isType(documentPhysical.getDocType())) {
            throw new Exception(TYPE_EXCEPTION);
        }

        if (documentPhysical.getShelf().equals("")) {
            throw new Exception(SHELF_EXCEPTION);
        }
    }

    @Override
    public DocumentPhysical getById(long docPhysId) {
        return docPhysDao.getById(docPhysId);
    }

    @Override
    public long getId(DocumentPhysical docPhys) throws Exception {
        try {
            return docPhysDao.getId(docPhys);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentPhysical> getList() {
        try {
            return docPhysDao.getList();
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public void removeAllByDocVirIdAndDocType(long docVirId, String docType) throws Exception {
        try {
            List<DocumentPhysical> docPhysList = getList().stream()
                    .filter(doc -> doc.getDocVirId() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .collect(Collectors.toList());

            for (DocumentPhysical docPhys : docPhysList) {
                remove(docPhys.getId());
            }

        } catch (NoSuchElementException | NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public long getValidPhysId(long docVirId, String docType) throws Exception {
        try {
            return getList().stream()
                    .filter(doc -> doc.getDocVirId() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .filter(DocumentPhysical::isCanBooked).findFirst().get().getId();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public void inverseCanBooked(long docPhysId) {
        try {
            DocumentPhysical docPhys = getById(docPhysId);
            docPhys.setCanBooked(!docPhys.isCanBooked());
            update(docPhys);
        } catch (Exception ignored) {
        }
    }

    public String getShelfById(long docPhysId) throws Exception {
        try {
            return getById(docPhysId).getShelf();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public long getDocVirIdById(long docPhysId) throws Exception {
        try {
            return getById(docPhysId).getDocVirId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getTypeById(long docPhysId) throws Exception {
        try {
            return getById(docPhysId).getDocType();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public int getCount(long docVirId, String docType) throws Exception {
        if (!Document.isType(docType)) throw new Exception(TYPE_EXCEPTION);
        try {
            return (int) getList().stream()
                    .filter(doc -> doc.getDocVirId() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .count();
        } catch (NullPointerException | NoSuchElementException e) {
            return 0;
        }
    }

    public int getCount(DocumentPhysical docPhys) throws Exception {
        return getCount(docPhys.getDocVirId(), docPhys.getDocType());
    }

    public List<DocumentPhysical> getByDocVirIdAndDocType(long docVirId, String docType) {
        try {
            return getList().stream()
                    .filter(doc -> doc.getDocVirId() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }
}
