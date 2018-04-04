package com.project.glib.controller;

import com.project.glib.model.Messages;
import com.project.glib.model.User;
import com.project.glib.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RestController
@SessionAttributes("user")
public class LoginController {
    private final UserService userService;
    private final BookService bookService;
    private final JournalService journalService;
    private final AudioVideoService audioVideoService;
    private final MessageService messageService;

    @Autowired
    public LoginController(UserService userService, BookService bookService, JournalService journalService, AudioVideoService audioVideoService, MessageService messageService) {
        this.userService = userService;
        this.bookService = bookService;
        this.journalService = journalService;
        this.audioVideoService = audioVideoService;
        this.messageService = messageService;
    }

//    @ModelAttribute("user")
//    public User setUpUser(){
//        return new User();
//    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login() {
        return new ModelAndView("login");
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(User form, HttpServletRequest request, SessionStatus status) {
        ModelAndView model = new ModelAndView();
        try {
            User user = userService.findByLogin(form.getLogin());

            String role = user.getRole();

            if (user.getPassword().equals(form.getPassword())) {
                model.addObject("info", user);
                model.addObject("listMessages", messageService.getMessages(user.getLogin()));
                request.getSession().setAttribute("user", user);
                if (role.equals(User.LIBRARIAN)) {
                    model.setViewName("librarian");
                } else if (Arrays.asList(User.PATRONS).contains(role)) {
                    model.setViewName("patron");
                } else {
                    throw new Exception("WRONG ROLE");
                }

                return model;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public ModelAndView registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("register");
        return modelAndView;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public @ResponseBody User regForm(@RequestBody User user) {
        try {
            System.out.println();
            System.out.println();
            System.out.println();
            System.out.println(user);
            System.out.println();
            System.out.println();
            System.out.println();
            userService.update(user);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        System.out.println();
        System.out.println();
        System.out.println(user);
        System.out.println();
        System.out.println();
        System.out.println();
        return user;
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView mainPage(@ModelAttribute("user") User user) {
        ModelAndView modelAndView = new ModelAndView();

        try {
            User user1 = userService.findByLogin(user.getLogin());
            modelAndView.addObject("info", user1);
            if (user1.getRole().equals(User.LIBRARIAN)) {
                modelAndView.setViewName("librarian");
            } else {
                modelAndView.setViewName("patron");
                modelAndView.addObject("listMessages", messageService.getMessages(user.getLogin()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return modelAndView;
    }


    /*
         USER CONTROLLER
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", userService.getList());
        modelAndView.setViewName("patrons");
        return modelAndView;
    }

    /*
        BOOK CONTROLLER
     */
    @RequestMapping(value = "/librarian/books", method = RequestMethod.GET)
    public ModelAndView books() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allBooks", bookService.getList());
        modelAndView.setViewName("documents");
        return modelAndView;
    }

    //    @RequestMapping(value = "/edit/book")
//    public ModelAndView editBook(@ModelAttribute Book book){
//        ModelAndView modelAndView = new ModelAndView();
//        return  modelAndView;
//    }
//
//    @RequestMapping(value = "/add/book")
//    public ModelAndView addBook(@ModelAttribute Book book){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/order/book")
//    public ModelAndView orderBook(@ModelAttribute Book book){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
    /*
        JOURNAL CONTROLLER
     */
    @RequestMapping(value = "/librarian/journals", method = RequestMethod.GET)
    public ModelAndView journals() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allJournals", journalService.getList());
        modelAndView.setViewName("journals");
        return modelAndView;
    }

    //
//    @RequestMapping(value = "/edit/journal")
//    public ModelAndView editJournal(@ModelAttribute Journal journal){
//        ModelAndView modelAndView = new ModelAndView();
//        return  modelAndView;
//    }
//
//    @RequestMapping(value = "/add/journal")
//    public ModelAndView addJournal(@ModelAttribute Journal journal){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/order/journal")
//    public ModelAndView orderBook(@ModelAttribute Journal journal){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
    /*
        AV CONTROLLER
     */
    @RequestMapping(value = "/librarian/av", method = RequestMethod.GET)
    public ModelAndView av() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allAV", audioVideoService.getList());
        modelAndView.setViewName("av");
        return modelAndView;
    }
//    @RequestMapping(value = "/edit/AV")
//    public ModelAndView editAV(@ModelAttribute AudioVideo audioVideo){
//        ModelAndView modelAndView = new ModelAndView();
//        return  modelAndView;
//    }
//
//    @RequestMapping(value = "/add/AV")
//    public ModelAndView addJournal(@ModelAttribute AudioVideo audioVideo){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//    @RequestMapping(value = "/order/AV")
//    public ModelAndView orderBook(@ModelAttribute AudioVideo audioVideo){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    /*
//        CHECKOUT CONTROLLER
//     */
//    @RequestMapping(value = "/checkout", method = RequestMethod.GET)
//    public ModelAndView checkout(@RequestParam String login){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/checkout", method = RequestMethod.POST)
//    public ModelAndView checkout(@ModelAttribute Booking booking, Model model){
//        ModelAndView modelAndView = new ModelAndView();
//        return modelAndView;
//    }
//
//    @RequestMapping(value = "/checkout/all", method = RequestMethod.POST)
//    public String checkoutAllDocument(@RequestParam String login, Model model) {
//        try {
//
//            return "success";
//        }catch (Exception e){
//
//        }
//
//        return "unsuccess";
//    }

    @RequestMapping(value = "/librarian/taken_doc", method = RequestMethod.GET)
    public ModelAndView takenDoc() {
        ModelAndView modelAndView = new ModelAndView("taken_documents");
        return modelAndView;
    }


}