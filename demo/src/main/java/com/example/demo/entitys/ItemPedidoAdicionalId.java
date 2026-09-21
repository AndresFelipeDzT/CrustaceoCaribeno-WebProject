package com.example.demo.entitys;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemPedidoAdicionalId implements Serializable {
    private Long itemPedido;
    private Long adicional;
}
