package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import com.project.glib.model.User;
import com.project.glib.service.AudioVideoService;
import com.project.glib.service.BookService;
import com.project.glib.service.JournalService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class UserController {
    //    private final SecurityDaoImplementation securityDao;
    private final UserService userService;
//    private final UserValidator userValidator;
private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;

    @Autowired
    public UserController(UserService userService,
//                          SecurityDaoImplementation securityDao,
//                          UserValidator userValidator,
                          BookService bookService,
                          JournalService journalService,
                          AudioVideoService avService) {
        this.userService = userService;
//        this.securityDao = securityDao;
//        this.userValidator = userValidator;
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
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

    @RequestMapping(value = "/visiting_professor", method = RequestMethod.GET)
    public ModelAndView visitingProfessorDashboard(Model model, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("visiting_professor");
        return modelAndView;
    }

    @RequestMapping(value = "/visiting_professor", method = RequestMethod.POST)
    public ModelAndView visitingProfessorDashboard(User user, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", " ");
        modelAndView.setViewName("visiting_professor");
        return modelAndView;
    }

    @RequestMapping(value = "accessibleBooks", method = RequestMethod.GET)
    public List<Book> getAllForCheckoutBook() {
        return bookService.getList();
    }

    @RequestMapping(value = "accessibleJournals", method = RequestMethod.GET)
    public List<Journal> getAllForCheckoutJournal() {
        return journalService.getList();
    }

    @RequestMapping(value = "accessibleAV", method = RequestMethod.GET)
    public List<AudioVideo> getAllForCheckoutAV() {
        return avService.getList();
    }

}
