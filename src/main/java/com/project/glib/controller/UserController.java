package com.project.glib.controller;

//import com.project.glib.dao.implementations.SecurityDaoImplementation;
import com.project.glib.dao.implementations.UsersDaoImplementation;
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
    public List<User> users() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", usersDao.getList());
        modelAndView.setViewName("patrons");
        return usersDao.authUsers();
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        System.out.println(" There ");
        System.out.println(logout);
        System.out.println(error);
        if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");

        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(Model model, @RequestParam(value = "login") String login,@RequestParam(value = "password") String password , String logout) {
        System.out.println("looooogin  " + login);
       User user = usersDao.findLogin(login);
       if (user.getPassword().equals(password)){
           return "redirect:/booking/books";
       }else{
           model.addAttribute("error","Invalid Password");
           return "redirect:/booking/books";
       }
    }


}
