package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.model.Checkout;
import com.project.glib.service.BookingService;
import com.project.glib.service.CheckOutService;
import com.project.glib.service.ReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

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

    //return list of books
    @ResponseBody
    @RequestMapping(value = "/return", method = RequestMethod.GET)
    public List<Checkout> checkoutBook(@RequestParam String login) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("allBookings", checkOutService.getCheckoutsByUser(usersDao.getIdByLogin(login)));
            modelAndView.setViewName("order_book");
            return checkOutService.getCheckoutsByUser(usersDao.getIdByLogin(login));
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/return", method = RequestMethod.POST)
    public String checkoutBook(@RequestBody Checkout checkout, Model model) {
        try {
            return "overdue: " + returnService.toReturnDocument(checkout).getValue().toString();
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";

    }

    @RequestMapping(value = "/return/all", method = RequestMethod.POST)
    public String returnAllDocument(@RequestParam String login, Model model) {
        try {
            List<Checkout> checkout = checkOutService.getCheckoutsByUser(usersDao.getIdByLogin(login));
            for (int i = 0; i < checkout.size(); i++) {
                returnService.toReturnDocument(checkout.get(i));
            }
            return "successfully checkout";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";

    }


}
