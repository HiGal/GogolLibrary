package com.project.glib.controller;

import com.project.glib.model.Book;
import com.project.glib.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
public class BookController {

    private final BookService bookDao;

    @Autowired
    public BookController(BookService bookDao) {
        this.bookDao = bookDao;
    }

    @RequestMapping(value = "/book/add", method = RequestMethod.POST)
    public String addBook(@RequestBody Book book, @RequestParam(value = "shelf") String shelf) {
        try {
            bookDao.add(book, shelf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    @RequestMapping(value = "/book/remove/{num_copies}", method = RequestMethod.POST)
//    public String removeBook(@RequestBody Book book, @PathVariable("num_copies") int num) {
//        try {
//            for (int i = 0; i < num; i++) {
//                System.out.println(book.getCount());
//                bookDao.decrementCountById(book.getId());
//                System.out.println(book.getCount());
//                docPhysDao.removeByDocIdAndDocType(book.getId(), Document.BOOK);
//            }
//            return "Book/books is/are successfully removed";
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
}
