package com.example.demo.controller.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Role;
import com.example.demo.repository.UserEntityRepository;
import com.example.demo.security.CustomUserDetailService;
import com.example.demo.security.JWTGenerator;
import com.example.demo.security.JwtAuthEntryPoint;
import com.example.demo.security.SecurityConfig;
import com.example.demo.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ClienteApiController.class)
@Import({SecurityConfig.class, JwtAuthEntryPoint.class, JWTGenerator.class})
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ClienteApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JWTGenerator jwtGenerator;

    @MockBean
    private ClienteService clienteService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private CustomUserDetailService customUserDetailService;

    @Test
    public void clienteApiController_listar_listaDeClienteDTO() throws Exception {
        when(clienteService.obtenerTodosLosClientesDTO()).thenReturn(List.of(
                new ClienteDTO(
                        1L,
                        "Ana",
                        "Ríos",
                        "ana@example.com",
                        "3001234567",
                        "Calle 1",
                        true)));

        ResultActions response = mockMvc.perform(get("/api/clientes"));
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ana"))
                .andExpect(jsonPath("$[0].password").doesNotExist());
    }

    @Test
    public void clienteApiController_obtener_clienteDTO() throws Exception {
        when(clienteService.obtenerClienteDTOPorId(2L)).thenReturn(
                new ClienteDTO(
                        2L,
                        "Luis",
                        "Pérez",
                        "luis@example.com",
                        "3001234567",
                        "Calle 2",
                        true));

        ResultActions response = mockMvc.perform(get("/api/clientes/2"));
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(2))
                .andExpect(jsonPath("$.correo").value("luis@example.com"))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void clienteApiController_detalles_devuelvePerfilDelClienteAutenticado() throws Exception {
        String correo = "ana@example.com";
        when(clienteService.obtenerClienteDTOPorCorreo(correo)).thenReturn(Optional.of(
                new ClienteDTO(1L, "Ana", "Ríos", correo, "3001234567", "Calle 1", true)));

        mockMvc.perform(get("/api/clientes/details").header("Authorization", bearer(correo, Role.CLIENTE)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(1))
                .andExpect(jsonPath("$.correo").value(correo))
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    public void clienteApiController_detalles_exigeToken() throws Exception {
        mockMvc.perform(get("/api/clientes/details")).andExpect(status().isUnauthorized());
    }

    @Test
    public void clienteApiController_detalles_exigeRolCliente() throws Exception {
        mockMvc.perform(get("/api/clientes/details")
                        .header("Authorization", bearer("operador", Role.OPERADOR)))
                .andExpect(status().isForbidden());
    }

    private String bearer(String username, String role) {
        UserDetails usuario = User.withUsername(username).password("sin-uso").authorities(role).build();
        when(customUserDetailService.loadUserByUsername(username)).thenReturn(usuario);
        return "Bearer " + jwtGenerator.generateToken(
                new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities()));
    }

    @Test
    public void clienteApiController_crear_estado201() throws Exception {
        Cliente cliente = new Cliente(
                "Marta",
                "López",
                "marta@example.com",
                "3001234567",
                "Calle 3",
                "clave");
        when(clienteService.guardarCliente(any(Cliente.class))).thenReturn(cliente);

        ResultActions response = mockMvc.perform(post("/api/clientes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cliente)));
        response
                .andExpect(status().isCreated());
    }

    @Test
    public void clienteApiController_actualizar_estado200() throws Exception {
        Cliente cliente = new Cliente(
                "Carlos",
                "Gómez",
                "carlos@example.com",
                "3001234567",
                "Calle 4",
                "clave");
        doNothing().when(clienteService).actualizarCliente(eq(4L), any(Cliente.class));

        ResultActions response = mockMvc.perform(put("/api/clientes/4")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(cliente)));
        response
                .andExpect(status().isOk());
    }

    @Test
    public void clienteApiController_eliminar_sinContenido204() throws Exception {
        doNothing().when(clienteService).eliminarCliente(5L);

        ResultActions response = mockMvc.perform(delete("/api/clientes/5"));
        response
                .andExpect(status().isNoContent());
    }
}
