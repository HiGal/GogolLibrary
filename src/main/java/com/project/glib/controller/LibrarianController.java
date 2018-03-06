package com.project.glib.controller;

import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.DocumentPhysicalDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.model.Book;
import com.project.glib.model.DocumentPhysical;
import com.project.glib.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@RestController
public class LibrarianController{
    private final UsersDaoImplementation usersDao;
    private final BookDaoImplementation bookDao;
    private final DocumentPhysicalDaoImplementation physicalDaoImplementation;

    @Autowired
    public LibrarianController(UsersDaoImplementation usersDao, BookDaoImplementation bookDao, DocumentPhysicalDaoImplementation physicalDaoImplementation) {
        this.usersDao = usersDao;
        this.bookDao = bookDao;
        this.physicalDaoImplementation = physicalDaoImplementation;
    }


    @RequestMapping(value = "/librarian", method = RequestMethod.GET)
    public ModelAndView librarianDashboard(Model model, String login, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("user", usersDao.findLogin(login));
        modelAndView.setViewName("librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/librarian", method = RequestMethod.POST)
    public ModelAndView librarianDashboard(User user) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", " ");
        modelAndView.setViewName("librarian");
        return modelAndView;
    }

    @RequestMapping(value = "/librarian/confirm", method = RequestMethod.GET)
    public ModelAndView librarianConfirm(String login) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", usersDao.getList());
        modelAndView.setViewName("confirm");
        return modelAndView;
    }

    @RequestMapping(value = "/librarian/user/confirm", method = RequestMethod.POST)
//    public ModelAndView librarianConfirm(User user, String login) {
    public String librarianConfirm(@RequestBody User user) throws Exception {
        user.setAuth(true);
        try {
            usersDao.update(user);
            return "- successfully updated -";
        } catch (Exception e) {
            return "- failed! -";
        }
    }
    @RequestMapping(value = "librarian/add/book", method = RequestMethod.POST)
    public String addBook(@RequestBody Book book, @RequestParam(value = "shelf") String shelf,
                        @RequestParam(value = "canBooked") boolean flag,
                        @RequestParam(value = "is_reference") boolean ref) {
        if (!bookDao.isAlreadyExist(book)) {
            try {
                bookDao.add(book);
                for (int i = 0; i < book.getCount(); i++) {
                    DocumentPhysical document = new DocumentPhysical();
                    document.setShelf(shelf);
                    document.setIdDoc(book.getId());
                    document.setShelf(shelf);
                    document.setDocType("BOOK");
                    document.setCanBooked(flag);
                    document.setReference(ref);
                    physicalDaoImplementation.add(document);

                }
                return "Book successfully added";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            book.setCount(book.getCount() + 1);
            try {
                bookDao.update(book);
            } catch (Exception e) {
                e.printStackTrace();
            }
            DocumentPhysical document = new DocumentPhysical();;
            document.setShelf(shelf);
            document.setIdDoc(book.getId());
            document.setShelf(shelf);
            document.setDocType("BOOK");
            document.setCanBooked(flag);
            document.setReference(ref);
            physicalDaoImplementation.add(document);
            return "Add a copy";
        }
        return "";
    }

    @RequestMapping(value = "/librarian/user/delete", method = RequestMethod.GET)
    //   public ModelAndView librarianConfirm(Model model, String login) {
    public List<User> librarianDeleteUser(Model model, String login) {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allUsers", usersDao.getList());
//        modelAndView.setViewName("confirm");
        return usersDao.getListAuthUsers();
    }

    @RequestMapping(value = "/librarian/user/delete", method = RequestMethod.POST)
//    public ModelAndView librarianConfirm(User user, String login) {
    public String librarianDeleteUser(@RequestParam String login) throws Exception {
        try {
            usersDao.remove(usersDao.getIdByLogin(login));
            return "- successfully deleted -";
        }catch (Exception e){
            return "- failed! -";
        }
    }





//    @RequestMapping(value = "/librarian/user/info", method = RequestMethod.POST)
////    public ModelAndView librarianConfirm(User user, String login) {
//    public Pair<List<User>,List<Booking>> librarianGetInfo(@RequestParam User user) throws Exception {
//        user.setAuth(true);
//        try {
//            usersDao.update(user);
//            return "- successfully updated -";
//        }catch (Exception e){
//            return "- failed! -";
//        }
//    }

}
