package com.project.glib.service;

import java.util.List;

public interface ModifyByLibrarianService<T> {
    String ADD_EXCEPTION = " can't add ";
    String UPDATE_EXCEPTION = " can't update ";
    String REMOVE_EXCEPTION = " can't remove ";
    String SMTH_WRONG = " something wrong ";
    String SHELF_EXCEPTION = " shelf must exist ";
    String TYPE_EXCEPTION = " invalid type ";
    String ID_EXCEPTION = " invalid id ";
    String KEYWORD_EXCEPTION = "Empty keyword forbidden";
    String INFORMATION_NOT_AVAILABLE = " information not available. ";
    String DOES_NOT_EXIST = " don't exist ";

    void checkValidParameters(T t) throws Exception;

    T getById(long id);

    long getId(T t) throws Exception;

    List<T> getList();
}
