package com.project.glib.controller;

import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
//@RestController
public class BookingController {

    private final BookingService bookingService;
    private final BookDaoImplementation bookDao;

    @Autowired
    public BookingController(BookingService bookingService, BookDaoImplementation bookDao) {
        this.bookingService = bookingService;
        this.bookDao = bookDao;
    }

    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    public ModelAndView booking() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allBooks", bookDao.getList());
        modelAndView.setViewName("order");
        return modelAndView;
    }

//    @RequestMapping(value = "/registration", method = RequestMethod.POST)
//    public String registration(@RequestBody User userForm, BindingResult bindingResult) {
//
//
//        return ;
//    }
}

