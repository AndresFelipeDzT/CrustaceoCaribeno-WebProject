package com.example.demo.controller.api;

import java.util.List;
import java.time.LocalDate;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.demo.service.EstadisticasVentasService;
import com.example.demo.dto.ResumenVentasDTO;

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

import com.example.demo.dto.AdministradorDTO;
import com.example.demo.entitys.Administrador;
import com.example.demo.entitys.UserEntity;
import com.example.demo.repository.UserEntityRepository;
import com.example.demo.security.JWTGenerator;
import com.example.demo.service.AdministradorService;

/**
 * Controlador REST para las operaciones de administrador.
 */
@RestController
@RequestMapping("/api/administradores")
@CrossOrigin(origins = "http://localhost:4200")
public class AdministradorApiController {
    
    @Autowired
    private AdministradorService administradorService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTGenerator jwtGenerator;


    @Autowired
    private EstadisticasVentasService estadisticasVentasService;

    @GetMapping("/estadisticas/ventas")
    public ResumenVentasDTO ventas(@RequestParam(required = false) LocalDate desde,
            @RequestParam(required = false) LocalDate hasta) {
        return estadisticasVentasService.consultar(desde, hasta);
    }

    @GetMapping
    public List<AdministradorDTO> listar() {
        return administradorService.obtenerTodosLosAdministradores().stream()
                .map(AdministradorDTO::new)
                .toList();
    }

    @GetMapping("/{id}")
    public AdministradorDTO obtener(@PathVariable Long id) {
        return new AdministradorDTO(administradorService.obtenerAdministradorPorId(id));
    }

    @PostMapping
    public void crear(@RequestBody Administrador administrador) {
        String username = administrador == null ? null : administrador.getUsuario();
        if (username != null && !username.isBlank() && userEntityRepository.existsByUsername(username.trim())) {
            throw new IllegalArgumentException("Este usuario ya existe.");
        }

        administradorService.guardarAdministrador(administrador);
    }

    @PutMapping("/{id}")
    public void actualizar(@PathVariable Long id, @RequestBody Administrador administrador) {
        administradorService.actualizarAdministrador(id, administrador);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        administradorService.eliminarAdministrador(id);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Administrador administrador) {
        if (administrador == null) {
            throw new IllegalArgumentException("Las credenciales son obligatorias.");
        }
        // return new AdministradorDTO(
        //         administradorService.login(administrador.getUsuario(), administrador.getContrasena()));

        String usuario = administrador.getUsuario();
        String contrasena = administrador.getContrasena();
        if (usuario == null || usuario.trim().isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu usuario.");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("Por favor ingresa tu contraseña.");
        }

        Administrador perfil = administradorService.buscarPorUsuario(usuario)
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
    public ResponseEntity<AdministradorDTO> obtenerDetalles() {
        String usuario = SecurityContextHolder.getContext().getAuthentication().getName();
        return administradorService.buscarPorUsuario(usuario)
                .map(AdministradorDTO::new)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
