package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.model.Booking;
import com.project.glib.model.Checkout;
import com.project.glib.service.BookingService;
import com.project.glib.service.CheckOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
public class CheckoutController {
    private final BookingService bookingService;
    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation audioVideoDao;
    private final BookingController bookingController;
    private final UsersDaoImplementation usersDao;
private final CheckOutService checkOutService;

    @Autowired
    public CheckoutController(BookingService bookingService, BookDaoImplementation bookDao, JournalDaoImplementation journalDao, AudioVideoDaoImplementation audioVideoDao, BookingController bookingController, UsersDaoImplementation usersDao, CheckOutService checkOutService) {
        this.bookingService = bookingService;
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.audioVideoDao = audioVideoDao;
        this.bookingController = bookingController;
        this.usersDao = usersDao;
        this.checkOutService = checkOutService;
    }

    //return list of books
    @ResponseBody
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public List<Booking> checkoutBook(@RequestParam String login) {
        try {
            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("allBookings", bookingService.getBookingsByUser(usersDao.getIdByLogin(login)));
            modelAndView.setViewName("order");
            return bookingService.getBookingsByUser(usersDao.getIdByLogin(login));
        } catch (Exception e) {
            return null;
        }
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public String checkoutBook(@RequestBody Booking booking, Model model) {
        try {
            checkOutService.toCheckoutDocument(booking);
            return "successfully checkout";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";

    }


}
