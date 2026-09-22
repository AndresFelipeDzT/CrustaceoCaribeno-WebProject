package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.demo.entitys.ItemPedido;

import com.example.demo.entitys.ItemPedidoAdicional;
import com.example.demo.entitys.ItemPedidoAdicionalId;

@Repository
public interface ItemPedidoAdicionalRepository extends JpaRepository<ItemPedidoAdicional, ItemPedidoAdicionalId> {
    List<ItemPedidoAdicional> findByItemPedido(ItemPedido itemPedido);
}
