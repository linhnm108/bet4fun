package com.fafc.bet4fun.services.impl;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fafc.bet4fun.common.Constants;
import com.fafc.bet4fun.entities.Role;
import com.fafc.bet4fun.repositories.RoleRepository;
import com.fafc.bet4fun.services.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return (List<Role>) this.roleRepository.findAll();
    }

    @Override
    public List<Role> getDefaultRoles() {
        return this.roleRepository.findByRoleNameIn(Arrays.asList(Constants.DEFAULT_ROLES));
    }

    @Override
    public Role saveRole(String roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        return this.roleRepository.save(role);
    }

    @Override
    public void deleteRole(String uuid) {
        this.roleRepository.deleteById(UUID.fromString(uuid));
    }

}
