package com.project.glib.controller;

import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import com.project.glib.model.User;
import com.project.glib.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@RestController
public class LoginController {
    private final UserService userService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;
    private final MessageService messageService;

    @Autowired
    public LoginController(UserService userService, JournalService journalService, AudioVideoService audioVideoService, MessageService messageService) {
        this.userService = userService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
        this.messageService = messageService;
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
    public ModelAndView login(User form, HttpServletRequest request) {
        ModelAndView model = new ModelAndView();
        try {

            User user = userService.findByLogin(form.getLogin());
            String role = user.getRole();

            if (user.getPassword().equals(form.getPassword())) {
                model.addObject("info", user);
                model.addObject("listMessages", messageService.getMessages(user.getLogin()));
                request.getSession().setAttribute("user", user);
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

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody
    User regForm(@RequestBody User user) {
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView mainPage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            User user = (User) request.getSession().getAttribute("user");
            modelAndView.addObject("info", user);
            if (user.getRole().equals(User.LIBRARIAN)) {
                modelAndView.setViewName("librarian");
            } else {
                modelAndView.setViewName("patron");
                modelAndView.addObject("listMessages", messageService.getMessages(user.getLogin()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

//
//    /*
//        CHECKOUT CONTROLLER
//     */
//    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
//    public ModelAndView checkout(@RequestParam String login){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
//    public ModelAndView checkout(@ModelAttribute Booking booking, Model model){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/checkout/all", method = RequestMethod.POST)
//    public String checkoutAllDocument(@RequestParam String login, Model model) {
//        try {
//
//            return "success";
//        }catch (Exception e){
//
//        }
//
//        return "unsuccess";
//    }
}