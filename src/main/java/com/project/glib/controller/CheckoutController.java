//package com.project.glib.controller;
//
//import com.project.glib.model.Booking;
//import com.project.glib.service.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.util.List;
//
//@RestController
//public class CheckoutController {
//    private final BookingService bookingService;
//    private final BookService bookService;
//    private final JournalService journalService;
//    private final AudioVideoService avService;
//    private final UserService userService;
//    private final CheckoutService checkoutService;
//
//    @Autowired
//    public CheckoutController(BookingService bookingService,
//                              BookService bookService,
//                              JournalService journalService,
//                              AudioVideoService avService,
//                              UserService userService,
//                              CheckoutService checkoutService) {
//        this.bookingService = bookingService;
//        this.bookService = bookService;
//        this.journalService = journalService;
//        this.avService = avService;
//        this.userService = userService;
//        this.checkoutService = checkoutService;
//    }
//
//    //return list of books
//    @ResponseBody
//    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
//    public List<Booking> checkoutDocument(@RequestParam String login) {
//        try {
//            ModelAndView modelAndView = new ModelAndView();
//            modelAndView.addObject("allBookings", bookingService.getBookingsByUser(userService.getIdByLogin(login)));
//            modelAndView.setViewName("order_book");
//            return bookingService.getBookingsByUser(userService.getIdByLogin(login));
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
//    public String checkoutDocument(@RequestBody Booking booking, Model model) {
//        try {
//            checkoutService.toCheckoutDocument(booking);
//            return "successfully checkout";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return e.getMessage();
//        }
//        //      return "order";
//
//    }
//
//    @RequestMapping(value = "/checkout/all", method = RequestMethod.POST)
//    public String checkoutAllDocument(@RequestParam String login, Model model) {
//        try {
//            List<Booking> bookingList = bookingService.getBookingsByUser(userService.getIdByLogin(login));
//            for (Booking booking : bookingList) {
//                checkoutService.toCheckoutDocument(booking);
//            }
//            return "successfully checkout";
//        } catch (Exception e) {
//            model.addAttribute("error", e.getMessage());
//            return e.getMessage();
//        }
//        //      return "order";
//
//    }
//
//
//}
