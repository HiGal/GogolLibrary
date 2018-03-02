package com.project.glib.dao.interfaces;

import com.project.glib.model.Document;

import java.util.List;

public interface DocumentDaoInterface<T extends Document> {
    void add(T document);

    void update(T document);

    void remove(long documentId);

    T getById(long documentId);

    List<T> getList();
}
