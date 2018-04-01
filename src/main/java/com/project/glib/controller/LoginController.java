package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class LoginController {

    @Autowired
    UserService userService;

    public static final String role1 = "LIBRARIAN";
    public static final String role2 = "STUDENT";
    public static final String role3 = "TEACHER_ASSISTANT";
    public static final String role4 = "VISITING_PROFESSOR";
    public static final String role5 = "PROFESSOR";
    public static final String role6 = "INSTRUCTOR";

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {

        return new ModelAndView("login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login( @ModelAttribute User form) {
        System.out.println();
        System.out.println();
        System.out.println(form);
        System.out.println();
        System.out.println();
        ModelAndView model= new ModelAndView();
        try {
            System.out.println(form.getLogin());
            User user = userService.findByLogin(form.getLogin());
            System.out.println(user);

            if (user != null && user.getPassword().equals(form.getPassword())) {
                model.addObject("info",user);
                if (user.getRole().equals(role1)) {
                    System.out.println("librarian");
                    model.setViewName("librarian");
                } else if(user.getRole().equals(role2) || user.getRole().equals(role3) ||
                          user.getRole().equals(role4) || user.getRole().equals(role5)
                          || user.getRole().equals(role6)){
                    System.out.println("student");
                    model.setViewName("student");
                }
                System.out.println("return  1");
                return model;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("return 2");
        return model;
    }

}