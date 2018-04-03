package com.project.glib.controller;

import com.project.glib.model.Book;
import com.project.glib.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @RequestMapping(value = "/book/add", method = RequestMethod.POST)
    public String addBook(@RequestBody Book book, @RequestParam(value = "shelf") String shelf) {
        try {
            bookService.add(book, shelf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    @RequestMapping(value = "/book/remove/{num_copies}", method = RequestMethod.POST)
//    public String removeBook(@RequestBody Book book) {
//        try {
//            bookService.remove(book.getId());
//            return "Book is successfully removed";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }

//    @RequestMapping(value = "/book/remove/{id_copy}", method = RequestMethod.POST)
//    public String removeCopyOfBook(@RequestBody Book book, @PathVariable("id_copy") long copyId) {
//        try {
//            bookService.removeCopy(book.getId(), copyId);
//            return "Copy of book is successfully removed";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
