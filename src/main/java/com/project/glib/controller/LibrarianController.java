package com.project.glib.controller;


import com.project.glib.model.*;
import com.project.glib.service.*;
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
    private final AudioVideoService audioVideoService;
    private final AudioVideoService avService;
    private final LoggerService loggerService;
    private final DocumentPhysicalService documentPhysicalService;

    @Autowired
    public LibrarianController(UserService userService, BookService bookService, JournalService journalService, AudioVideoService audioVideoService, AudioVideoService avService, LoggerService loggerService, DocumentPhysicalService documentPhysicalService) {
        this.userService = userService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
        this.avService = avService;
        this.loggerService = loggerService;
        this.documentPhysicalService = documentPhysicalService;
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
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0) throw new IllegalAccessException();
            bookService.add(book, shelf);
            long docPhysId = documentPhysicalService.getValidPhysId(bookService.getId(book), Document.BOOK);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.ADDED_BOOK, System.currentTimeMillis(), Document.BOOK);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }
        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/book")
    public String editBook(@RequestBody Book book, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            bookService.update(book);
            long docPhysId = documentPhysicalService.getValidPhysId(bookService.getId(book), Document.BOOK);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.MODIFIED_BOOK, System.currentTimeMillis(), Document.BOOK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/delete/all/book")
    public ModelAndView delete_book_all(@RequestBody Book book, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            bookService.remove(book.getId());
            long docPhysId = documentPhysicalService.getValidPhysId(bookService.getId(book), Document.BOOK);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.DELETED_BOOK, System.currentTimeMillis(), Document.BOOK);
            modelAndView.addObject("success", "Book has successfully deleted");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/copies/book")
    public @ResponseBody  ModelAndView getListOfBookCopies(@RequestBody Book book) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("copies", bookService.getListOfShelvesAndCounts(book.getId()));
        System.out.println(modelAndView);
     //   modelAndView.setViewName("documents");
        return modelAndView;
    }

//    @RequestMapping(value = "/copies/book", method = RequestMethod.GET)
//    public Model getListOfBookCopies(@RequestBody long bookId, HttpServletRequest request) {
//        Model model = new Model(new MappingJackson2JsonView());
//        model.addObject("copies", bookService.getListOfShelvesAndCounts(bookId));
//        return model;
//    }


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
            long docPhysId = documentPhysicalService.getValidPhysId(journalService.getId(journal), Document.JOURNAL);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.ADDED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value = "/edit/journal")
    public String editJournal(@RequestBody Journal journal, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            journalService.update(journal);
            long docPhysId = documentPhysicalService.getValidPhysId(journalService.getId(journal), Document.JOURNAL);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.MODIFIED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/delete/all/journals")
    public String delete_all_journals(@RequestBody Journal journal, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            journalService.remove(journal.getId());
            long docPhysId = documentPhysicalService.getValidPhysId(journalService.getId(journal), Document.JOURNAL);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.DELETED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/copies/journal", method = RequestMethod.GET)
    public ModelAndView getListOfJournalCopies(@RequestBody long journalId) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        return modelAndView.addObject(journalService.getListOfShelvesAndCounts(journalId));
    }

    @RequestMapping(value = "/edit/AV")
    public String editAV(@RequestBody AudioVideo audioVideo, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            avService.update(audioVideo);
            long docPhysId = documentPhysicalService.getValidPhysId(audioVideoService.getId(audioVideo), Document.AV);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.MODIFIED_AV, System.currentTimeMillis(), Document.AV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/add/AV", method = RequestMethod.GET)
    public ModelAndView addAVPage() {
        return new ModelAndView("addAV");
    }

    @RequestMapping(value = "/add/AV")
    public ModelAndView addAV(@RequestBody AudioVideo audioVideo,
                              @RequestParam(value = "shelf") String shelf,
                              HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0) throw new IllegalAccessException();
            avService.add(audioVideo, shelf);
            long docPhysId = documentPhysicalService.getValidPhysId(audioVideoService.getId(audioVideo), Document.AV);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.ADDED_AV, System.currentTimeMillis(), Document.AV);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    @RequestMapping(value="/delete/all/av")
    public String delete_all_av(@RequestBody AudioVideo audioVideo, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            avService.remove(audioVideo.getId());
            long docPhysId = documentPhysicalService.getValidPhysId(audioVideoService.getId(audioVideo), Document.AV);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.DELETED_AV, System.currentTimeMillis(), Document.AV);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
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