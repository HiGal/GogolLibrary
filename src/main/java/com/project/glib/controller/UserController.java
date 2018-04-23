package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;

import com.project.glib.model.*;
import com.project.glib.service.BookingService;
import com.project.glib.service.DocumentPhysicalService;
import com.project.glib.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class UserController {

    private final MessageService messageService;
    private final BookingService bookingService;
    private final DocumentPhysicalService physicalService;

    @Autowired
    public UserController(MessageService messageService, BookingService bookService, DocumentPhysicalService physicalService) {
        this.messageService = messageService;
        this.bookingService = bookService;
        this.physicalService = physicalService;
    }

    @RequestMapping(value = "/patron")
    public ModelAndView UserPage(HttpServletRequest request) throws Exception {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("message", messageService.getMessages(user.getLogin()));
        modelAndView.addObject("info", user);
        modelAndView.setViewName("patron");
        return modelAndView;
    }

    @RequestMapping(value = "/order/book", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView orderBook(@RequestBody Book book, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        User user = (User) request.getSession().getAttribute("user");
        try {
            bookingService.toBookDocument(book.getId(), Document.BOOK, user.getId());
            modelAndView.addObject("data","Success! Wait confirmation");
        } catch (Exception e) {
            modelAndView.addObject("data",e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/order/journal")
    public @ResponseBody ModelAndView orderBook(@RequestBody Journal journal, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        User user = (User) request.getSession().getAttribute("user");
        try {
            bookingService.toBookDocument(journal.getId(), Document.JOURNAL, user.getId());
            modelAndView.addObject("data","Success! Wait confirmation");
        } catch (Exception e) {
            modelAndView.addObject("data",e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/order/av")
    public @ResponseBody ModelAndView orderBook(@RequestBody AudioVideo audioVideo, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        User user = (User) request.getSession().getAttribute("user");
        try {
            bookingService.toBookDocument(audioVideo.getId(), Document.AV, user.getId());
            modelAndView.addObject("data","Success! Wait confirmation");
        } catch (Exception e) {
            modelAndView.addObject("data",e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

}
