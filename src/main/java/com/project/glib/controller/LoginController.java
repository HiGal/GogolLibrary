package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Arrays;

@Controller
@SessionAttributes("user")
public class LoginController {
    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public LoginController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

//    @ModelAttribute("user")
//    public User setUpUser(){
//        return new User();
//    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login( User form, HttpServletRequest request, SessionStatus status) {
        ModelAndView model = new ModelAndView();
        try {
            User user = userService.findByLogin(form.getLogin());

            String role = user.getRole();

            if (user.getPassword().equals(form.getPassword())) {
                model.addObject("info", user);
                request.getSession().setAttribute("user",user);
                if (role.equals(User.LIBRARIAN)) {
                    model.setViewName("librarian");
                } else if (Arrays.asList(User.PATRONS).contains(role)) {
                    model.setViewName("patron");
                } else {
                    throw new Exception("WRONG ROLE");
                }

                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ModelAndView books() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allBooks", bookService.getList());
        modelAndView.setViewName("documents");
        return modelAndView;
    }


}