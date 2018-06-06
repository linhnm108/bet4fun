package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.view_models.UserViewModel;

public interface UserService {
    public Client findByClientName(String username);
    public Client register(UserViewModel userViewModel);
    public List<Client> getAllUsers();
    public void deleteUser(String uuid);
}
