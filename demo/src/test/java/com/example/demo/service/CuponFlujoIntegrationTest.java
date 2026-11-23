package com.example.demo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.CuponAplicacionDTO;
import com.example.demo.dto.CuponDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Producto;
import com.example.demo.repository.ClienteFakeRepository;
import com.example.demo.repository.CuponRepository;
import com.example.demo.repository.ProductoFakeRepository;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:cuponIntegration;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@Transactional
class CuponFlujoIntegrationTest {

    @Autowired
    private CarritoService carritoService;

    @Autowired
    private ClienteFakeRepository clienteRepository;

    @Autowired
    private ProductoFakeRepository productoRepository;

    @Autowired
    private CuponRepository cuponRepository;

    private Cliente cliente;
    private Producto producto;

    @BeforeEach
    void setUp() {
        cliente = clienteRepository.save(new Cliente(
                "Cliente", "Prueba", "cupon.integration@example.com", "3001234567",
                "Calle 1 # 10-10", "clave"));
        producto = productoRepository.save(new Producto("Plato de prueba", 25000, "Descripción", null));
    }

    @Test
    void confirmarPedidoEmiteUnCuponYElSiguientePedidoLoConsume() {
        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);

        PedidoDTO primerPedido = carritoService.confirmarPedido(cliente.getIdCliente(), null);
        CuponDTO primerCupon = carritoService.obtenerCuponDisponible(cliente.getIdCliente());

        assertThat(primerPedido.getTotal()).isEqualTo(30000);
        assertThat(primerCupon).isNotNull();
        assertThat(primerCupon.porcentaje()).isEqualTo(10);

        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);
        CuponAplicacionDTO cotizacion = carritoService.validarCupon(
                cliente.getIdCliente(), primerCupon.codigo());
        PedidoDTO segundoPedido = carritoService.confirmarPedido(
                cliente.getIdCliente(), primerCupon.codigo());

        assertThat(cotizacion.subtotal()).isEqualTo(25000);
        assertThat(cotizacion.descuento()).isEqualTo(2500);
        assertThat(cotizacion.total()).isEqualTo(27500);
        assertThat(segundoPedido.getCodigoCupon()).isEqualTo(primerCupon.codigo());
        assertThat(segundoPedido.getDescuento()).isEqualTo(2500);
        assertThat(segundoPedido.getTotal()).isEqualTo(27500);
        assertThat(cuponRepository.findByCodigoIgnoreCase(primerCupon.codigo()))
                .get()
                .extracting(cupon -> cupon.getFechaUso())
                .isNotNull();
        assertThat(carritoService.obtenerCuponDisponible(cliente.getIdCliente()).codigo())
                .isNotEqualTo(primerCupon.codigo());
    }

    @Test
    void validarCupon_rechazaUnCodigoInexistente() {
        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);

        assertThatThrownBy(() -> carritoService.validarCupon(cliente.getIdCliente(), "NO-EXISTE"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("no es válido");
    }

    @Test
    void confirmarOtroPedidoSinUsarCupon_noEmiteUnSegundoCupon() {
        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);
        carritoService.confirmarPedido(cliente.getIdCliente(), null);
        String codigoInicial = carritoService.obtenerCuponDisponible(cliente.getIdCliente()).codigo();

        carritoService.agregarProducto(cliente.getIdCliente(), producto.getIdProducto(), null);
        carritoService.confirmarPedido(cliente.getIdCliente(), null);

        assertThat(carritoService.obtenerCuponDisponible(cliente.getIdCliente()).codigo())
                .isEqualTo(codigoInicial);
        assertThat(cuponRepository.findAll())
                .filteredOn(cupon -> cupon.getFechaUso() == null)
                .hasSize(1);
    }
}
