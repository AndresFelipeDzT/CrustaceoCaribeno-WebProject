package com.example.demo.entitys;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemCarritoAdicionalId implements Serializable {
    private Long itemCarrito;
    private Long adicional;
}
