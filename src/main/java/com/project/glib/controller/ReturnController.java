package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.service.BookingService;
import com.project.glib.service.CheckOutService;
import com.project.glib.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReturnController {
    private final BookingService bookingService;
    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation audioVideoDao;
    private final BookingController bookingController;
    private final UsersDaoImplementation usersDao;
    private final CheckOutService checkOutService;
    private final ReturnService returnService;

    @Autowired
    public ReturnController(BookingService bookingService, BookDaoImplementation bookDao, JournalDaoImplementation journalDao, AudioVideoDaoImplementation audioVideoDao, BookingController bookingController, UsersDaoImplementation usersDao, CheckOutService checkOutService, ReturnService returnService) {
        this.bookingService = bookingService;
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.audioVideoDao = audioVideoDao;
        this.bookingController = bookingController;
        this.usersDao = usersDao;
        this.checkOutService = checkOutService;
        this.returnService = returnService;
    }


}
