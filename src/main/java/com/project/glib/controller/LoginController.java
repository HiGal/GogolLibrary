package com.project.glib.controller;

import com.project.glib.model.*;
import com.project.glib.service.BookService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
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

    /*
        BOOK CONTROLLER
     */
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ModelAndView books() {
        ModelAndView modelAndView = new ModelAndView();
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

    @RequestMapping(value = "/order/book")
    public ModelAndView orderBook(@ModelAttribute Book book){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /*
        JOURNAL CONTROLLER
     */
    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ModelAndView journals() {
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

    @RequestMapping(value = "/order/journal")
    public ModelAndView orderBook(@ModelAttribute Journal journal){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /*
        AV CONTROLLER
     */
    @RequestMapping(value = "/av", method = RequestMethod.GET)
    public ModelAndView av() {
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
    @RequestMapping(value = "/order/AV")
    public ModelAndView orderBook(@ModelAttribute AudioVideo audioVideo){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    /*
        CHECKOUT CONTROLLER
     */
    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
    public ModelAndView checkout(@RequestParam String login){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
    public ModelAndView checkout(@ModelAttribute Booking booking, Model model){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/checkout/all", method = RequestMethod.POST)
    public String checkoutAllDocument(@RequestParam String login, Model model) {
        try {

            return "success";
        }catch (Exception e){

        }

        return "unsuccess";
    }


}