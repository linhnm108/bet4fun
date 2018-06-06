package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.UserService;
import com.fafc.bet4fun.validators.LoginValidator;
import com.fafc.bet4fun.validators.SignupValidator;
import com.fafc.bet4fun.view_models.UserViewModel;

@Controller
public class UserController {

    public static final String DELETED_USER = "deleted_user";

    @Autowired
    SignupValidator signupValidator;


    @Autowired
    LoginValidator loginValidator;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (error != null) {
            model.addAttribute("error", "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully.");
        }
        return "login";
    }

    @RequestMapping(value="/signup", method = RequestMethod.GET)
    public String signup(@Valid @ModelAttribute("userViewModel") UserViewModel userViewModel) {
        return "signup";
    }

    @RequestMapping(value="/signup", method = RequestMethod.POST)
    public String signup(@Valid @ModelAttribute("userViewModel") UserViewModel userViewModel, BindingResult errors) {
        this.signupValidator.validate(userViewModel, errors);
        if (errors.hasErrors()) {
            return "signup";
        }

        // Save user to database.
        this.userService.register(userViewModel);

        //Auto login
        this.authenticationService.login(userViewModel);

        return "home";
    }

    @RequestMapping(value="/users", method = RequestMethod.GET)
    public String getAllUsers(Model model, HttpServletRequest request) {
        List<Client> users = this.userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute(DELETED_USER, request.getParameter(DELETED_USER));
        return "users";
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name="uuid") String uuid, RedirectAttributes redir) {
        this.userService.deleteUser(uuid);
        redir.addAttribute(DELETED_USER, uuid);
        return "redirect:/users";
    }
}
