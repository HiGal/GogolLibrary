package com.project.glib.service;

public interface LoggerActions {
    String CHECKEDOUT_BOOK = "CHECKEDOUT_BOOK ";
    String RETURNED_BOOK = "RETURNED_BOOK";
    String BOOKED_BOOK = "BOOKED_BOOK";

    String CHECKEDOUT_JOURNAL = "CHECKEDOUT_JOURNAL";
    String RETURNED_JOURNAL = "RETURNED_JOURNAL";
    String BOOKED_JOURNAL = "BOOKED_JOURNAL";

    String CHECKEDOUT_AV = "CHECKEDOUT_AV";
    String RETURNED_AV = "RETURNED_AV";
    String BOOKED_AV = "BOOKED_AV";

    String ADDED_BOOK = "ADDED_BOOK";
    String MODIFIED_BOOK = "MODIFIED_BOOK";
    String DELETED_BOOK = "DELETED_BOOK";
    String DELETED_ALL_BOOKS = "DELETED_ALL_BOOKS";

    String ADDED_JOURNAL = "ADDED_JOURNAL";
    String MODIFIED_JOURNAL = "MODIFIED_JOURNAL";
    String DELETED_JOURNAL = "DELETED_JOURNAL";
    String DELETED_ALL_JOURNALS = "DELETED_ALL_JOURNALS";

    String ADDED_AV = "ADDED_AV ";
    String MODIFIED_AV = "MODIFIED_AV";
    String DELETED_AV = "DELETED_AV";
    String DELETED_ALL_AV = "DELETED_ALL_AV";

    String ADDED_NEW_USER = "ADDED_NEW_USER";
    String MODIFIED_USER = "MODIFIED_USER or ADDED_NEW_USER";
    String DELETED_USER = "DELETED_USER";

    String ADDED_NEW_LIBRARIAN = "ADDED_AS_LIBRARIAN BY ADMIN";
    String MODIFIED_LIBRARAN = "MODIFIED_LIBRARAN";
    String DELETED_LIBRARIAN = "DELETED_LIBRARIAN";

    String READ_ALL_MESSAGES = "READ_ALL_MESSAGES";
    String OUTSTANDING_REQUEST = "OUTSTANDING_REQUEST ";

}
