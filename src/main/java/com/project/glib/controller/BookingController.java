package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.JournalDaoImplementation;
import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Booking;
import com.project.glib.model.Journal;
import com.project.glib.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

//@Controller
@RestController
public class BookingController {

    private final BookingService bookingService;
    private final BookDaoImplementation bookDao;
    private final JournalDaoImplementation journalDao;
    private final AudioVideoDaoImplementation audioVideoDao;


    @Autowired
    public BookingController(BookingService bookingService, BookDaoImplementation bookDao, JournalDaoImplementation journalDao, AudioVideoDaoImplementation audioVideoDao) {
        this.bookingService = bookingService;
        this.bookDao = bookDao;
        this.journalDao = journalDao;
        this.audioVideoDao = audioVideoDao;
    }

    //return list of books
    @ResponseBody
    @RequestMapping(value = "/booking/book", method = RequestMethod.GET)
    public List<Book> bookingBook() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allBooks", bookDao.getList());
        modelAndView.setViewName("order_book");
        return bookDao.getList();
    }

    @RequestMapping(value = "/booking/book", method = RequestMethod.POST)
    public String bookingBook(@RequestBody Booking bookingForm, Model model)  {
        try {
            bookingService.toBookDocument(bookingForm.getDocPhysId(), bookingForm.getDocType(), bookingForm.getUserId());
            return " book successfully ordered";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
  //      return "order";
    }

    @RequestMapping(value = "/booking/journal", method = RequestMethod.GET)
    public List<Journal> bookingJournal() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allJournals", journalDao.getList());
        modelAndView.setViewName("orderJ");
        return journalDao.getList();
    }

    @RequestMapping(value = "/booking/journal", method = RequestMethod.POST)
    public String bookingJournal(@RequestBody Booking bookingForm, Model model)  {
        try {
            bookingService.toBookDocument(bookingForm.getDocPhysId(), bookingForm.getDocType(), bookingForm.getUserId());
            return "journal successfully ordered";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";
    }

    @RequestMapping(value = "/booking/av", method = RequestMethod.GET)
    public List<AudioVideo> bookingAV() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allAv", audioVideoDao.getList());
        modelAndView.setViewName("orderAV");
        return audioVideoDao.getList();
    }

    @RequestMapping(value = "/booking/av", method = RequestMethod.POST)
    public String bookingAV(@RequestBody Booking bookingForm, Model model)  {
        try {
            bookingService.toBookDocument(bookingForm.getDocPhysId(), bookingForm.getDocType(), bookingForm.getUserId());
            return "audio_video successfully ordered";
        }catch (Exception e){
            model.addAttribute("error", e.getMessage());
            return e.getMessage();
        }
        //      return "order";
    }

}

