package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;

import com.project.glib.model.*;
import com.project.glib.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Controller
public class UserController {

    private final MessageService messageService;
    private final BookingService bookingService;
    private final CheckoutService checkoutService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;
    private final DocumentPhysicalService physicalService;

    @Autowired
    public UserController(MessageService messageService, BookingService bookService, CheckoutService checkoutService, BookService bookService1, JournalService journalService, AudioVideoService audioVideoService, DocumentPhysicalService physicalService) {
        this.messageService = messageService;
        this.bookingService = bookService;
        this.checkoutService = checkoutService;
        this.bookService = bookService1;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
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
            modelAndView.addObject("data", "Success! Wait confirmation");
        } catch (Exception e) {
            modelAndView.addObject("data", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/order/journal")
    public @ResponseBody
    ModelAndView orderBook(@RequestBody Journal journal, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        User user = (User) request.getSession().getAttribute("user");
        try {
            bookingService.toBookDocument(journal.getId(), Document.JOURNAL, user.getId());
            modelAndView.addObject("data", "Success! Wait confirmation");
        } catch (Exception e) {
            modelAndView.addObject("data", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/order/av")
    public @ResponseBody
    ModelAndView orderBook(@RequestBody AudioVideo audioVideo, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        User user = (User) request.getSession().getAttribute("user");
        try {
            bookingService.toBookDocument(audioVideo.getId(), Document.AV, user.getId());
            modelAndView.addObject("data", "Success! Wait confirmation");
        } catch (Exception e) {
            modelAndView.addObject("data", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/mydocs")
    public @ResponseBody
    ModelAndView mydocs(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        List<Checkout> checkouts = checkoutService.getCheckoutsByUser(user.getId());
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        List<HashMap<String, String>> docs = new LinkedList<>();
        Calendar calendar = Calendar.getInstance();
        try {
            for (Checkout data : checkouts) {

                HashMap<String, String> nList = new HashMap<>();

                calendar.setTimeInMillis(data.getReturnTime());
                String s = calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) % 12 + 1) +
                        "." + calendar.get(Calendar.YEAR);
                nList.put("return", s);

                long id = physicalService.getDocVirIdById(data.getDocPhysId());
                String type = physicalService.getTypeById(data.getDocPhysId());
                nList.put("type",type);
                switch (type) {
                    case Document.BOOK:
                        Book book = bookService.getById(id);
                        nList.put("title", book.getTitle());
                        nList.put("author", book.getAuthor());
                        break;
                    case Document.JOURNAL:
                        Journal journal = journalService.getById(id);
                        nList.put("title", journal.getTitle());
                        nList.put("author", journal.getAuthor());
                        break;
                    default:
                        AudioVideo av = audioVideoService.getById(id);
                        nList.put("title", av.getTitle());
                        nList.put("author", av.getAuthor());
                        break;
                }
                docs.add(nList);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        modelAndView.addObject("mydocs",docs);
        modelAndView.setViewName("mydocs");
        return modelAndView;
    }

}
