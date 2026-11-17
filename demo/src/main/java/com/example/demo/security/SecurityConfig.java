package com.example.demo.security;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.demo.entitys.Role;
import com.example.demo.service.ClienteService;
import com.example.demo.service.PedidoService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, ClienteService clienteService,
            PedidoService pedidoService) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(frame -> frame.disable()))
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/api/clientes/details").hasAuthority(Role.CLIENTE)
                        .requestMatchers("/api/operadores/details").hasAuthority(Role.OPERADOR)
                        .requestMatchers("/api/administradores/details").hasAuthority(Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.POST, "/api/administradores/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/operadores/login").permitAll()
                        .requestMatchers("/api/administradores", "/api/administradores/**",
                                "/api/operadores", "/api/operadores/**")
                        .hasAuthority(Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.GET, "/api/pedidos")
                        .hasAnyAuthority(Role.CLIENTE, Role.OPERADOR, Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.GET, "/api/pedidos/{idPedido}/**")
                        .access((auth, context) -> lecturaPedido(auth, context, clienteService, pedidoService))
                        .requestMatchers(HttpMethod.PUT, "/api/pedidos/**")
                        .hasAnyAuthority(Role.OPERADOR, Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.DELETE, "/api/pedidos/**").hasAuthority(Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.POST, "/api/adicionales").hasAuthority(Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.PUT, "/api/adicionales/**").hasAuthority(Role.ADMINISTRADOR)
                        .requestMatchers(HttpMethod.DELETE, "/api/adicionales/**").hasAuthority(Role.ADMINISTRADOR)
                        .requestMatchers("/api/carritos/{idCliente}/**")
                        .access((auth, context) -> accesoCliente(auth, context, clienteService))
                        .requestMatchers(HttpMethod.GET, "/api/clientes")
                        .hasAnyAuthority(Role.ADMINISTRADOR, Role.OPERADOR)
                        .requestMatchers(HttpMethod.GET, "/api/clientes/{idCliente}")
                        .access((auth, context) -> accesoPerfil(auth, context, clienteService, true))
                        .requestMatchers(HttpMethod.PUT, "/api/clientes/{idCliente}")
                        .access((auth, context) -> accesoPerfil(auth, context, clienteService, false))
                        .requestMatchers(HttpMethod.DELETE, "/api/clientes/{idCliente}")
                        .access((auth, context) -> accesoPerfil(auth, context, clienteService, false))
                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtAuthEntryPoint));

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private AuthorizationDecision accesoPerfil(Supplier<Authentication> proveedor,
            RequestAuthorizationContext context, ClienteService clienteService, boolean lectura) {
        Authentication autenticacion = proveedor.get();
        if (autenticacion != null && autenticacion.getAuthorities().stream().anyMatch(autoridad ->
                Role.ADMINISTRADOR.equals(autoridad.getAuthority())
                || (lectura && Role.OPERADOR.equals(autoridad.getAuthority())))) {
            return new AuthorizationDecision(true);
        }
        return accesoCliente(proveedor, context, clienteService);
    }

    private AuthorizationDecision accesoCliente(Supplier<Authentication> proveedor,
            RequestAuthorizationContext context, ClienteService clienteService) {
        Authentication autenticacion = proveedor.get();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken
                || autenticacion.getAuthorities().stream().noneMatch(a -> Role.CLIENTE.equals(a.getAuthority()))) {
            return new AuthorizationDecision(false);
        }
        String idSolicitado = context.getVariables().get("idCliente");
        boolean titular = clienteService.obtenerClienteDTOPorCorreo(autenticacion.getName())
                .filter(cliente -> cliente.getIdCliente().toString().equals(idSolicitado))
                .isPresent();
        return new AuthorizationDecision(titular);
    }

    private AuthorizationDecision lecturaPedido(Supplier<Authentication> proveedor,
            RequestAuthorizationContext context, ClienteService clienteService, PedidoService pedidoService) {
        Authentication autenticacion = proveedor.get();
        if (autenticacion == null || autenticacion instanceof AnonymousAuthenticationToken
                || !autenticacion.isAuthenticated()) {
            return new AuthorizationDecision(false);
        }
        if (autenticacion.getAuthorities().stream().anyMatch(a ->
                Role.ADMINISTRADOR.equals(a.getAuthority()) || Role.OPERADOR.equals(a.getAuthority()))) {
            return new AuthorizationDecision(true);
        }
        if (autenticacion.getAuthorities().stream().noneMatch(a -> Role.CLIENTE.equals(a.getAuthority()))) {
            return new AuthorizationDecision(false);
        }
        Long idPedido;
        try {
            idPedido = Long.valueOf(context.getVariables().get("idPedido"));
        } catch (NumberFormatException ex) {
            return new AuthorizationDecision(false);
        }
        boolean titular = clienteService.obtenerClienteDTOPorCorreo(autenticacion.getName())
                .filter(cliente -> pedidoService.perteneceACliente(idPedido, cliente.getIdCliente()))
                .isPresent();
        return new AuthorizationDecision(titular);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
