package com.fafc.bet4fun.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

import com.fafc.bet4fun.services.UserService;
import com.fafc.bet4fun.view_models.UserViewModel;

@Component
public class SignupValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserViewModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserViewModel user = (UserViewModel) target;
        if (this.userService.findByClientName(user.getUsername()) != null) {
            errors.rejectValue("username", "duplicate.signupForm.username");
        }
        if (!StringUtils.equals(user.getConfirmPassword(), user.getPassword())) {
            errors.rejectValue("confirmPassword", "diff.signupForm.confirmPassword");
        }
    }

}
