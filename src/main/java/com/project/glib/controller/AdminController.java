package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.LoggerService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class AdminController {

    private final UserService userService;
    private final LoggerService loggerService;

    @Autowired
    public AdminController(UserService userService, LoggerService loggerService) {
        this.userService = userService;
        this.loggerService = loggerService;
    }

    @RequestMapping(value = "/admin")
    public ModelAndView adminPage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("info", user);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @RequestMapping(value = "/add/librarian")
    public ModelAndView addPage(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("add_librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/add/librarian", method = RequestMethod.POST)
    public String libForm(@RequestBody User user){
        try {
            userService.add(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/log")
    public ModelAndView log(HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView();
        try {
            modelAndView.addObject("actions",loggerService.showLoggerForaWeekString());
            System.out.println(loggerService.showLoggerForaWeekString());
            modelAndView.setViewName("log");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }


}
