package com.project.glib.dao.interfaces;

import java.util.List;

public interface ModifyByLibrarian<T> {
    String ADD = " successfully saved. Details : ";
    String UPDATE = " successfully update. Details : ";
    String REMOVE = " successfully delete. Details : ";
    String INFORMATION_NOT_AVAILABLE = " information not available. ";
    String DOES_NOT_EXIST = " don't exist ";
    String LIST = " list : ";
    String TYPE_EXCEPTION = " invalid type ";

    void add(T t);

    void update(T t);

    void remove(long id);

    T getById(long id);

    long getId(T t);

    List<T> getList();
}
