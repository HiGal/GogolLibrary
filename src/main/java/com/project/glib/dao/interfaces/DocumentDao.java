package com.project.glib.dao.interfaces;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Document;

import java.util.List;

public interface DocumentDao<T extends Document> extends ModifyByLibrarian<T> {
    int getCountById(long documentId);

    void decrementCountById(long documentId);

    int getPriceById(long documentId);

    List<T> getListCountNotZero();
}
