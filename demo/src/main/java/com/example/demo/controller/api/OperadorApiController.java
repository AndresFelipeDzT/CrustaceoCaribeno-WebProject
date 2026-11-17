package com.example.demo.controller.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.OperadorDTO;
import com.example.demo.entitys.Operador;
import com.example.demo.entitys.UserEntity;
import com.example.demo.repository.UserEntityRepository;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.OperadorService;

/**
 * Controlador REST para las operaciones de operador.
 */
@RestController
@RequestMapping("/api/operadores")
@CrossOrigin(origins = "http://localhost:4200")
public class OperadorApiController {

    @Autowired
    private OperadorService operadorService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;

    @GetMapping
    public List<OperadorDTO> listar() {
        return operadorService.obtenerTodosLosOperadores().stream()
                .map(OperadorDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public OperadorDTO obtener(@PathVariable Long id) {
        return new OperadorDTO(operadorService.obtenerOperadorPorId(id));
    }

    @PostMapping
    public void crear(@RequestBody Operador operador) {
        String username = operador == null ? null : operador.getUsuario();
        if (username != null && !username.isBlank() && userEntityRepository.existsByUsername(username.trim())) {
            throw new IllegalArgumentException("Este usuario ya existe.");
        }

        operadorService.guardarOperador(operador);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Operador operador) {
        operadorService.actualizarOperador(id, operador);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        operadorService.eliminarOperador(id);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Operador operador) {
        if (operador == null) {
            throw new IllegalArgumentException("Las credenciales son obligatorias.");
        }
        // return new OperadorDTO(operadorService.login(operador.getUsuario(), operador.getContrasena()));

        String usuario = operador.getUsuario();
        String contrasena = operador.getContrasena();
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu usuario.");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu contraseña.");
        }

        Operador perfil = operadorService.buscarPorUsuario(usuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario o la contraseña no son correctos."));
        UserEntity cuenta = perfil.getUserEntity();
        if (cuenta == null) {
            throw new IllegalArgumentException("El usuario o la contraseña no son correctos.");
        }

        Authentication autenticacion;
        try {
            autenticacion = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(cuenta.getUsername(), contrasena));
        } catch (BadCredentialsException ex) {
            throw new IllegalArgumentException("El usuario o la contraseña no son correctos.");
        }
        SecurityContextHolder.getContext().setAuthentication(autenticacion);
        String token = jwtGenerator.generateToken(autenticacion);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @GetMapping("/details")
    public ResponseEntity<OperadorDTO> obtenerDetalles() {
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return operadorService.buscarPorUsuario(usuario)
                .map(OperadorDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
