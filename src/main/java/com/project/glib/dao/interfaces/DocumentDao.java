package com.project.glib.dao.interfaces;

import com.project.glib.model.Document;

import java.util.List;

public interface DocumentDao<T extends Document> extends ModifyByLibrarian<T> {
    void add(T t, String shelf) throws Exception;

    int getCountById(long documentId) throws Exception;

    void decrementCountById(long documentId) throws Exception;

    void incrementCountById(long docId) throws Exception;

    int getPriceById(long documentId) throws Exception;

    T isAlreadyExist(T t);

    // TODO add renewed document to the list
    List<T> getListCountNotZeroOrRenewed();

    boolean isNote(String note);
}
