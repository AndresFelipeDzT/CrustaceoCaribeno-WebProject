package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entitys.UserEntity;
import com.example.demo.repository.UserEntityRepository;
import com.example.demo.security.CustomUserDetailService;

@Service
public class UserEntityService {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserEntity saveUserEntity(UserEntity existingUser, String username, String password, String roleName) {
        UserEntity user = customUserDetailService.prepararUserEntity(existingUser, username, password, roleName);
        return userEntityRepository.save(user);
    }

    public boolean coincideContrasena(UserEntity user, String password) {
        return user != null && password != null && passwordEncoder.matches(password, user.getPassword());
    }
}
