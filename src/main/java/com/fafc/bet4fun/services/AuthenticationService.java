package com.fafc.bet4fun.services;

import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.view_models.UserViewModel;

public interface AuthenticationService {
    public void login(UserViewModel userViewModel);
    public void logout();
    public Client getLoggedInUser();
}
