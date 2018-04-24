package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentKeywordRelationRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.DocumentKeywordRelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DocumentKeywordRelationDaoImplementation implements ModifyByLibrarian<DocumentKeywordRelation> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = DocumentKeywordRelation.TYPE;
    private static final String ADD = TYPE + ModifyByLibrarian.ADD;
    private static final String UPDATE = TYPE + ModifyByLibrarian.UPDATE;
    private static final String REMOVE = TYPE + ModifyByLibrarian.REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final DocumentKeywordRelationRepository documentKeywordRelationRepository;

    public DocumentKeywordRelationDaoImplementation(DocumentKeywordRelationRepository documentKeywordRelationRepository) {
        this.documentKeywordRelationRepository = documentKeywordRelationRepository;
    }

    @Override
    public void add(DocumentKeywordRelation documentKeywordRelation) {
        documentKeywordRelationRepository.saveAndFlush(documentKeywordRelation);
        logger.info(ADD + documentKeywordRelation);
    }

    @Override
    public void update(DocumentKeywordRelation documentKeywordRelation) {
        documentKeywordRelationRepository.saveAndFlush(documentKeywordRelation);
        logger.info(UPDATE + documentKeywordRelation);
    }

    @Override
    public void remove(long id) {
        documentKeywordRelationRepository.delete(id);
        logger.info(REMOVE + id);
    }

    @Override
    public DocumentKeywordRelation getById(long id) {
        return documentKeywordRelationRepository.findOne(id);
    }

    @Override
    @Deprecated
    public long getId(DocumentKeywordRelation documentKeywordRelation) {
        return 0;
    }

    @Override
    public List<DocumentKeywordRelation> getList() {
        List<DocumentKeywordRelation> list = documentKeywordRelationRepository.findAll();

        for (DocumentKeywordRelation relation : list) {
            logger.info(LIST + relation);
        }

        return list;
    }
}
