package com.project.glib.service;

import com.project.glib.dao.implementations.KeywordDaoImplementation;
import com.project.glib.model.Keyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class KeywordService implements ModifyByLibrarianService<Keyword> {
    public static final String TYPE = Keyword.TYPE;
    public static final String ADD_EXCEPTION = ModifyByLibrarianService.ADD_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String UPDATE_EXCEPTION = ModifyByLibrarianService.UPDATE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String REMOVE_EXCEPTION = ModifyByLibrarianService.REMOVE_EXCEPTION + TYPE + SMTH_WRONG;
    public static final String EXIST_EXCEPTION = INFORMATION_NOT_AVAILABLE + TYPE + DOES_NOT_EXIST;

    private final KeywordDaoImplementation keywordDao;

    @Autowired
    public KeywordService(KeywordDaoImplementation keywordDao) {
        this.keywordDao = keywordDao;
    }

    public void add(Keyword keyword) throws Exception {
        checkValidParameters(keyword);
        try {
            keywordDao.add(keyword);
        } catch (Exception e) {
            throw new Exception(ADD_EXCEPTION);
        }
    }

    @Override
    public void checkValidParameters(Keyword keyword) throws Exception {
        if (keyword.getId() <= 0) {
            throw new Exception(ID_EXCEPTION);
        }

        if (keyword.getKeyword().equals("")) {
            throw new Exception(KEYWORD_EXCEPTION);
        }
    }

    @Override
    public Keyword getById(long id) {
        return keywordDao.getById(id);
    }

    @Override
    public long getId(Keyword keyword) throws Exception {
        try {
            return keywordDao.getId(keyword);
        } catch (NullPointerException | NoSuchElementException e) {
            throw new Exception(EXIST_EXCEPTION);
        }
    }

    @Override
    public List<Keyword> getList() {
        try {
            return keywordDao.getList();
        } catch (NoSuchElementException | NullPointerException e) {
            return new ArrayList<>();
        }
    }
}
