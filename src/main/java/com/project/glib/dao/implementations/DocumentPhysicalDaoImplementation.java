package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentPhysicalRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.DocumentPhysical;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentPhysicalDaoImplementation implements ModifyByLibrarian<DocumentPhysical> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(DocumentPhysicalDaoImplementation.class);
    private static final String TYPE = DocumentPhysical.TYPE;
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
    public long getId(DocumentPhysical docPhys) {
        return documentPhysicalRepository.findAll().stream()
                .filter(doc -> doc.getDocVirId() == docPhys.getDocVirId() &&
                        doc.isCanBooked() == docPhys.isCanBooked() &&
                        doc.getDocType().equals(docPhys.getDocType()) &&
                        doc.getShelf().equals(docPhys.getShelf()))
                .findFirst().get().getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<DocumentPhysical> getList() {
        List<DocumentPhysical> documentPhysicals = documentPhysicalRepository.findAll();

        for (DocumentPhysical documentPhysical : documentPhysicals) {
            logger.info(LIST + documentPhysical);
        }
        return documentPhysicals;
    }
}
