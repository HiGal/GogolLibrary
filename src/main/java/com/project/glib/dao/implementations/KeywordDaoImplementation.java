package com.project.glib.dao.implementations;

import com.project.glib.dao.interfaces.KeywordRepository;
import com.project.glib.dao.interfaces.ModifyByLibrarian;
import com.project.glib.model.Keyword;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class KeywordDaoImplementation implements ModifyByLibrarian<Keyword> {
    private static final Logger logger = (Logger) LoggerFactory.getLogger(BookDaoImplementation.class);
    private static final String TYPE = Keyword.TYPE;
    private static final String ADD_KEYWORD = TYPE + ADD;
    private static final String UPDATE_KEYWORD = TYPE + UPDATE;
    private static final String REMOVE_KEYWORD = TYPE + REMOVE;
    private static final String LIST = TYPE + ModifyByLibrarian.LIST;
    private final KeywordRepository keywordRepository;

    @Autowired
    public KeywordDaoImplementation(KeywordRepository keywordRepository) {
        this.keywordRepository = keywordRepository;
    }


    @Override
    public void add(Keyword keyword) {
        keywordRepository.saveAndFlush(keyword);
        logger.info(ADD_KEYWORD + keyword);
    }

    @Override
    public void update(Keyword keyword) {
        keywordRepository.saveAndFlush(keyword);
        logger.info(UPDATE_KEYWORD + keyword);
    }

    @Override
    public void remove(long id) {
        keywordRepository.delete(id);
        logger.info(REMOVE_KEYWORD + id);
    }

    @Override
    public Keyword getById(long id) {
        return keywordRepository.findOne(id);
    }

    @Override
    public long getId(Keyword keyword) {
        return keywordRepository.findAll().stream()
                .filter(k -> k.getKeyword().equals(keyword.getKeyword()))
                .findFirst().get().getId();
    }

    @Override
    public List<Keyword> getList() {
        List<Keyword> list = keywordRepository.findAll();

        for (Keyword keyword : list) {
            logger.info(LIST + keyword);
        }

        return list;
    }
}
