package com.project.glib.controller;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.Booking;
import com.project.glib.model.Checkout;
import com.project.glib.model.User;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//@Controller
@RestController
public class LibrarianController {
    private final UsersDaoImplementation usersDao;
    private final BookDaoImplementation bookDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;
    private final BookingDaoImplementation bookingDao;
    private final CheckoutDaoImplementation checkoutDao;
    private final AudioVideoDaoImplementation avDao;
    private final JournalDaoImplementation journalDao;

    @Autowired
    public LibrarianController(UsersDaoImplementation usersDao, BookDaoImplementation bookDao,
                               DocumentPhysicalDaoImplementation docPhysDao,
                               BookingDaoImplementation bookingDao, CheckoutDaoImplementation checkoutDao,
                               AudioVideoDaoImplementation avDao, JournalDaoImplementation journalDao) {
        this.usersDao = usersDao;
        this.bookDao = bookDao;
        this.docPhysDao = docPhysDao;
        this.bookingDao = bookingDao;
        this.checkoutDao = checkoutDao;
        this.avDao = avDao;
        this.journalDao = journalDao;
    }


    @RequestMapping(value = "/librarian", method = RequestMethod.GET)
    public ModelAndView librarianDashboard(Model model, String login, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", usersDao.findByLogin(login));
        modelAndView.setViewName("librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/librarian", method = RequestMethod.POST)
    public ModelAndView librarianDashboard(User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", " ");
        modelAndView.setViewName("librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/librarian/user/confirm", method = RequestMethod.GET)
    public ModelAndView librarianConfirm(Model model, String login) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", usersDao.getListNotAuthUsers());
        modelAndView.setViewName("confirm_user");
        return modelAndView;
    }

    @RequestMapping(value = "/librarian/user/confirm", method = RequestMethod.POST)
    public ModelAndView librarianConfirm(User user, String login) {
        User realUser = usersDao.findByLogin(user.getLogin());
        realUser.setAuth(true);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("student");
        try {
            usersDao.update(realUser);
            modelAndView.addObject("error", "");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
        }
        return modelAndView;
    }

    @RequestMapping(value = "/librarian/user/delete", method = RequestMethod.GET)
    //   public ModelAndView librarianConfirm(Model model, String login) {
    public List<User> librarianDeleteUser(Model model, String login) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allUsers", usersDao.getList());
//        modelAndView.setViewName("confirm");
        return usersDao.getListAuthUsers();
    }

    @RequestMapping(value = "/librarian/user/delete", method = RequestMethod.POST)
//    public ModelAndView librarianConfirm(User user, String login) {
    public String librarianDeleteUser(@RequestParam String login) {
        try {
            User user = usersDao.findByLogin(login);
            if (user != null) {
                bookingDao.remove(usersDao.getIdByLogin(login));
                usersDao.remove(usersDao.getIdByLogin(login));
                return "- successfully deleted -";
            } else {
                throw new Exception("User is not exist");
            }
        } catch (Exception e) {
            return "- failed! -";
        }
    }

    @RequestMapping(value = "/librarian/user/modify", method = RequestMethod.GET)
    //   public ModelAndView librarianConfirm(Model model, String login) {
    public List<User> librarianModifyUser(Model model, String login) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allUsers", usersDao.getList());
//        modelAndView.setViewName("confirm");
        return usersDao.getListAuthUsers();
    }

    @RequestMapping(value = "/librarian/user/modify", method = RequestMethod.POST)
//    public ModelAndView librarianConfirm(User user, String login) {
    public String librarianModifyUser(@RequestBody User user) {
        try {
            User RealUser = usersDao.findByLogin(user.getLogin());
            if (RealUser != null) {
                usersDao.update(user);
                return "- successfully modified -";
            } else {
                throw new Exception("User does not exist");
            }
        } catch (Exception e) {
            return "- failed! -";
        }
    }


    @RequestMapping(value = "/librarian/user/info/checkout", method = RequestMethod.GET)
//    public ModelAndView librarianConfirm(User user, String login) {
    public Pair librarianGetCheckout(@RequestParam(value = "login") String login) throws Exception {
        User user = usersDao.findByLogin(login);
        if (user != null) {
            List<Checkout> checkout = checkoutDao.getCheckoutsByUser(user.getId());
            return new Pair<>(user, checkout);
        } else {
            throw new Exception("User doesn't exist");
        }
    }

    @RequestMapping(value = "/librarian/user/info/booking", method = RequestMethod.GET)
//    public ModelAndView librarianConfirm(User user, String login) {
    public Pair<User, List<Booking>> librarianGetBooking(@RequestParam String login) throws Exception {
        User user = usersDao.findByLogin(login);
        if (user != null) {
            List<Booking> bookings = bookingDao.getBookingsByUser(user.getId());
            return new Pair<>(user, bookings);
        } else {
            throw new Exception("User does not exist");
        }
    }

//    @RequestMapping(value = "/librarian/user/info/overdue", method = RequestMethod.GET)
////    public ModelAndView librarianConfirm(User user, String login) {
//    public Pair<User, List<Checkout>> librarianGetOverdue(@RequestParam String login) throws Exception {
//        User user = usersDao.findByLogin(login);
//        if (user != null) {
//            Pair pair = new Pair(user, checkout);
//            return pair;
//        } else {
//            throw new Exception("User is not exist");
//        }
//    }


}
