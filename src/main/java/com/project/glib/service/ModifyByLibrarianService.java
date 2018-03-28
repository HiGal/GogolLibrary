package com.project.glib.service;

public interface ModifyByLibrarianService<T> {
    String ADD_EXCEPTION = " can't add ";
    String UPDATE_EXCEPTION = " can't update ";
    String REMOVE_EXCEPTION = " can't remove ";
    String SMTH_WRONG = " something wrong ";
    String SHELF_EXCEPTION = " shelf must exist ";
    String TYPE_EXCEPTION = " invalid type ";
    String ID_EXCEPTION = " invalid id ";

    void checkValidParameters(T t) throws Exception;
}
