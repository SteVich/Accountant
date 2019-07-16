package com.example.biblio.service;

import com.example.biblio.model.Role;
import com.example.biblio.model.RoleName;
import com.example.biblio.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> findByName(RoleName roleName){
        return roleRepository.findByName(roleName);
    }
}