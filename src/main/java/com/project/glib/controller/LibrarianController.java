package com.project.glib.controller;

import com.project.glib.dao.implementations.*;
import com.project.glib.model.*;
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
    private final DocumentPhysicalDaoImplementation physicalDaoImplementation;
    private final BookingDaoImplementation bookingDao;
    private final CheckoutDaoImplementation checkoutDao;
    private final AudioVideoDaoImplementation avDao;

    @Autowired
    public LibrarianController(UsersDaoImplementation usersDao, BookDaoImplementation bookDao,
                               DocumentPhysicalDaoImplementation physicalDaoImplementation,
                               BookingDaoImplementation bookingDao, CheckoutDaoImplementation checkoutDao,
                               AudioVideoDaoImplementation avDao) {
        this.usersDao = usersDao;
        this.bookDao = bookDao;
        this.physicalDaoImplementation = physicalDaoImplementation;
        this.bookingDao = bookingDao;
        this.checkoutDao = checkoutDao;
        this.avDao = avDao;
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
    //   public ModelAndView librarianConfirm(Model model, String login) {
    public List<User> librarianConfirm(Model model, String login) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allUsers", usersDao.getList());
//        modelAndView.setViewName("confirm");
        return usersDao.getListNotAuthUsers();
    }

    @RequestMapping(value = "/librarian/user/confirm", method = RequestMethod.POST)
//    public ModelAndView librarianConfirm(User user, String login) {
    public String librarianConfirm(@RequestBody User user) throws Exception {
        User RealUser = usersDao.findByLogin(user.getLogin());
        RealUser.setAuth(true);
        try {
            usersDao.update(RealUser);
            return "- successfully updated -";
        } catch (Exception e) {
            return "- failed! -";
        }
    }

    @RequestMapping(value = "librarian/add/book", method = RequestMethod.POST)
    public String addBook(@RequestBody Book book, @RequestParam(value = "shelf") String shelf,
                          @RequestParam(value = "isReference") boolean flag) {
        if (!bookDao.isAlreadyExist(book)) {
            try {
                bookDao.add(book);
                for (int i = 0; i < book.getCount(); i++) {
                    DocumentPhysical document = new DocumentPhysical();
                    document.setShelf(shelf);
                    document.setIdDoc(book.getId());
                    document.setShelf(shelf);
                    document.setDocType("BOOK");
                    document.setCanBooked(true);
                    document.setReference(flag);
                    physicalDaoImplementation.add(document);
                }
                return "Book successfully added";
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            book.setCount(book.getCount() + 1);
            try {
                bookDao.update(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DocumentPhysical document = new DocumentPhysical();
            document.setIdDoc(book.getId());
            document.setShelf(shelf);
            document.setDocType("BOOK");
            document.setCanBooked(flag);
            document.setReference(flag);
            physicalDaoImplementation.add(document);
            return "Add a copy";
        }
        return "";
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
    public String librarianDeleteUser(@RequestBody User user) {

        try {
            User user1 = usersDao.findByLogin(user.getLogin());
            if (user1 != null) {
                usersDao.remove(usersDao.getIdByLogin(user1.getLogin()));
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
    public String librarianModifyUser(@RequestParam String login) throws Exception {
        try {
            User user = usersDao.findByLogin(login);
            if (user != null) {
                usersDao.update(user);
                return "- successfully modified -";
            } else {
                throw new Exception("User is not exist");
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
            return new Pair(user, checkout);
        } else {
            throw new Exception("User doesn't exist");
        }
    }

    @RequestMapping(value = "/librarian/user/info/booking", method = RequestMethod.GET)
//    public ModelAndView librarianConfirm(User user, String login) {
    public Pair<User, List<Booking>> librarianGetBooking(@RequestParam String login) throws Exception {
        User user = usersDao.findByLogin(login);
        if (user != null) {
            List<Booking> booking = bookingDao.getBookingsByUser(user.getId());
            return new Pair(user, booking);
        } else {
            throw new Exception("User does not exist");
        }
    }


    @RequestMapping(value = "/librarian/add/AV")
    public String addAV(@RequestBody AudioVideo audioVideo,
                        @RequestParam(value = "shelf") String shelf,
                        @RequestParam(value = "isReference") boolean flag) {
        if (!avDao.isAlreadyExist(audioVideo)) {
            try {
                avDao.add(audioVideo);
                for (int i = 0; i < audioVideo.getCount(); i++) {
                    DocumentPhysical document = new DocumentPhysical();
                    document.setIdDoc(audioVideo.getId());
                    document.setShelf(shelf);
                    document.setDocType("AUDIO_VIDEO");
                    document.setCanBooked(true);
                    document.setReference(flag);
                    physicalDaoImplementation.add(document);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            audioVideo.setCount(audioVideo.getCount() + 1);
            try {
                avDao.update(audioVideo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DocumentPhysical document = new DocumentPhysical();
            document.setIdDoc(audioVideo.getId());
            document.setShelf(shelf);
            document.setDocType("AUDIO_VIDEO");
            document.setCanBooked(flag);
            document.setReference(flag);
            physicalDaoImplementation.add(document);
            return "Add a copy";
        }
        return "";
    }

    @RequestMapping(value = "/librarian/remove/book/{num_copies}",method = RequestMethod.POST)
    public String removeBook(@RequestBody Book book, @PathVariable("num_copies") long num){
        try {
            for (int i = 0; i < num; i++) {
                System.out.println(book.getCount());
                bookDao.decrementCountById(book.getId());
                System.out.println(book.getCount());
                physicalDaoImplementation.remove(book.getId());
            }
            return "Book/books is/are successfully removed";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
