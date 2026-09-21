package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Carrito;
import com.example.demo.entitys.ItemCarrito;
import com.example.demo.entitys.Producto;

@Repository
public interface ItemCarritoRepository extends JpaRepository<ItemCarrito, Long> {
    List<ItemCarrito> findByCarrito(Carrito carrito);
    List<ItemCarrito> findByProducto(Producto producto);
}
