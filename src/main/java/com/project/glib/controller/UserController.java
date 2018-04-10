package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import com.project.glib.model.User;
import com.project.glib.service.AudioVideoService;
import com.project.glib.service.BookService;
import com.project.glib.service.JournalService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class UserController {

    @RequestMapping(value = "/patron")
    public ModelAndView UserPage(HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView  modelAndView = new ModelAndView();
        modelAndView.addObject("info",user);
        modelAndView.setViewName("patron");
        return modelAndView;
    }

    @RequestMapping(value = "/order/book")
    public ModelAndView orderBook(@ModelAttribute Book book) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
    @RequestMapping(value = "/order/journal")
    public ModelAndView orderBook(@ModelAttribute Journal journal){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }
    @RequestMapping(value = "/order/AV")
    public ModelAndView orderBook(@ModelAttribute AudioVideo audioVideo){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

}
