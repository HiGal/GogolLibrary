package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentPhysicalRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.DocumentPhysical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DocumentPhysicalDaoImplementation implements ModifyByLibrarian<DocumentPhysical> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DocumentPhysicalDaoImplementation.class);
    private final DocumentPhysicalRepository documentPhysicalRepository;

    @Autowired
    public DocumentPhysicalDaoImplementation(DocumentPhysicalRepository documentPhysicalRepository) {
        this.documentPhysicalRepository = documentPhysicalRepository;
    }

    public void add(DocumentPhysical documentPhysical) {
        documentPhysicalRepository.save(documentPhysical);
        logger.info("Document successfully saved. Document details : " + documentPhysical);
    }

    public void update(DocumentPhysical documentPhysical) {
        documentPhysicalRepository.saveAndFlush(documentPhysical);
        logger.info("Document successfully update. Document details : " + documentPhysical);
    }

    public void remove(long id_doc) {
        long physID= (documentPhysicalRepository.getFirstByIdDoc(id_doc)).getId();
        documentPhysicalRepository.delete(physID);
    }


    public DocumentPhysical getById(long documentPhysicalId) {
        return documentPhysicalRepository.findOne(documentPhysicalId);
    }

    @SuppressWarnings("unchecked")
    public List<DocumentPhysical> getList() {
        List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll();

        for (DocumentPhysical documentPhysical : documentPhysicals) {
            logger.info("Document list : " + documentPhysical);
        }

        return documentPhysicals;
    }

    public long getValidPhysicalId(long documentId, String documentType) {
        return documentPhysicalRepository.findAll().stream()
                .filter(doc -> doc.getIdDoc() == documentId)
                .filter(doc -> doc.getDocType().equals(documentType))
                .filter(DocumentPhysical::isCanBooked)
                .filter(doc -> !doc.isReference()).findFirst().get().getId();
    }

    public void inverseCanBooked(long documentId) {
        documentPhysicalRepository.findOne(documentId).setCanBooked(
                !documentPhysicalRepository.findOne(documentId).isCanBooked());
    }

    public String getShelfById(long documentPhysicalId) {
        return documentPhysicalRepository.findOne(documentPhysicalId).getShelf();
    }

    public long getIdByDocument(long documentId, String documentType) {
        long i =  documentPhysicalRepository.findAll().stream()
                .filter(documentPhysical -> documentPhysical.getDocType().equals(documentType))
                .filter(documentPhysical -> documentPhysical.getId() == documentId)
                .findFirst().get().getIdDoc();
        System.out.println(i + " iiiiiii");
        return i;
    }

    public void removeAllByDocId(long docId) {
        List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll().stream()
                .filter(doc -> doc.getIdDoc() == docId).collect(Collectors.toList());

        for (DocumentPhysical doc : documentPhysicals) {
            remove(doc.getId());
        }
    }
}
