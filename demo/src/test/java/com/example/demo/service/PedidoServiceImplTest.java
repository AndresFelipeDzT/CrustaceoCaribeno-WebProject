package com.example.demo.service;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.assertj.core.api.Assertions;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.example.demo.dto.AdicionalDTO;
import com.example.demo.dto.ItemPedidoDTO;
import com.example.demo.dto.PedidoDTO;
import com.example.demo.entitys.Adicional;
import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.entitys.Producto;
import com.example.demo.repository.ItemPedidoRepository;
import com.example.demo.repository.PedidoRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class PedidoServiceImplTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private ItemPedidoRepository itemPedidoRepository;

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void pedidoService_obtenerPedidosDTO_soloActivos_listaDePedidoDTO() {
        Cliente cliente = new Cliente(
                7L,
                "Ana",
                "Ríos",
                "ana@example.com",
                "3001234567",
                "Calle 1",
                "clave",
                true,
                null);
        Pedido activo = new Pedido(
                1L,
                LocalDate.of(2026, 1, 15),
                null,
                "En preparación",
                cliente,
                null,
                List.of());
        Pedido entregado = new Pedido(
                2L,
                LocalDate.of(2026, 1, 15),
                null,
                "Entregado",
                cliente,
                null,
                List.of());
        Pedido cancelado = new Pedido(
                3L,
                LocalDate.of(2026, 1, 15),
                null,
                "Cancelado",
                cliente,
                null,
                List.of());
        when(clienteService.obtenerClientePorId(7L)).thenReturn(cliente);
        when(pedidoRepository.findByCliente(cliente)).thenReturn(List.of(activo, entregado, cancelado));

        List<PedidoDTO> resultado = pedidoService.obtenerPedidosDTO(true, 7L);

        Assertions.assertThat(resultado).hasSize(1);
        Assertions.assertThat(resultado.get(0).getIdPedido()).isEqualTo(1L);
        Assertions.assertThat(resultado.get(0).getIdCliente()).isEqualTo(7L);
    }

    @Test
    void pedidoService_obtenerPedidoDTOPorId_pedidoDTO() {
        Pedido pedido = new Pedido(
                3L,
                LocalDate.of(2026, 1, 15),
                null,
                "Pendiente",
                null,
                null,
                List.of());
        when(pedidoRepository.findById(3L)).thenReturn(Optional.ofNullable(pedido));

        PedidoDTO resultado = pedidoService.obtenerPedidoDTOPorId(3L);

        Assertions.assertThat(resultado.getIdPedido()).isEqualTo(3L);
        Assertions.assertThat(resultado.getEstado()).isEqualTo("Pendiente");
        Assertions.assertThat(resultado.getTotal()).isZero();
    }

    @Test
    void pedidoService_obtenerItemsDTODePedido_listaDeItemPedidoDTO() {
        Pedido pedido = new Pedido(
                4L,
                LocalDate.of(2026, 1, 15),
                null,
                "En preparación",
                null,
                null,
                List.of());
        Producto producto = new Producto(
                11L,
                "Cazuela",
                25000.0,
                null,
                null,
                true,
                null);
        ItemPedido item = new ItemPedido(
                21L,
                2,
                pedido,
                producto,
                List.of());
        when(pedidoRepository.findById(4L)).thenReturn(Optional.ofNullable(pedido));
        when(itemPedidoRepository.findByPedido(pedido)).thenReturn(List.of(item));

        List<ItemPedidoDTO> resultado = pedidoService.obtenerItemsDTODePedido(4L);

        Assertions.assertThat(resultado).hasSize(1);
        Assertions.assertThat(resultado.get(0).getIdItemPedido()).isEqualTo(21L);
        Assertions.assertThat(resultado.get(0).getIdProducto()).isEqualTo(11L);
        Assertions.assertThat(resultado.get(0).getNombreProducto()).isEqualTo("Cazuela");
        Assertions.assertThat(resultado.get(0).getSubtotal()).isEqualTo(50000.0);
    }

    @Test
    void pedidoService_obtenerAdicionalesDTODeItem_listaDeAdicionalDTO() {
        Pedido pedido = new Pedido(
                5L,
                LocalDate.of(2026, 1, 15),
                null,
                "Pendiente",
                null,
                null,
                List.of());
        Adicional adicional = new Adicional(
                31L, 
                "Aguacate", 
                3000.0, true, 
                List.of());
        ItemPedido item = new ItemPedido(
                22L,
                1,
                pedido,
                null,
                List.of(adicional));
        when(pedidoRepository.findById(5L)).thenReturn(Optional.ofNullable(pedido));
        when(itemPedidoRepository.findById(22L)).thenReturn(Optional.ofNullable(item));

        List<AdicionalDTO> resultado = pedidoService.obtenerAdicionalesDTODeItem(5L, 22L);

        Assertions.assertThat(resultado).hasSize(1);
        Assertions.assertThat(resultado.get(0).getIdAdicional()).isEqualTo(31L);
        Assertions.assertThat(resultado.get(0).getNombre()).isEqualTo("Aguacate");
        Assertions.assertThat(resultado.get(0).getPrecio()).isEqualTo(3000.0);
    }

    @Test
    void pedidoService_obtenerPedidosDTO_listaDePedidoDTO() {
        Pedido pendiente = new Pedido(
                6L,
                LocalDate.of(2026, 1, 15),
                null,
                "Pendiente",
                null,
                null,
                List.of());
        Pedido entregado = new Pedido(
                7L,
                LocalDate.of(2026, 1, 15),
                null,
                "Entregado",
                null,
                null,
                List.of());
        when(pedidoRepository.findAll()).thenReturn(List.of(pendiente, entregado));

        List<PedidoDTO> resultado = pedidoService.obtenerPedidosDTO();

        Assertions.assertThat(resultado).hasSize(2);
        Assertions.assertThat(resultado.get(0).getIdPedido()).isEqualTo(6L);
        Assertions.assertThat(resultado.get(1).getIdPedido()).isEqualTo(7L);
    }

}
