package com.project.glib.controller;

import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.util.List;

//@Controller
@RestController
public class LibrarianController{
    private final UsersDaoImplementation usersDao;

    public LibrarianController(UsersDaoImplementation usersDao) {
        this.usersDao = usersDao;
    }

//    @RequestMapping(value = "/librarian", method = RequestMethod.GET)
//    public ModelAndView librarianDashboard(Model model, String login) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("user", usersDao.findLogin(login));
//        modelAndView.setViewName("librarian");
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/librarian", method = RequestMethod.POST)
//    public ModelAndView librarianDashboard(User user) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("error", " ");
//        modelAndView.setViewName("librarian");
//        return modelAndView;
//    }

    @RequestMapping(value = "/librarian/confirm", method = RequestMethod.GET)
 //   public ModelAndView librarianConfirm(Model model, String login) {
    public List<User> librarianConfirm(Model model, String login) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allUsers", usersDao.getList());
//        modelAndView.setViewName("confirm");
        return usersDao.getListAuthUsers();
    }

    @RequestMapping(value = "/librarian/confirm", method = RequestMethod.POST)
//    public ModelAndView librarianConfirm(User user, String login) {
    public String librarianConfirm(@RequestBody User user) throws Exception {
        user.setAuth(true);
        try {
            usersDao.update(user);
            return "- successfully updated -";
        }catch (Exception e){
            return "- failed! -";
        }
    }

}
