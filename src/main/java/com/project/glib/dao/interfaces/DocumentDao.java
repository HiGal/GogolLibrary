package com.project.glib.dao.interfaces;

import com.project.glib.model.Document;

public interface DocumentDao<T extends Document> extends ModifyByLibrarian<T> {
    int getCountById(long documentId);

    void decrementCountById(long documentId);

    int getPriceById(long documentId);
}
