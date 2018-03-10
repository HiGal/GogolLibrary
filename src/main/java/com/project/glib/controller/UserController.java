package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.User;
import com.project.glib.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//@Controller
@RestController
public class UserController {
    //    private final SecurityDaoImplementation securityDao;
    private final UsersDaoImplementation usersDao;
    private final UserValidator userValidator;
    private final BookDaoImplementation bookDao;
    private final AudioVideoDaoImplementation avDao;

    @Autowired
    public UserController(UsersDaoImplementation usersDao,
//                          SecurityDaoImplementation securityDao,
                          UserValidator userValidator, BookRepository bookRepository, BookDaoImplementation bookDao, AudioVideoDaoImplementation avDao) {
        this.usersDao = usersDao;
//        this.securityDao = securityDao;
        this.userValidator = userValidator;
        this.bookDao = bookDao;
        this.avDao = avDao;
    }

    @RequestMapping(value = "/faculty", method = RequestMethod.GET)
    public ModelAndView facultyDashboard(Model model, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("faculty");
        return modelAndView;
    }

    @RequestMapping(value = "/faculty", method = RequestMethod.POST)
    public ModelAndView facultyDashboard(User user, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", " ");
        modelAndView.setViewName("faculty");
        return modelAndView;
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public ModelAndView studentDashboard(Model model, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("student");
        return modelAndView;
    }

    @RequestMapping(value = "/student", method = RequestMethod.POST)
    public ModelAndView studentDashboard(User user, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", " ");
        modelAndView.setViewName("student");
        return modelAndView;
    }

    @RequestMapping(value = "accessiblebooks", method = RequestMethod.GET)
    public List<Book> getAllForCheckoutBook() {
        return bookDao.getListCountNotZeroOrRenewed();
    }

    @RequestMapping(value = "accessibleAV", method = RequestMethod.GET)
    public List<AudioVideo> getAllForCheckoutAV() {
        return avDao.getListCountNotZeroOrRenewed();
    }

}
