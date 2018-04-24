package com.project.glib.service;

import com.project.glib.dao.implementations.DocumentKeywordRelationDaoImplementation;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentKeywordRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentKeywordRelationService implements ModifyByLibrarianService<DocumentKeywordRelation> {
    public static final String TYPE = DocumentKeywordRelation.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;

    private final DocumentKeywordRelationDaoImplementation relationDao;

    @Autowired
    public DocumentKeywordRelationService(DocumentKeywordRelationDaoImplementation relationDao) {
        this.relationDao = relationDao;
    }

    public void add(DocumentKeywordRelation documentKeywordRelation) throws Exception {
        try {
            relationDao.add(documentKeywordRelation);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    @Override
    public void checkValidParameters(DocumentKeywordRelation documentKeywordRelation) throws Exception {
        if (documentKeywordRelation.getDocVirId() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }

        if (!Document.isType(documentKeywordRelation.getDocType())) {
            throw new Exception(TYPE_EXCEPTION);
        }

        if (documentKeywordRelation.getKeywordId() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }
    }

    @Override
    public DocumentKeywordRelation getById(long id) {
        return relationDao.getById(id);
    }

    @Override
    @Deprecated
    public long getId(DocumentKeywordRelation documentKeywordRelation) {
        return relationDao.getId(documentKeywordRelation);
    }

    @Override
    public List<DocumentKeywordRelation> getList() {
        try {
            return relationDao.getList();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
