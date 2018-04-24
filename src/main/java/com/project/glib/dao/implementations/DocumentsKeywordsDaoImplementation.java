package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.DocumentsKeywordsRepository;
import com.project.glib.model.DocumentsKeywords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class DocumentsKeywordsDaoImplementation {
    private static final Logger log = (Logger) LoggerFactory.getLogger(DocumentsKeywordsDaoImplementation.class);
    private final DocumentsKeywordsRepository documentsKeywordsRepository;

    @Autowired
    public DocumentsKeywordsDaoImplementation(DocumentsKeywordsRepository documentsKeywordsRepository) {
        this.documentsKeywordsRepository = documentsKeywordsRepository;
    }

    public void add(DocumentsKeywords documentsKeywords) throws Exception {
        try {
            documentsKeywordsRepository.save(documentsKeywords);
            log.info("Connection successfully saved. Connection details : " + documentsKeywords);
        } catch (Exception e) {
            log.info("Try to add connection with wrong parameters. New connection information : " + documentsKeywords);
            throw new Exception("Can't add this connection, some parameters are wrong");
        }
    }

    public void update(DocumentsKeywords documentsKeywords) throws Exception {
        try {
            documentsKeywordsRepository.saveAndFlush(documentsKeywords);
            log.info("Connection successfully update. Connection details : " + documentsKeywords);
        } catch (Exception e) {
            log.info("Try to update this connection, connection don't exist or some new connection parameters are wrong. " +
                    "Update connection information : " + documentsKeywords);
            throw new Exception("Can't update this connection, connection don't exist or some new connection parameters are wrong");
        }
    }

    public void remove(long id) throws Exception {
        try {
            log.info("Try to delete connection with connection id = " + id);
            documentsKeywordsRepository.delete(id);
        } catch (Exception e) {
            log.info("Try to delete connection with wrong log id = " + id);
            throw new Exception("Delete this connection not available, connection don't exist");
        }
    }

    public Object getById(long id) {
        return documentsKeywordsRepository.findOne(id);
    }


    public long getId(DocumentsKeywords documentsKeywords) {
        return documentsKeywords.getId();
    }


    public List<DocumentsKeywords> getList() {
        return documentsKeywordsRepository.findAll();
    }
}
