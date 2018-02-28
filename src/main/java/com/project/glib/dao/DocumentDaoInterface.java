package com.project.glib.dao;

import com.project.glib.model.Document;

import java.util.List;

public interface DocumentDaoInterface<T extends Document> {
    public void add(T document);

    public void update(T document);

    public void remove(long documentId);

    public T getById(long documentId);

    public List<T> getList();
}
