package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentPhysicalRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class DocumentPhysicalDaoImplementation implements ModifyByLibrarian<DocumentPhysical> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DocumentPhysicalDaoImplementation.class);
    private static final String TYPE = DocumentPhysical.TYPE;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;
    private static final String ADD_DOCUMENT = TYPE + ADD;
    private static final String UPDATE_DOCUMENT = TYPE + UPDATE;
    private static final String REMOVE_DOCUMENT = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final DocumentPhysicalRepository documentPhysicalRepository;

    @Autowired
    public DocumentPhysicalDaoImplementation(DocumentPhysicalRepository documentPhysicalRepository) {
        this.documentPhysicalRepository = documentPhysicalRepository;
    }

    @Override
    public void add(DocumentPhysical docPhys) {
        documentPhysicalRepository.saveAndFlush(docPhys);
        logger.info(ADD_DOCUMENT + docPhys);
    }

    @Override
    public void update(DocumentPhysical docPhys) {
        documentPhysicalRepository.saveAndFlush(docPhys);
        logger.info(UPDATE_DOCUMENT + docPhys);
    }

    @Override
    public void remove(long docPhysId) {
        documentPhysicalRepository.delete(docPhysId);
        logger.info(REMOVE_DOCUMENT + docPhysId);
    }

    @Override
    public DocumentPhysical getById(long docPhysId) {
        return documentPhysicalRepository.findOne(docPhysId);

    }

    @Override
    public long getId(DocumentPhysical docPhys) throws Exception {
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docPhys.getIdDoc() &&
                            doc.isCanBooked() == docPhys.isCanBooked() &&
                            doc.getDocType().equals(docPhys.getDocType()) &&
                            doc.getShelf().equals(docPhys.getShelf()))
                    .findFirst().get().getId();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentPhysical> getList() {
        try {
            List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll();

            for (DocumentPhysical documentPhysical : documentPhysicals) {
                logger.info(LIST + documentPhysical);
            }
            return documentPhysicals;
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }

    public void removeByDocIdAndDocType(long docVirId, String docType) throws Exception {
        try {
            long physID = documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .findFirst().get().getId();
            documentPhysicalRepository.delete(physID);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public long getValidPhysId(long docVirId, String docType) throws Exception {
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .filter(DocumentPhysical::isCanBooked).findFirst().get().getId();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public void inverseCanBooked(long docPhysId) {
        try {
            DocumentPhysical docPhys = documentPhysicalRepository.findOne(docPhysId);
            boolean canBooked = !docPhys.isCanBooked();
            docPhys.setCanBooked(canBooked);
            documentPhysicalRepository.saveAndFlush(docPhys);
        } catch (NullPointerException | NoSuchElementException ignored) {
        }
    }

    public String getShelfById(long docPhysId) throws Exception {
        try {
            return documentPhysicalRepository.findOne(docPhysId).getShelf();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public long getDocIdByPhysDocument(long docPhysId) throws Exception {
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getId() == docPhysId)
                    .findFirst().get().getIdDoc();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }


    public void removeAllByDocId(long docVirId) throws Exception {
        try {
            List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docVirId).collect(Collectors.toList());
            for (DocumentPhysical doc : documentPhysicals) {
                remove(doc.getId());
            }
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }

    }

    public long getDocIdByID(long docPhysId) throws Exception {
        try {
            return documentPhysicalRepository.findOne(docPhysId).getIdDoc();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public String getTypeByID(long docPhysId) throws Exception {
        try {
            return documentPhysicalRepository.findOne(docPhysId).getDocType();
        } catch (NullPointerException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    public int getCount(long docVirId, String docType) throws Exception {
        if (!Document.isType(docType)) throw new Exception(TYPE_EXCEPTION);
        try {
            return (int) documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .count();
        } catch (NullPointerException | NoSuchElementException e) {
            return 0;
        }
    }

    public int getCount(DocumentPhysical docPhys) throws Exception {
        return getCount(docPhys.getIdDoc(), docPhys.getDocType());
    }

    public List<DocumentPhysical> getByDocVirIdAndDocType(long docVirId, String docType) {
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docVirId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .collect(Collectors.toList());
        } catch (NullPointerException | NoSuchElementException e) {
            return new ArrayList<>();
        }
    }
}
