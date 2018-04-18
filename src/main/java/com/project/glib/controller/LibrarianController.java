package com.project.glib.controller;


import com.project.glib.model.AudioVideo;
import com.project.glib.model.Book;
import com.project.glib.model.Journal;
import com.project.glib.model.User;
import com.project.glib.service.AudioVideoService;
import com.project.glib.service.BookService;
import com.project.glib.service.JournalService;
import com.project.glib.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

import static com.project.glib.model.User.ACCESS;
import static com.project.glib.model.User.LIBSECOND;

@RestController
public class LibrarianController {

    private final UserService userService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService avService;

    @Autowired
    public LibrarianController(UserService userService, BookService bookService, JournalService journalService, AudioVideoService avService) {
        this.userService = userService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.avService = avService;
    }


    @RequestMapping(value = "/librarian")
    public ModelAndView librarianPage(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("info", user);
        modelAndView.setViewName("librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView AuthUsers(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();

        if (Arrays.asList(User.LIBRARIANS).contains(user.getRole())) {
            try {
                modelAndView.addObject("allUsers", userService.getListAuthUsersLib());
                modelAndView.setViewName("patrons");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return modelAndView;
    }

    @RequestMapping(value = "/add/user", method = RequestMethod.GET)
    public ModelAndView NotAuthUsers(HttpServletRequest request) {

        User user = (User) request.getSession().getAttribute("user");
        ModelAndView modelAndView = new ModelAndView();

        if (Arrays.asList(User.LIBRARIANS).contains(user.getRole())) {
            try {
                modelAndView.addObject("notAuth", userService.getListNotAuthUsersLib());
                modelAndView.setViewName("addUser");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return modelAndView;
    }


    @RequestMapping(value = "/add/book", method = RequestMethod.GET)
    public ModelAndView addBookPage() {
        return new ModelAndView("addBook");
    }

    @RequestMapping(value = "/add/book", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    ModelAndView addBook(@RequestBody Book book,
                         @RequestParam(value = "shelf") String shelf,
                         HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        System.out.println(book);
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0) throw new IllegalAccessException();
            bookService.add(book, shelf);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/book")
    public String editBook(@RequestBody Book book) {
        System.out.println(book);
        try {
            bookService.update(book);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/delete/book")
    public ModelAndView delete_book_all(@RequestBody Book book){
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            bookService.remove(book.getId());
            modelAndView.addObject("success", "Book has successfully deleted");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/copies/book", method = RequestMethod.GET)
    public ModelAndView getListOfBookCopies(@RequestBody long bookId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        return modelAndView.addObject(bookService.getListOfShelvesAndCounts(bookId));
    }


    @RequestMapping(value = "/add/journal", method = RequestMethod.GET)
    public ModelAndView addJournalPage() {
        return new ModelAndView("addJournal");
    }

    @RequestMapping(value = "/add/journal", method = RequestMethod.POST, produces = "application/json")
    public ModelAndView addJournal(@RequestBody Journal journal,
                                   @RequestParam(value = "shelf") String shelf,
                                   HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0) throw new IllegalAccessException();
            journalService.add(journal, shelf);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/journal")
    public ModelAndView editJournal(@ModelAttribute Journal journal) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/copies/journal", method = RequestMethod.GET)
    public ModelAndView getListOfJournalCopies(@RequestBody long journalId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        return modelAndView.addObject(journalService.getListOfShelvesAndCounts(journalId));
    }

    @RequestMapping(value = "/edit/AV")
    public ModelAndView editAV(@ModelAttribute AudioVideo audioVideo) {
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/add/AV", method = RequestMethod.GET, produces = "application/json")
    public ModelAndView addAVPage() {
        return new ModelAndView("addAV");
    }

    @RequestMapping(value = "/add/AV")
    public ModelAndView addAV(@ModelAttribute AudioVideo audioVideo,
                              @RequestParam(value = "shelf") String shelf,
                              HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0) throw new IllegalAccessException();
            avService.add(audioVideo, shelf);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value = "/copies/av", method = RequestMethod.GET)
    public ModelAndView getListOfAVCopies(@RequestBody long avId, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        return modelAndView.addObject(avService.getListOfShelvesAndCounts(avId));
    }

    @RequestMapping(value = "/librarian/taken_doc", method = RequestMethod.GET)
    public ModelAndView takenDoc() {
        ModelAndView modelAndView = new ModelAndView("taken_documents");
        return modelAndView;
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public @ResponseBody
    String UserDelete(@RequestBody User user1) {
        System.out.println(user1);
        try {
            userService.remove(user1.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User deleted";
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public @ResponseBody
    String UserEdit(@RequestBody User user) {
        try {
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "success";
    }

    @RequestMapping(value = "/user/confirm", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView UserConfirm(@RequestBody User user, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        try {
            User userInSession = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(userInSession.getRole()) - ACCESS.get(LIBSECOND) < 0) throw new IllegalAccessException();
            User userInDao = userService.getById(user.getId());
            userInDao.setAuth(user.getAuth());
            userService.update(userInDao);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }
}