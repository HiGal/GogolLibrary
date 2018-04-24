package com.project.glib.controller;


import com.project.glib.model.*;
import com.project.glib.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.project.glib.model.User.*;

@RestController
public class LibrarianController {
    public static final String RIGHT_PERMISSION_EXCEPTION = "You have not rights to do this action";

    private final UserService userService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;
    private final AudioVideoService avService;
    private final LoggerService loggerService;
    private final DocumentPhysicalService documentPhysicalService;
    private final BookingService bookingService;
    private final CheckoutService checkoutService;

    @Autowired
    public LibrarianController(UserService userService, BookService bookService,
                               JournalService journalService, AudioVideoService audioVideoService,
                               AudioVideoService avService, LoggerService loggerService,
                               DocumentPhysicalService documentPhysicalService, BookingService bookingService,
                               CheckoutService checkoutService) {
        this.userService = userService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
        this.avService = avService;
        this.loggerService = loggerService;
        this.documentPhysicalService = documentPhysicalService;
        this.bookingService = bookingService;
        this.checkoutService = checkoutService;
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
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            bookService.add(book, shelf);
            long docPhysId = documentPhysicalService.getValidPhysId(bookService.getId(book), Document.BOOK);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.ADDED_BOOK, System.currentTimeMillis(), Document.BOOK, true);
            modelAndView.addObject("message", "succ");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/edit/book")
    public String editBook(@RequestBody Book book, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBFIRST) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            System.out.println();
            System.out.println(book);
            System.out.println();
            bookService.update(book);
            long docPhysId = documentPhysicalService.getValidPhysId(book.getId(), Document.BOOK);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.MODIFIED_BOOK, System.currentTimeMillis(), Document.BOOK, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/delete/all/book")
    public ModelAndView delete_book_all(@RequestBody Book book, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        Book temp = bookService.getById(book.getId());
        try {

            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            long docPhysId = documentPhysicalService.getValidPhysId(bookService.getId(temp), Document.BOOK);
            bookService.remove(book.getId());
            loggerService.addLog(user.getId(), docPhysId,
                    LoggerService.DELETED_ALL_BOOKS + " \" " + book.getTitle() + "\"  by " + book.getAuthor(),
                    System.currentTimeMillis(), Document.BOOK, false);
            modelAndView.addObject("success", "Book has successfully deleted");
        } catch (Exception e) {
            modelAndView.addObject("error", e.getMessage());
            e.printStackTrace();
        }

        return modelAndView;
    }


    //todo write logger !!
    @RequestMapping(value = "/update/phys/book",method = RequestMethod.POST)
    public @ResponseBody ModelAndView update_phys_book(@RequestBody Book data,
                                                       @RequestParam(value = "shelf") String shelf,
                                                       HttpServletRequest request) {
        Book book = bookService.getById(data.getId());
        int count = data.getCount();
        data.setPrice(book.getPrice());
        data.setAuthor(book.getAuthor());
        data.setEdition(book.getEdition());
        data.setTitle(book.getTitle());
        data.setPublisher(book.getPublisher());
        data.setPicture(book.getPicture());
        data.setNote(book.getNote());
        data.setYear(book.getYear());
        data.setKeywords(book.getKeywords());
        try {
            User user = (User) request.getSession().getAttribute("user");
            boolean hasDeletePermission = ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) >= 0;
            boolean hasAddPermission = ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) >= 0;
            if (count == -1 && hasDeletePermission) {
                bookService.removeCopyByShelf(book.getId(),shelf);
            } else if (count == 1 && hasAddPermission){
                bookService.add(data, shelf);
            } else {
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("data","succ");
        return modelAndView;
    }

    @RequestMapping(value = "/copies/book")
    public @ResponseBody
    ModelAndView getListOfBookCopies(@RequestBody Book book) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("copies", bookService.getListOfShelvesAndCounts(book.getId()));
        return modelAndView;
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
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            journalService.add(journal, shelf);
            long docPhysId = documentPhysicalService.getValidPhysId(journalService.getId(journal), Document.JOURNAL);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.ADDED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL, true);
            modelAndView.addObject("message", "succ");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/edit/journal")
    public String editJournal(@RequestBody Journal journal, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBFIRST) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            journalService.update(journal);
            long docPhysId = documentPhysicalService.getValidPhysId(journalService.getId(journal), Document.JOURNAL);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.MODIFIED_JOURNAL, System.currentTimeMillis(), Document.JOURNAL, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "succ";
    }

    //todo write Logger !!
    @RequestMapping(value = "/update/phys/journal", method = RequestMethod.POST)
    public @ResponseBody ModelAndView update_phys_journal(@RequestBody Journal data,
                                                          @RequestParam(value = "shelf") String shelf, HttpServletRequest request) {
        Journal journal = journalService.getById(data.getId());
        int count = data.getCount();
        data = new Journal(journal);
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            boolean hasDeletePermission = ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) >= 0;
            boolean hasAddPermission = ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) >= 0;
            if (count == -1 && hasDeletePermission) {
                journalService.removeCopyByShelf(journal.getId(),shelf);
            } else if (count == 1 && hasAddPermission) {
                journalService.add(data, shelf);
            } else {
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            }
            modelAndView.addObject("data", "succ");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/delete/all/journals")
    public @ResponseBody ModelAndView delete_all_journals(@RequestBody Journal journal, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            long docPhysId = documentPhysicalService.getValidPhysId(journalService.getId(journal), Document.JOURNAL);
            journalService.remove(journal.getId());
            loggerService.addLog(user.getId(), docPhysId,
                    LoggerService.DELETED_JOURNAL + " \" " + journal.getTitle() + "\" by " + journal.getAuthor(),
                    System.currentTimeMillis(), Document.JOURNAL, false);
            modelAndView.addObject("You have successfully deleted all copies");
        } catch (Exception e) {
            modelAndView.addObject(e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/copies/journal")
    public @ResponseBody ModelAndView getListOfJournalCopies(@RequestBody Journal journal) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(journalService.getListOfShelvesAndCounts(journal.getId()));
        return modelAndView;
    }

    @RequestMapping(value = "/edit/AV")
    public String editAV(@RequestBody AudioVideo audioVideo, HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBFIRST) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            avService.update(audioVideo);
            long docPhysId = documentPhysicalService.getValidPhysId(audioVideoService.getId(audioVideo), Document.AV);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.MODIFIED_AV, System.currentTimeMillis(), Document.AV, true);
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
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            avService.add(audioVideo, shelf);
            long docPhysId = documentPhysicalService.getValidPhysId(audioVideoService.getId(audioVideo), Document.AV);
            loggerService.addLog(user.getId(), docPhysId, LoggerService.ADDED_AV, System.currentTimeMillis(), Document.AV, true);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
            return modelAndView;
        }

        modelAndView.addObject("message", "succ");
        return modelAndView;
    }

    // todo write Logger !!
    @RequestMapping(value = "/update/phys/av")
    public @ResponseBody ModelAndView update_phys_av(@RequestBody AudioVideo data,
                                                     @RequestParam(value = "shelf") String shelf, HttpServletRequest request) {
        AudioVideo av = audioVideoService.getById(data.getId());
        int count = data.getCount();
        data.setPrice(av.getPrice());
        data.setAuthor(av.getAuthor());
        data.setTitle(av.getTitle());
        data.setId(av.getId());
        data.setKeywords(av.getKeywords());
        data.setPicture(av.getPicture());
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            boolean hasDeletePermission = ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) >= 0;
            boolean hasAddPermission = ACCESS.get(user.getRole()) - ACCESS.get(LIBSECOND) >= 0;
            if (count == -1 && hasDeletePermission) {
                audioVideoService.removeCopyByShelf(av.getId(),shelf);
            } else if (count == 1 && hasAddPermission) {
                audioVideoService.add(data, shelf);
            } else {
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            }
            modelAndView.addObject("data", "succ");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }


    @RequestMapping(value = "/delete/all/av")
    public String delete_all_av(@RequestBody AudioVideo audioVideo, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            avService.remove(audioVideo.getId());
            long docPhysId = documentPhysicalService.getValidPhysId(audioVideoService.getId(audioVideo), Document.AV);
            loggerService.addLog(user.getId(), docPhysId,
                    LoggerService.DELETED_AV + " \" " + audioVideo.getTitle() + "\" by " + audioVideo.getAuthor(),
                    System.currentTimeMillis(), Document.AV, false);
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }
        return "succ";
    }

    @RequestMapping(value = "/copies/av")
    public @ResponseBody ModelAndView getListOfAVCopies(@RequestBody AudioVideo av) {
        System.out.println(avService.getListOfShelvesAndCounts(av.getId()));
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject(avService.getListOfShelvesAndCounts(av.getId()));
        System.out.println(modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = "/librarian/taken_doc", method = RequestMethod.GET)
    public ModelAndView takenDoc() {
        ModelAndView modelAndView = new ModelAndView("taken_documents");
        return modelAndView;
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView UserDelete(@RequestBody User user1, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBTHIRD) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            userService.remove(user1.getId());
            modelAndView.addObject("User deleted");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }
        return modelAndView;
    }

    @RequestMapping(value = "/user/edit", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView UserEdit(@RequestBody User user1, HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        try {
            User user = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(user.getRole()) - ACCESS.get(LIBFIRST) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            userService.update(user1);
            modelAndView.addObject("success");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "/user/confirm", method = RequestMethod.POST)
    public @ResponseBody
    ModelAndView UserConfirm(@RequestBody User user, HttpServletRequest request) {

        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        try {
            User userInSession = (User) request.getSession().getAttribute("user");
            if (ACCESS.get(userInSession.getRole()) - ACCESS.get(LIBSECOND) < 0)
                throw new IllegalAccessException(RIGHT_PERMISSION_EXCEPTION);
            User userInDao = userService.getById(user.getId());
            userInDao.setAuth(user.getAuth());
            userService.update(userInDao);
            modelAndView.addObject("message", "succ");
        } catch (Exception e) {
            modelAndView.addObject("message", e.getMessage());
            e.printStackTrace();
        }

        return modelAndView;
    }

    @RequestMapping(value = "user/requests")
    public ModelAndView requests(){
        ModelAndView modelAndView = new ModelAndView();
        List<Booking> list = bookingService.getList();
        List<HashMap<String,String>> nList = new LinkedList<>();
        for(Booking person: list){
            User user = userService.getById(person.getUserId());
            HashMap<String,String> rList = new HashMap<>();
            rList.put("phys_id", String.valueOf(person.getDocPhysId()));
            rList.put("user_id", String.valueOf(person.getUserId()));
            rList.put("id", String.valueOf(person.getId()));
            rList.put("shelf", person.getShelf());
            rList.put("name",user.getName());
            rList.put("surname",user.getSurname());
            rList.put("type",person.getDocType());
            if(person.getDocType().equals(Document.BOOK)){
                Book book = bookService.getById(person.getDocVirId());
                rList.put("title",book.getTitle());
                rList.put("author",book.getAuthor());
            } else if(person.getDocType().equals(Document.JOURNAL)){
                Journal journal = journalService.getById(person.getDocVirId());
                rList.put("title",journal.getTitle());
                rList.put("author",journal.getAuthor());
            } else {
                AudioVideo audioVideo = audioVideoService.getById(person.getDocVirId());
                rList.put("title",audioVideo.getTitle());
                rList.put("author",audioVideo.getAuthor());
            }
            nList.add(rList);
        }
        modelAndView.addObject("requests",nList);
        modelAndView.setViewName("order_requests");
        System.out.println(modelAndView);
        return modelAndView;
    }

    @RequestMapping(value = "/confirm/request")
    public ModelAndView confirm_request(@RequestBody Booking booking,
                                        HttpServletRequest request){
        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());

        User user = (User) request.getSession().getAttribute("user");
        if(Arrays.asList(User.LIBRARIANS).contains(user.getRole())){
            try {
                System.out.println(booking);
                checkoutService.toCheckoutDocument(booking);
                modelAndView.addObject("data","succ");
            } catch (Exception e) {
                modelAndView.addObject("data",e.getMessage());
                e.printStackTrace();
            }
        }
        return modelAndView;
    }

}