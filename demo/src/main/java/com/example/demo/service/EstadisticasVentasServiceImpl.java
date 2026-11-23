package com.example.demo.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.ResumenVentasDTO;
import com.example.demo.dto.VentaPlatoDTO;
import com.example.demo.entitys.ItemPedido;
import com.example.demo.entitys.Pedido;
import com.example.demo.repository.PedidoRepository;

@Service
@Transactional(readOnly = true)
public class EstadisticasVentasServiceImpl implements EstadisticasVentasService {
    private final PedidoRepository pedidos;

    public EstadisticasVentasServiceImpl(PedidoRepository pedidos) {
        this.pedidos = pedidos;
    }

    @Override
    public ResumenVentasDTO consultar(LocalDate desde, LocalDate hasta) {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new IllegalArgumentException("La fecha inicial debe ser anterior o igual a la final.");
        }
        List<Pedido> entregados = pedidos.buscarEntregados(desde, hasta);
        Map<Long, VentaPlatoDTO> porPlato = new HashMap<>();
        int sinImporte = 0;
        long unidades = 0;
        double totalCobrado = 0;
        for (Pedido pedido : entregados) {
            boolean importesCompletos = true;
            double subtotal = 0;
            for (ItemPedido item : pedido.getItems()) {
                unidades += item.getCantidad();
                if (item.getSubtotalVenta() == null) importesCompletos = false;
                else subtotal += item.getSubtotalVenta();
                acumularPlato(porPlato, item);
            }
            if (importesCompletos) totalCobrado += subtotal - pedido.getDescuento() + pedido.getDomicilio();
            else sinImporte++;
        }
        List<VentaPlatoDTO> platos = porPlato.values().stream()
                .sorted(Comparator.comparingLong(VentaPlatoDTO::getUnidades).reversed()
                        .thenComparing(VentaPlatoDTO::getNombre))
                .toList();
        return new ResumenVentasDTO(entregados.size(), unidades, totalCobrado, sinImporte, platos);
    }

    private void acumularPlato(Map<Long, VentaPlatoDTO> porPlato, ItemPedido item) {
        Long id = item.getProducto() == null ? null : item.getProducto().getIdProducto();
        String nombre = id == null ? "Producto retirado" : item.getProducto().getNombre();
        VentaPlatoDTO anterior = porPlato.getOrDefault(id, new VentaPlatoDTO(id, nombre, 0, 0, 0));
        Double precio = item.getPrecioUnitarioVenta();
        long cantidad = item.getCantidad();
        porPlato.put(id, new VentaPlatoDTO(id, nombre, anterior.getUnidades() + cantidad,
                anterior.getValorPlatos() + (precio == null ? 0 : precio * cantidad),
                anterior.getUnidadesSinPrecio() + (precio == null ? cantidad : 0)));
    }
}
