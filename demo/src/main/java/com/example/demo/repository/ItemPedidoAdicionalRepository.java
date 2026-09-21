package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.ItemPedidoAdicional;
import com.example.demo.entitys.ItemPedidoAdicionalId;

@Repository
public interface ItemPedidoAdicionalRepository extends JpaRepository<ItemPedidoAdicional, ItemPedidoAdicionalId> {}
