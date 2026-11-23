package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.entitys.Cliente;
import com.example.demo.entitys.Domiciliario;
import com.example.demo.entitys.Pedido;

import jakarta.persistence.LockModeType;

/**
 * Repositorio de acceso a datos para pedido mediante Spring Data JPA.
 */
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("""
            select distinct p from Pedido p left join fetch p.items i left join fetch i.producto
            where lower(trim(p.estado)) = 'entregado'
            and (:desde is null or p.fechaEntrega >= :desde)
            and (:hasta is null or p.fechaEntrega <= :hasta)
            """)
    List<Pedido> buscarEntregados(@Param("desde") LocalDate desde,
            @Param("hasta") LocalDate hasta);

    Optional<Pedido> findByIdPedidoAndCliente_IdCliente(Long idPedido, Long idCliente);
    List<Pedido> findByCliente(Cliente cliente);
    boolean existsByIdPedidoAndCliente_IdCliente(Long idPedido, Long idCliente);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select pedido from Pedido pedido where pedido.idPedido = :id")
    Optional<Pedido> findByIdParaActualizar(@Param("id") Long id);
    List<Pedido> findByDomiciliario(Domiciliario domiciliario);
    List<Pedido> findByEstado(String estado);
}
