package com.project.glib.controller;

import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.model.Book;
import com.project.glib.model.Document;
import com.project.glib.model.DocumentPhysical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
public class BookController {

    private final BookRepository bookRepository;
    private final BookDaoImplementation bookDao;
    private final DocumentPhysicalDaoImplementation docPhysDao;

    @Autowired
    public BookController(BookRepository bookRepository, BookDaoImplementation bookDao, DocumentPhysicalDaoImplementation documentPhysicalDao) {
        this.bookRepository = bookRepository;
        this.bookDao = bookDao;
        this.docPhysDao = documentPhysicalDao;
    }

    @RequestMapping(value = "/book/add", method = RequestMethod.POST)
    public String addBook(@RequestBody Book book, @RequestParam(value = "shelf") String shelf) {
        try {
            bookDao.add(book);
            for (int i = 0; i < book.getCount(); i++) {
                // TODO add keywords options
                docPhysDao.add(
                        new DocumentPhysical(shelf, true, book.getId(), Document.BOOK, null));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/book/remove/{num_copies}", method = RequestMethod.POST)
    public String removeBook(@RequestBody Book book, @PathVariable("num_copies") long num) {
        try {
            for (int i = 0; i < num; i++) {
                System.out.println(book.getCount());
                bookDao.decrementCountById(book.getId());
                System.out.println(book.getCount());
                docPhysDao.removeByDocId(book.getId());
            }
            return "Book/books is/are successfully removed";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
