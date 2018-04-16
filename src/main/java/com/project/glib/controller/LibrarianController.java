package com.project.glib.controller;


import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import com.project.glib.model.User;
import com.project.glib.service.BookService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LibrarianController {

    private final UserService userService;
    private final BookService bookService;

    @Autowired
    public LibrarianController( UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }



    @RequestMapping(value = "/librarian")
    public ModelAndView librarianPage(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("info",user);
        modelAndView.setViewName("librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView AuthUsers(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();

        if (user.getRole().equals(User.LIBRARIAN)) {
            try {
                modelAndView.addObject("allUsers", userService.getListAuthUsersLib());
                modelAndView.setViewName("patrons");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "/add/user", method = RequestMethod.GET)
    public ModelAndView NotAuthUsers(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();

        if (user.getRole().equals(User.LIBRARIAN)) {
            try {
                modelAndView.addObject("notAuth", userService.getListNotAuthUsersLib());
                modelAndView.setViewName("addUser");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return modelAndView;
    }

    @RequestMapping(value = "/edit/book")
    public String editBook(@RequestBody Book book) {
        System.out.println(book);
        try {
            bookService.update(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/add/book", method = RequestMethod.GET)
    public ModelAndView addBookPage(){
        return new ModelAndView("addBook");
    }

    @RequestMapping(value = "/add/book", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody ModelAndView addBook(@RequestBody Book book) {
        System.out.println(book);
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            bookService.update(book);
        } catch (Exception e) {
            String exc = e.toString().replace("java.lang.Exception:  ","");
            modelAndView.addObject("message",exc);
            e.printStackTrace();
            return modelAndView;
        }
        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/journal")
    public ModelAndView editJournal(@ModelAttribute Journal journal) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/add/journal")
    public ModelAndView addJournal(@ModelAttribute Journal journal) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/edit/AV")
    public ModelAndView editAV(@ModelAttribute AudioVideo audioVideo) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/add/AV")
    public ModelAndView addJournal(@ModelAttribute AudioVideo audioVideo) {
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