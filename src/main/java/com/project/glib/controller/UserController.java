package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;

import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.model.Document;
import com.project.glib.model.User;
import com.project.glib.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
//@RestController
public class UserController {
    private final UsersDaoImplementation usersDao;
    //    private final SecurityDaoImplementation securityDao;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UsersDaoImplementation usersDao,
//                          SecurityDaoImplementation securityDao,
                          UserValidator userValidator) {
        this.usersDao = usersDao;
//        this.securityDao = securityDao;
        this.userValidator = userValidator;
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

        usersDao.add(userForm);
//        securityDao.autoLogin(userForm.getLogin(), userForm.getPasswordConfirm());

        return "redirect:/login";
    }

    @RequestMapping(value = "/users")
    public ModelAndView users() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", usersDao.getList());
        modelAndView.setViewName("patrons");
        return modelAndView;
    }

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
        User user = usersDao.findLogin(login);
        ModelAndView modelAndView = new ModelAndView();
        if (user != null) {
            if (user.getPassword().equals(password)) {
                String role = user.getRole().getName();
                if (role.equals(User.LIBRARIAN)) {
                    System.out.println(" LIIIIIIIIIIIIIIIIIIIIIIIB ");
                    modelAndView.setViewName("librarian");
                    return modelAndView;
                } else if (role.equals(User.FACULTY)) {
                    System.out.println("FAAAAAAAAAAAAAAAC");
                    modelAndView.setViewName("faculty");
                    return modelAndView;
                } else if (role.equals(User.STUDENT)) {
                    System.out.println("STUUUUUUUUUUUUUU");
                    modelAndView.setViewName("student");
                    return modelAndView;
                } else {
                    modelAndView.addObject("error", "Invalid Programmer");
                    modelAndView.setViewName("login");
                    return modelAndView;
                }
            } else {
                modelAndView.addObject("error", "Invalid Password");
                modelAndView.setViewName("login");
                return modelAndView;
            }
        } else {
            modelAndView.addObject("error", "Invalid Login");
            modelAndView.setViewName("login");
            return modelAndView;
        }
    }

    @RequestMapping(value = "/librarian", method = RequestMethod.GET)
    public ModelAndView librarianDashboard(Model model, String logout) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("librarian");
        return modelAndView;
    }


}
