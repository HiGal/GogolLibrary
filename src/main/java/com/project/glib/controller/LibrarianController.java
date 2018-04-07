package com.project.glib.controller;


import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import com.project.glib.model.User;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LibrarianController {

    private final UserService userService;

    @Autowired
    public LibrarianController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        System.out.println(user);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", userService.getList());
        modelAndView.setViewName("patrons");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/book")
    public ModelAndView editBook(@ModelAttribute Book book){
        ModelAndView modelAndView = new ModelAndView();
        return  modelAndView;
    }

    @RequestMapping(value = "/add/book")
    public ModelAndView addBook(@ModelAttribute Book book){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/edit/journal")
    public ModelAndView editJournal(@ModelAttribute Journal journal){
        ModelAndView modelAndView = new ModelAndView();
        return  modelAndView;
    }

    @RequestMapping(value = "/add/journal")
    public ModelAndView addJournal(@ModelAttribute Journal journal){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/edit/AV")
    public ModelAndView editAV(@ModelAttribute AudioVideo audioVideo){
        ModelAndView modelAndView = new ModelAndView();
        return  modelAndView;
    }

    @RequestMapping(value = "/add/AV")
    public ModelAndView addJournal(@ModelAttribute AudioVideo audioVideo){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
    @RequestMapping(value = "/librarian/taken_doc", method = RequestMethod.GET)
    public ModelAndView takenDoc() {
        ModelAndView modelAndView = new ModelAndView("taken_documents");
        return modelAndView;
    }
    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public @ResponseBody
    String UserDelete(@RequestBody User user1) {
        System.out.println(user1);
        try {
            userService.remove(user1.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User deleted";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public @ResponseBody
    String UserEdit(@RequestBody User user) {
        try {
            System.out.println(user);
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping(value = "/user/confirm", method = RequestMethod.POST)
    public @ResponseBody
    String UserConfirm(@RequestBody User user) {

        try {
            User user1 = userService.getById(user.getId());
            System.out.println();
            System.out.println();
            System.out.println(user);
            System.out.println(user.getAuth());
            System.out.println();
            System.out.println();
            System.out.println();
            user1.setAuth(user.getAuth());
            userService.update(user1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User's registration confirmed";
    }
}