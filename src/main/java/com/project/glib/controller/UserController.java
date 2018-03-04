package com.project.glib.controller;

import com.project.glib.dao.implementations.SecurityDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
import com.project.glib.dao.interfaces.RoleRepository;
import com.project.glib.model.Role;
import com.project.glib.model.User;
import com.project.glib.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {
    private final UsersDaoImplementation usersDao;
    private final SecurityDaoImplementation securityDao;
    private final UserValidator userValidator;

    @Autowired
    public UserController(UsersDaoImplementation usersDao,
                          SecurityDaoImplementation securityDao,
                          UserValidator userValidator) {
        this.usersDao = usersDao;
        this.securityDao = securityDao;
        this.userValidator = userValidator;
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registration(Model model) {
        model.addAttribute("userForm", new User());

        return "register";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String registration(User userForm, BindingResult bindingResult, Model model) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "register";
        }

        usersDao.add(userForm);


        securityDao.autoLogin(userForm.getLogin(), userForm.getPasswordConfirm());

        return "redirect:/login";
    }

    @RequestMapping(value = "/users")
    public ModelAndView users(){
        ModelAndView modelAndView=new ModelAndView();
        modelAndView.addObject("allUsers", usersDao.getList());
        modelAndView.setViewName("patrons");
        return modelAndView;
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

}
