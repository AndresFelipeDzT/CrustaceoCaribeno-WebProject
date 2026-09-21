package com.example.demo.entitys;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductoAdicionalId implements Serializable {
    private Long producto;
    private Long adicional;
}
