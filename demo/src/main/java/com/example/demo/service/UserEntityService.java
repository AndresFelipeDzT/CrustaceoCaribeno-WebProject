package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entitys.Role;
import com.example.demo.entitys.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserEntityRepository;

@Service
public class UserEntityService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Transactional
    public UserEntity saveUserEntity(UserEntity existingUser, String username, String password, String roleName) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
        UserEntity user = existingUser != null ? existingUser : new UserEntity();
        user.setUsername(username.trim());
        user.setPassword(password);
        user.setRoles(new ArrayList<>(List.of(role)));
        return userEntityRepository.save(user);
    }
}
