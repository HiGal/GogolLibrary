package com.project.glib.dao.interfaces;

import com.project.glib.model.Document;

public interface DocumentDao<T extends Document> extends ModifyByLibrarian<T> {
    T isAlreadyExist(T t);
}
