package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Producto;
import com.example.demo.errors.PedidoNotFoundException;
import com.example.demo.repository.ClienteFakeRepository;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoFakeRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:pedidoIntegration;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class PedidoServiceIntegrationTest {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private ClienteFakeRepository clienteRepository;

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = clienteRepository.save(new Cliente(
                "Ana",
                "Ríos",
                "ana.integration@example.com",
                "3001234567",
                "Calle 1 # 10-10",
                "clave"));
    }

    @Test
    void obtenerPedidoDTOPorId_conItems_retornaDatosYTotal() {
        // Arrange
        Producto producto = productoRepository.save(
                new Producto("Cazuela Caribeña", 25000.0, "Mariscos", null));
        Pedido pedido = guardarPedido("En preparación");
        ItemPedido item = itemPedidoRepository.save(new ItemPedido(2, pedido, producto));
        pedido.getItems().add(item);

        // Act
        PedidoDTO resultado = pedidoService.obtenerPedidoDTOPorId(pedido.getIdPedido());

        // Assert
        assertThat(resultado.getIdPedido()).isEqualTo(pedido.getIdPedido());
        assertThat(resultado.getIdCliente()).isEqualTo(cliente.getIdCliente());
        assertThat(resultado.getEstado()).isEqualTo("En preparación");
        assertThat(resultado.getTotal()).isEqualTo(50000.0);
    }

    @Test
    void obtenerPedidosDTO_soloActivos_excluyePedidosEntregadosYCancelados() {
        // Arrange
        Pedido activo = guardarPedido("En camino");
        Pedido entregado = guardarPedido("Entregado");
        Pedido cancelado = guardarPedido("Cancelado");

        // Act
        List<PedidoDTO> resultado = pedidoService.obtenerPedidosDTO(true, cliente.getIdCliente());

        // Assert
        assertThat(resultado).extracting(PedidoDTO::getIdPedido)
                .containsExactly(activo.getIdPedido())
                .doesNotContain(entregado.getIdPedido(), cancelado.getIdPedido());
    }

    @Test
    void actualizarEstadoPedido_conEstadoValido_persisteElNuevoEstado() {
        // Arrange
        Pedido pedido = guardarPedido("Pendiente");
        Pedido datosActualizacion = Pedido.builder().estado(" Entregado ").build();

        // Act
        pedidoService.actualizarEstadoPedido(pedido.getIdPedido(), datosActualizacion);

        // Assert
        assertThat(pedidoRepository.findById(pedido.getIdPedido()))
                .isPresent()
                .get()
                .extracting(Pedido::getEstado)
                .isEqualTo("Entregado");
    }

    @Test
    void obtenerPedidoPorId_conIdInexistente_lanzaExcepcion() {
        // Arrange
        long idInexistente = 999999L;

        // Act y Assert
        assertThatThrownBy(() -> pedidoService.obtenerPedidoPorId(idInexistente))
                .isInstanceOf(PedidoNotFoundException.class)
                .hasMessageContaining(String.valueOf(idInexistente));
    }

    private Pedido guardarPedido(String estado) {
        return pedidoRepository.save(Pedido.builder()
                .fechaCreacion(LocalDate.of(2026, 1, 15))
                .estado(estado)
                .cliente(cliente)
                .items(new ArrayList<>())
                .build());
    }
}
