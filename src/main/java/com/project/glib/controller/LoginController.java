package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.AudioVideoService;
import com.project.glib.service.JournalService;
import com.project.glib.service.MessageService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Controller
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

    @RequestMapping(value = "/")
    public String redirect() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        return new ModelAndView("login", "data", "");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView login(@RequestBody User form, HttpServletRequest request) {
        ModelAndView model = new ModelAndView(new MappingJackson2JsonView());
        request.getSession().removeAttribute("user");
        try {

            User user = userService.findByLogin(form.getLogin());
            String role = user.getRole();

            if (user.getPassword().equals(form.getPassword()) && user.getAuth()) {
                request.getSession().setAttribute("user", user);
                if (Arrays.asList(User.LIBRARIANS).contains(role)) {
                    model.addObject("data", "/librarian");
                } else if (Arrays.asList(User.PATRONS).contains(role)) {
                    model.addObject("data", "/patron");
                } else if (role.equals(User.ADMIN)){
                    model.addObject("data","/admin");

                } else {
                    model.addObject("data", "Something goes wrong");
                    throw new Exception("WRONG ROLE");
                }

            } else if (!user.getPassword().equals(form.getPassword()))
                model.addObject("data", "Login or password is incorrect");
            else if (!user.getAuth())
                model.addObject("data", "You are not confirmed yet");
        } catch (Exception e) {
            model.addObject("data", "You aren't registered in the system");
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
    public
    @ResponseBody
    User regForm(@RequestBody User user) {
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView mainPage( HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            User user = (User) request.getSession().getAttribute("user");
            modelAndView.addObject("info", user);

            if (Arrays.asList(User.LIBRARIANS).contains(user.getRole())) {
                modelAndView.setViewName("librarian");
            } else if(user.getRole().equals(User.ADMIN)){
                modelAndView.setViewName("admin");
            }else {
                modelAndView.setViewName("patron");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }


    @RequestMapping(value = "/checked", method = RequestMethod.POST)
    public void read_messages(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        try {
            messageService.sendMessagesToLib(user.getLogin());
            messageService.removeAllByUserID(user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}