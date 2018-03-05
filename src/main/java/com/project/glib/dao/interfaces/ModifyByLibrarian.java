package com.project.glib.dao.interfaces;

import java.util.List;

public interface ModifyByLibrarian<T> {
    void add(T t) throws Exception;

    void update(T t) throws Exception;

    void remove(long id) throws Exception;

    T getById(long id) throws Exception;

    List<T> getList();
}
