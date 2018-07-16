package com.fafc.bet4fun.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.entities.Role;
import com.fafc.bet4fun.services.AuthenticationService;
import com.fafc.bet4fun.services.RoleService;
import com.fafc.bet4fun.services.UserService;
import com.fafc.bet4fun.validators.LoginValidator;
import com.fafc.bet4fun.validators.SignupValidator;
import com.fafc.bet4fun.view_models.UserViewModel;

@Controller
public class UserController {

    @Autowired
    SignupValidator signupValidator;

    @Autowired
    LoginValidator loginValidator;

    @Autowired
    UserService userService;

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    RoleService roleService;

    @Autowired
    BCryptPasswordEncoder encoder;

    @RequestMapping(value="/login", method = RequestMethod.GET)
    public String login(Model model, String error, String logout) {
        if (this.authenticationService.getLoggedInUser() != null) {
            return "redirect:/"; 
        }
        if (error != null) {
            model.addAttribute(Constants.ERROR, "Your username and password is invalid.");
        }

        if (logout != null) {
            model.addAttribute(Constants.MESSAGE, "You have been logged out successfully.");
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
        model.addAttribute(Constants.MESSAGE, request.getParameter(Constants.MESSAGE));
        return "users";
    }

    @RequestMapping(value = "/user/delete", method = RequestMethod.POST)
    public String delete(@RequestParam(name="uuid") String uuid, RedirectAttributes redir) {
        this.userService.deleteUser(uuid);
        redir.addAttribute(Constants.MESSAGE, "User " + uuid + " deleted successfully!");
        return "redirect:/users";
    }

    @RequestMapping(value="/user/roles/assign", method = RequestMethod.GET)
    public String getAllUsers(Model model, @RequestParam(name="uuid") String uuid) {
        Client user = this.userService.findByClientId(uuid);
        model.addAttribute("user", user);
        return "user-roles-assign";
    }

    @RequestMapping(value="/user/roles/assign", method = RequestMethod.POST)
    public String assignRole(@Valid @ModelAttribute("user") Client user, BindingResult errors, RedirectAttributes redir) {
        List<Role> roles = this.roleService.getRoleByNames(user.getStrRoles());
        user = this.userService.findByClientId(user.getClientId().toString());
        user.setRoles(roles);
        this.userService.saveUser(user);

        redir.addAttribute(Constants.MESSAGE, "User " + user.getClientName() + " has been assigned new roles.");
        return "redirect:/users";
    }

    @RequestMapping(value="/user/password/change", method = RequestMethod.GET)
    public String changePassword() {
        return "password-change";
    }

    @RequestMapping(value="/user/password/change", method = RequestMethod.POST)
    public String changePassword(Model model, HttpServletRequest request) {

        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        Client currentUser = this.authenticationService.getLoggedInUser();
        if (StringUtils.isEmpty(newPassword)) {
            model.addAttribute(Constants.ERROR, "New password is required.");
        } else if (!this.encoder.matches(oldPassword, currentUser.getPassword())) {
            model.addAttribute(Constants.ERROR, "Your old password is incorrect.");
        } else if (!StringUtils.equals(newPassword, confirmPassword)) {
            model.addAttribute(Constants.ERROR, "Your new password and confirmation password do not match.");
        } else {
            currentUser.setPassword(this.encoder.encode(newPassword));
            this.userService.saveUser(currentUser);
    
            model.addAttribute(Constants.MESSAGE, "Your password changed successfully.");
        }

        return "password-change";
    }
}
