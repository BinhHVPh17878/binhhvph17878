package com.vubq.joyboystore.services.impl;

import com.vubq.joyboystore.entities.Role;
import com.vubq.joyboystore.enums.ERole;
import com.vubq.joyboystore.repositories.RoleRepository;
import com.vubq.joyboystore.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(ERole name) {
        return this.roleRepository.findByName(name);
    }

    @Override
    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }
}
