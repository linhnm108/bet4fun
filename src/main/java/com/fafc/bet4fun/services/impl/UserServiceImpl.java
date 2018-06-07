package com.fafc.bet4fun.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Client;
import com.fafc.bet4fun.repositories.ClientRepository;
import com.fafc.bet4fun.repositories.RoleRepository;
import com.fafc.bet4fun.services.UserService;
import com.fafc.bet4fun.view_models.UserViewModel;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Client findByClientName(String username) {
        return this.clientRepository.findByClientName(username);
    }

    @Override
    public Client register(UserViewModel userViewModel) {
        Client client = new Client();
        client.setClientName(userViewModel.getUsername());
        client.setPassword(this.bCryptPasswordEncoder.encode(userViewModel.getPassword()));
        client.setBalance(0);
        client.setRoles(roleRepository.findByRoleNameIn(Arrays.asList(Constants.DEFAULT_ROLES)));

        return this.clientRepository.save(client);
    }

    @Override
    public List<Client> getAllUsers() {
        return (List<Client>) this.clientRepository.findAll();
    }

    @Override
    public void deleteUser(String uuid) {
        this.clientRepository.deleteById(UUID.fromString(uuid));
    }

    @Override
    public Client findByClientId(String uuid) {
        return this.clientRepository.findById(UUID.fromString(uuid)).get();
    }

    @Override
    public Client saveUser(Client user) {
        return this.clientRepository.save(user);
    }

}
