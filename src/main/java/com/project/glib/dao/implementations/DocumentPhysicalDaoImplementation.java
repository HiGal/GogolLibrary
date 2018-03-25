package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentPhysicalRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Repository
public class DocumentPhysicalDaoImplementation implements ModifyByLibrarian<DocumentPhysical> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DocumentPhysicalDaoImplementation.class);
    private final DocumentPhysicalRepository documentPhysicalRepository;

    @Autowired
    public DocumentPhysicalDaoImplementation(DocumentPhysicalRepository documentPhysicalRepository) {
        this.documentPhysicalRepository = documentPhysicalRepository;
    }

    @Override
    public void add(DocumentPhysical documentPhysical) throws Exception {
        checkValidParameters(documentPhysical);
        try {
            documentPhysicalRepository.saveAndFlush(documentPhysical);
            logger.info("Document successfully saved. Document details : " + documentPhysical);
        } catch (Exception e) {
            throw new Exception("Can not add this document. Document details : " + documentPhysical);
        }
    }

    @Override
    public void update(DocumentPhysical documentPhysical) throws Exception {
        // TODO if librarian change count?
        checkValidParameters(documentPhysical);
        try {
            documentPhysicalRepository.saveAndFlush(documentPhysical);
            logger.info("Document successfully update. Document details : " + documentPhysical);
        } catch (Exception e) {
            throw new Exception("Can not update this document. Document details : " + documentPhysical);
        }
    }

    @Override
    public void remove(long id) throws Exception {
        try {
            documentPhysicalRepository.delete(id);
            logger.info("Delete physical document with id = " + id);
        } catch (Exception e) {
            throw new Exception("Can not delete document with this id = " + id);
        }
    }

    @Override
    public void checkValidParameters(DocumentPhysical documentPhysical) throws Exception {
        if (documentPhysical.getIdDoc() <= 0) {
            throw new Exception("Invalid virtual document id");
        }

        if (!Document.isType(documentPhysical.getDocType())) {
            throw new Exception("Invalid document type");
        }

        if (documentPhysical.getShelf().equals("")) {
            throw new Exception("Must have shelf");
        }
    }


    @Override
    public DocumentPhysical getById(long documentPhysicalId) {
        return documentPhysicalRepository.findOne(documentPhysicalId);

    }

    @Override
    public long getId(DocumentPhysical documentPhysical) throws Exception {
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == documentPhysical.getIdDoc() &&
                            doc.isCanBooked() == documentPhysical.isCanBooked() &&
                            doc.getDocType().equals(documentPhysical.getDocType()) &&
                            doc.getShelf().equals(documentPhysical.getShelf()))
                    .findFirst().get().getId();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception("Document does not exist");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentPhysical> getList() {
        try {
            List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll();

            for (DocumentPhysical documentPhysical : documentPhysicals) {
                logger.info("Document list : " + documentPhysical);
            }
            return documentPhysicals;
        } catch (NullPointerException | NoSuchElementException e) {
            return null;
        }
    }

    public void removeByDocIdAndDocType(long docId, String docType) throws Exception {
        try {
            long physID = documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .findFirst().get().getId();
            documentPhysicalRepository.delete(physID);
        } catch (NoSuchElementException | NullPointerException e) {
            throw new Exception("Document with this virtual document id = " + docId + " does not exist");
        }
    }

    public long getValidPhysicalId(long docId, String docType) throws Exception {
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docId)
                    .filter(doc -> doc.getDocType().equals(docType))
                    .filter(DocumentPhysical::isCanBooked).findFirst().get().getId();
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(docType.toLowerCase() + " with this id = " + docId + " does not exist");
        }
    }

    public void inverseCanBooked(long docId) {
        try {
            DocumentPhysical docPhys = documentPhysicalRepository.findOne(docId);
            boolean canBooked = !docPhys.isCanBooked();
            docPhys.setCanBooked(canBooked);
            documentPhysicalRepository.saveAndFlush(docPhys);
        } catch (NullPointerException | NoSuchElementException ignored) {
        }
    }

    public String getShelfById(long docPhysId) throws Exception {
        try {
            return documentPhysicalRepository.findOne(docPhysId).getShelf();
        } catch (NoSuchElementException e) {
            throw new Exception("Document does not exist");
        }
    }

    public long getIdByDocument(long documentId, String docType) throws Exception {
        if (!Document.isType(docType)) throw new Exception("This document does not exist");
        try {
            return documentPhysicalRepository.findAll().stream()
                    .filter(documentPhysical -> documentPhysical.getDocType().equals(docType))
                    .filter(Document -> Document.getId() == documentId)
                    .findFirst().get().getIdDoc();
        } catch (NoSuchElementException e) {
            throw new Exception("This document does not exist");
        }
    }


    public void removeAllByDocId(long docId) throws Exception {
        try {
            List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docId).collect(Collectors.toList());
            for (DocumentPhysical doc : documentPhysicals) {
                remove(doc.getId());
            }
        } catch (Exception e) {
            throw new Exception("Document does not exist");
        }

    }

    public long getDocIdByID(long id) {
        return documentPhysicalRepository.findOne(id).getIdDoc();
    }

    public int getCount(long docId, String docType) throws Exception {
        if (!Document.isType(docType)) throw new Exception("Invalid type");
        try {
            return (int) documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docId && doc.getDocType().equals(docType)).count();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }

    public int getCount(DocumentPhysical docPhys) throws Exception {
        if (!Document.isType(docPhys.getDocType())) throw new Exception("Invalid type");
        try {
            return (int) documentPhysicalRepository.findAll().stream()
                    .filter(doc -> doc.getIdDoc() == docPhys.getIdDoc())
                    .filter(doc -> doc.getDocType().equals(docPhys.getDocType()))
                    .count();
        } catch (NoSuchElementException e) {
            return 0;
        }
    }
}
