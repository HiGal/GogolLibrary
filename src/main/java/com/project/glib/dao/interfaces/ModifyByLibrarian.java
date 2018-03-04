package com.project.glib.dao.interfaces;

import java.util.List;

public interface ModifyByLibrarian<T> {
    void add(T t);

    void update(T t);

    void remove(long id);

    T getById(long id);

    List<T> getList();
}
