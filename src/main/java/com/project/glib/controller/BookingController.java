package com.project.glib.controller;

import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Booking;
import com.project.glib.model.Journal;
import com.project.glib.service.AudioVideoService;
import com.project.glib.service.BookService;
import com.project.glib.service.BookingService;
import com.project.glib.service.JournalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BookingController {
    private final BookingService bookingService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;


    @Autowired
    public BookingController(BookingService bookingService,
                             BookService bookService,
                             JournalService journalService,
                             AudioVideoService avService) {
        this.bookingService = bookingService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
    }

    //return list of books
    @ResponseBody
    @RequestMapping(value = "/booking/book", method = RequestMethod.GET)
    public List<Book> bookingBook() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allBooks", bookService.getList());
        modelAndView.setViewName("order_book");
        return bookService.getList();
    }

    @RequestMapping(value = "/booking/book", method = RequestMethod.POST)
    public String bookingBook(@RequestBody Booking bookingForm, Model model) {
        try {
            bookingService.toBookDocument(bookingForm.getDocPhysId(), bookingForm.getDocType(), bookingForm.getUserId());
            return " book successfully ordered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";
    }

    @RequestMapping(value = "/booking/journal", method = RequestMethod.GET)
    public List<Journal> bookingJournal() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allJournals", journalService.getList());
        modelAndView.setViewName("orderJ");
        return journalService.getList();
    }

    @RequestMapping(value = "/booking/journal", method = RequestMethod.POST)
    public String bookingJournal(@RequestBody Booking bookingForm, Model model) {
        try {
            bookingService.toBookDocument(bookingForm.getDocPhysId(), bookingForm.getDocType(), bookingForm.getUserId());
            return "journal successfully ordered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";
    }

    @RequestMapping(value = "/booking/av", method = RequestMethod.GET)
    public List<AudioVideo> bookingAV() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allAv", avService.getList());
        modelAndView.setViewName("orderAV");
        return avService.getList();
    }

    @RequestMapping(value = "/booking/av", method = RequestMethod.POST)
    public String bookingAV(@RequestBody Booking bookingForm, Model model) {
        try {
            bookingService.toBookDocument(bookingForm.getDocPhysId(), bookingForm.getDocType(), bookingForm.getUserId());
            return "audio_video successfully ordered";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";
    }

}

