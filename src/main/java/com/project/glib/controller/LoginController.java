package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;

@Controller
public class LoginController {
    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@ModelAttribute User form) {
        ModelAndView model = new ModelAndView();
        try {
            User user = userService.findByLogin(form.getLogin());
            String role = user.getRole();

            if (user.getPassword().equals(form.getPassword())) {
                model.addObject("info", user);

                if (role.equals(User.LIBRARIAN)) {
                    model.setViewName("librarian");
                } else if (role.equals(User.STUDENT) ||
                        Arrays.asList(User.FACULTY).contains(role) ||
                        role.equals(User.PROFESSOR_VISITING)) {
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

}