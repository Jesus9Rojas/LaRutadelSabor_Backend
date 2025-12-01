package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import java.util.List;

public interface IPedidoDAO extends CrudRepository<Pedido, Long> {

    // Para obtener historial de un cliente
    List<Pedido> findByCliente_IdOrderByFechaPedidoDesc(Long clienteId);

    // ✅ CORRECCIÓN: Usar nombres de atributos Java (camelCase), no nombres de columnas SQL
    @Query("SELECT SUM(p.total) FROM Pedido p WHERE p.fechaPedido BETWEEN :inicio AND :fin AND p.audAnulado = false")
    Double sumTotalBetweenDates(@Param("inicio") Date inicio, @Param("fin") Date fin);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.fechaPedido BETWEEN :inicio AND :fin AND p.audAnulado = false")
    Long countBetweenDates(@Param("inicio") Date inicio, @Param("fin") Date fin);
}