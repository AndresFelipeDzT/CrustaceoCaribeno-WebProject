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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.example.demo.dto.ClienteDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = ClienteApiController.class)
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ClienteApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClienteService clienteService;

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
                        "clave",
                        true)));

        ResultActions response = mockMvc.perform(get("/api/clientes"));
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idCliente").value(1))
                .andExpect(jsonPath("$[0].nombre").value("Ana"));
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
                        "clave",
                        true));

        ResultActions response = mockMvc.perform(get("/api/clientes/2"));
        response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCliente").value(2))
                .andExpect(jsonPath("$.correo").value("luis@example.com"));
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
