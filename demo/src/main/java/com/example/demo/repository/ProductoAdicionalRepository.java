package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.demo.entitys.Producto;
import com.example.demo.entitys.ProductoAdicional;
import com.example.demo.entitys.ProductoAdicionalId;

@Repository
public interface ProductoAdicionalRepository extends JpaRepository<ProductoAdicional, ProductoAdicionalId> {
    List<ProductoAdicional> findByProducto(Producto producto);
}
