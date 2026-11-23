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
import com.example.demo.repository.HistorialEstadoPedidoRepository;
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
    private CarritoService carritoService;

    @Autowired
    private ClienteFakeRepository clienteRepository;

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private HistorialEstadoPedidoRepository historialEstadoPedidoRepository;

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
    void confirmarPedido_descuentaInventario_yCancelarLoRestauraConHistorial() {
        Producto producto = productoRepository.save(
                new Producto("Camarones de prueba", 18000.0, "Plato de prueba", null));
        int stockInicial = producto.getStock();

        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);
        PedidoDTO pedidoCreado = carritoService.confirmarPedido(cliente.getIdCliente(), null);

        assertThat(productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock())
                .isEqualTo(stockInicial - 1);
        assertThat(pedidoService.obtenerHistorialEstado(pedidoCreado.getIdPedido()))
                .extracting(historial -> historial.estado())
                .containsExactly("Pendiente");

        pedidoService.actualizarEstadoPedido(
                pedidoCreado.getIdPedido(),
                Pedido.builder().estado("En preparación").build());
        pedidoService.actualizarEstadoPedido(
                pedidoCreado.getIdPedido(),
                Pedido.builder().estado("Cancelado").build());

        assertThat(productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock())
                .isEqualTo(stockInicial);
        assertThat(pedidoService.obtenerHistorialEstado(pedidoCreado.getIdPedido()))
                .extracting(historial -> historial.estado())
                .containsExactly("Pendiente", "En preparación", "Cancelado");
    }

    @Test
    void confirmarPedido_rechazaInventarioInsuficiente_yConservaElCarrito() {
        Producto producto = productoRepository.save(
                new Producto("Plato agotado de prueba", 18000.0, "Plato de prueba", null));
        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);
        producto.setStock(0);
        productoRepository.save(producto);

        assertThatThrownBy(() -> carritoService.confirmarPedido(cliente.getIdCliente(), null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Inventario insuficiente");
        assertThat(carritoService.obtenerItemsDTO(cliente.getIdCliente())).hasSize(1);
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

    @Autowired
    private EstadisticasVentasService estadisticas;

    @Test
    void repetirRespetaElStockElCarritoExistenteYElPrecioActual() {
        Producto producto = productoRepository.save(new Producto("Cazuela", 20000, "Prueba", null));
        Pedido anterior = guardarPedido("Entregado");
        anterior.getItems().add(itemPedidoRepository.save(new ItemPedido(3, anterior, producto)));
        producto.setStock(2);
        producto.setPrecio(24000);
        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);

        var resultado = carritoService.repetirPedido(cliente.getIdCliente(), anterior.getIdPedido());

        assertThat(resultado.getUnidadesAgregadas()).isEqualTo(1);
        assertThat(resultado.getAvisos()).hasSize(1);
        assertThat(carritoService.obtenerCarritoDTO(cliente.getIdCliente()).getSubtotal()).isEqualTo(48000);
        assertThat(producto.getStock()).isEqualTo(2);
    }

    @Test
    void repetirRechazaPedidoAjenoOPendiente() {
        Pedido pedido = guardarPedido("Pendiente");
        assertThatThrownBy(() -> carritoService.repetirPedido(cliente.getIdCliente(), pedido.getIdPedido()))
                .hasMessageContaining("entregados");
        Cliente otro = clienteRepository.save(new Cliente("Otro", "Cliente", "otro@test.com",
                "3012345678", "Calle 2", "clave"));
        assertThatThrownBy(() -> carritoService.repetirPedido(otro.getIdCliente(), pedido.getIdPedido()))
                .hasMessageContaining("no pertenece");
    }

    @Test
    void ventasAgrupaUnidadesYConservaImportesAlCambiarElPrecio() {
        LocalDate fecha = LocalDate.of(2020, 2, 10);
        Producto producto = productoRepository.save(new Producto("Pulpo", 10000, "Prueba", null));
        Pedido pedido = guardarPedido("Entregado");
        pedido.setFechaEntrega(fecha);
        pedido.setDomicilio(5000);
        pedido.setDescuento(2000);
        pedido.getItems().add(itemPedidoRepository.save(new ItemPedido(2, pedido, producto)));
        producto.setPrecio(15000);
        var resumen = estadisticas.consultar(fecha, fecha);

        assertThat(resumen.getPedidosEntregados()).isEqualTo(1);
        assertThat(resumen.getTotalCobrado()).isEqualTo(23000);
        assertThat(resumen.getUnidadesVendidas()).isEqualTo(2);
        assertThat(resumen.getPlatos()).singleElement().satisfies(plato ->
                assertThat(plato.getValorPlatos()).isEqualTo(20000));
        assertThat(estadisticas.consultar(fecha.plusDays(1), fecha.plusDays(1)).getPlatos()).isEmpty();
        assertThat(estadisticas.consultar(null, null).getPlatos())
                .anyMatch(plato -> producto.getIdProducto().equals(plato.getIdProducto()) && plato.getUnidades() == 2);
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
