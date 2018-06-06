package com.fafc.bet4fun.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.fafc.bet4fun.entities.Role;

public interface RoleRepository extends CrudRepository<Role, UUID> {
    public List<Role> findByRoleNameIn(List<String> roles);
}
