package com.project.glib.dao.interfaces;

import com.project.glib.model.Document;

public interface DocumentDao<T extends Document> extends ModifyByLibrarian<T> {
    void incrementCountById(long docId);

    void decrementCountById(long docId);

    int getCountById(long docId) throws Exception;

    int getPriceById(long docId) throws Exception;

    T isAlreadyExist(T t);

//    // TODO add renewed document to the list
//    List<T> getListCountNotZeroOrRenewed();
}
