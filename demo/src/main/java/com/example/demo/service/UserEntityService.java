package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity saveUserEntity(UserEntity existingUser, String username, String password, String roleName) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario es obligatorio.");
        }
        boolean nuevaContrasena = password != null && !password.isBlank();
        if (existingUser == null && !nuevaContrasena) {
            throw new IllegalArgumentException("La contraseña es obligatoria.");
        }

        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
        UserEntity user = existingUser != null ? existingUser : new UserEntity();
        user.setUsername(username.trim());
        if (nuevaContrasena) {
            user.setPassword(passwordEncoder.encode(password));
        }
        user.setRoles(new ArrayList<>(List.of(role)));
        return userEntityRepository.save(user);
    }

    public boolean coincideContrasena(UserEntity user, String password) {
        return user != null && password != null && passwordEncoder.matches(password, user.getPassword());
    }
}
