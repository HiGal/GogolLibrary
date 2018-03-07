//package com.project.glib.controller;
//
//import com.project.glib.dao.interfaces.BookRepository;
//import com.project.glib.model.Book;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//@RestController()
//public class BookController {
//
//    private final BookRepository bookRepository;
//
//    @Autowired
//    public BookController(BookRepository bookRepository) {
//        this.bookRepository = bookRepository;
//    }
//
//
//
//    @RequestMapping(value = "/books/add", method = RequestMethod.POST)
//    public String addBook(@ModelAttribute("book") Book book) {
//        if (book.getId() == 0) {
//            this.bookRepository.save(book);
//        } else {
//            this.bookRepository.save(book);
//        }
//
//        return "redirect:/books";
//    }
//
//    @RequestMapping("books/remove/{id}")
//    public String removeBook(@PathVariable("id") long id) {
//        this.bookRepository.delete(id);
//
//        return "redirect:/books";
//    }
//
//    @RequestMapping("books/edit/{id}")
//    public String editBook(@PathVariable("id") long id, Model model) {
//        model.addAttribute("book", this.bookRepository.getOne(id));
//        model.addAttribute("listBooks", this.bookRepository.findAll());
//
//        return "books";
//    }
//
//    @RequestMapping("bookdata/{id}")
//    public String bookData(@PathVariable("id") long id, Model model) {
//        model.addAttribute("book", this.bookRepository.getOne(id));
//
//        return "bookdata";
//    }
//}
