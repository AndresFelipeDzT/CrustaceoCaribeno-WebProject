package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.example.demo.entitys.ItemCarrito;
import com.example.demo.entitys.ItemCarritoAdicional;
import com.example.demo.entitys.ItemCarritoAdicionalId;

@Repository
public interface ItemCarritoAdicionalRepository extends JpaRepository<ItemCarritoAdicional, ItemCarritoAdicionalId> {
    List<ItemCarritoAdicional> findByItemCarrito(ItemCarrito itemCarrito);
}
