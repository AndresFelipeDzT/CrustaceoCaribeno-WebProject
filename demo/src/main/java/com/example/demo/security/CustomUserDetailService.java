package com.example.demo.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entitys.Role;
import com.example.demo.entitys.UserEntity;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserEntityRepository;

@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailService(UserEntityRepository userEntityRepository, RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.userEntityRepository = userEntityRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userEntityRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();

        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public UserEntity prepararUserEntity(UserEntity existingUser, String username, String password, String roleName) {
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
        return user;
    }
}
