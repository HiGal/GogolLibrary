package com.project.glib.controller;

import com.project.glib.dao.implementations.AudioVideoDaoImplementation;
import com.project.glib.dao.implementations.BookDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.dao.interfaces.BookRepository;
import com.project.glib.model.User;
import com.project.glib.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class LoginController {
    //    private final SecurityDaoImplementation securityDao;
    private final UsersDaoImplementation usersDao;
    private final UserValidator userValidator;
    private final BookDaoImplementation bookDao;
    private final AudioVideoDaoImplementation avDao;

    @Autowired
    public LoginController(UsersDaoImplementation usersDao,
//                          SecurityDaoImplementation securityDao,
                           UserValidator userValidator, BookRepository bookRepository, BookDaoImplementation bookDao, AudioVideoDaoImplementation avDao) {
        this.usersDao = usersDao;
//        this.securityDao = securityDao;
        this.userValidator = userValidator;
        this.bookDao = bookDao;
        this.avDao = avDao;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());
        return "register";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(@RequestBody User userForm, BindingResult bindingResult) {

        userValidator.validate(userForm, bindingResult);

        //TODO check this validation
//        if (bindingResult.hasErrors()) {
//            return "register";
//        }
        try {
            userValidator.validate(userForm, bindingResult);
            usersDao.add(userForm);
        }catch (Exception e){
            return e.getMessage();
        }


//        securityDao.autoLogin(userForm.getLogin(), userForm.getPasswordConfirm());

        return "redirect:/login";
    }

//    @RequestMapping(value = "/users")
//    public ModelAndView users() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("allUsers", usersDao.getList());
//        modelAndView.setViewName("patrons");
//        return modelAndView;
//    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ModelAndView login(Model model, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", "");
        modelAndView.setViewName("login");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(Model model, @RequestParam(value = "login") String login,
                              @RequestParam(value = "password") String password, String logout) {
        User user = usersDao.findByLogin(login);
        ModelAndView modelAndView = new ModelAndView();
        if (user != null) {
            if (user.getPassword().equals(password)) {
                String role = user.getRole();
                switch (role) {
                    case User.LIBRARIAN:
                        modelAndView.addObject("user",user);
                        modelAndView.setViewName("librarian");
                        break;
                    case User.INSTRUCTOR:
                    case User.TA:
                    case User.PROFESSOR:
                        modelAndView.addObject("user",user);
                        modelAndView.setViewName("faculty");
                        break;
                    case User.STUDENT:
                        modelAndView.addObject("user",user);
                        modelAndView.setViewName("student");
                        break;
                    default:
                        modelAndView.addObject("error", "Invalid Programmer");
                        modelAndView.setViewName("login");
                        break;
                }
            } else {
                modelAndView.addObject("error", "Invalid Password");
                modelAndView.setViewName("login");
            }
        } else {
            modelAndView.addObject("error", "Invalid Login");
            modelAndView.setViewName("login");
        }
        return modelAndView;
    }

}
