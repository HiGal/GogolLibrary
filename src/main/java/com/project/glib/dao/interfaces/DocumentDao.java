package com.project.glib.dao.interfaces;

import com.project.glib.model.Book;
import com.project.glib.model.Document;

import java.util.List;

public interface DocumentDao<T extends Document> extends ModifyByLibrarian<T> {
    int getCountById(long documentId) throws Exception;

    void decrementCountById(long documentId) throws Exception;

    void incrementCountById(long docId) throws Exception;

    int getPriceById(long documentId) throws Exception;

    void checkValidParameters(T t) throws Exception;

    T isAlreadyExist(T t);

    List<T> getListCountNotZeroOrRenewed();
}
