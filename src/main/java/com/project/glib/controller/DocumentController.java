package com.project.glib.controller;

import com.project.glib.model.User;
import com.project.glib.service.AudioVideoService;
import com.project.glib.service.BookService;
import com.project.glib.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Controller
public class DocumentController {

    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;

    @Autowired
    public DocumentController(BookService bookService,
                              JournalService journalService,
                              AudioVideoService audioVideoService) {
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
    }

    @RequestMapping(value = "/books", method = RequestMethod.GET)
    public ModelAndView books(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        try {
            User user = (User) request.getSession().getAttribute("user");
            String role = user.getRole();
            modelAndView.addObject("allBooks", bookService.getList());
            if (role.equals(User.LIBRARIAN))
                modelAndView.setViewName("documents");
            else
                modelAndView.setViewName("order");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/journals", method = RequestMethod.GET)
    public ModelAndView journals(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allJournals", journalService.getList());
        try {
            String role = ((User) request.getSession().getAttribute("user")).getRole();
            if (role.equals(User.LIBRARIAN))
                modelAndView.setViewName("journals");
            else
                modelAndView.setViewName("orderJ");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/av", method = RequestMethod.GET)
    public ModelAndView av(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allAV", audioVideoService.getList());
        try {
            String role = ((User) request.getSession().getAttribute("user")).getRole();
            if (role.equals(User.LIBRARIAN))
                modelAndView.setViewName("av");
            else
                modelAndView.setViewName("orderAV");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return modelAndView;
    }

}
