package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import java.util.List;
import org.springframework.data.domain.Pageable;
import com.RutaDelSabor.ruta.models.entities.PedidoDetallado;

public interface IPedidoDetalladoDAO extends CrudRepository<PedidoDetallado, Long> {

    // ✅ CORRECCIÓN: Usar nombres de atributos Java (camelCase)
    @Query("SELECT pd.producto.id, pd.producto.producto, SUM(pd.cantidad) as totalVendido " +
           "FROM PedidoDetallado pd " +
           "WHERE pd.pedido.audAnulado = false " +
           "GROUP BY pd.producto.id, pd.producto.producto " +
           "ORDER BY totalVendido DESC")
    List<Object[]> findTopVentas(Pageable pageable);
}