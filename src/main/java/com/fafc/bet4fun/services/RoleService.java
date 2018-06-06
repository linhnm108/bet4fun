package com.fafc.bet4fun.services;

import java.util.List;

import com.fafc.bet4fun.entities.Role;

public interface RoleService {
    public List<Role> getAllRoles();
    public List<Role> getDefaultRoles();
    public Role saveRole(String roleName);
    public void deleteRole(String uuid);
}
