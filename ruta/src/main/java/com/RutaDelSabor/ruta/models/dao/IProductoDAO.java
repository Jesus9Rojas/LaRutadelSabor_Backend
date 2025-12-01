package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.RutaDelSabor.ruta.models.entities.Producto;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

// Renombrar a ProductoRepository es mejor práctica
public interface IProductoDAO extends CrudRepository<Producto, Long> {

    // Para bloquear el producto mientras se actualiza el stock (ya estaba)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT p FROM Producto p WHERE p.id = :id AND p.audAnulado = false")
Optional<Producto> findByIdAndLock(@Param("id") Long id);

    // --- NUEVOS MÉTODOS ---
    // Buscar solo activos
List<Producto> findByAudAnuladoFalseOrderByProducto(); // Correcto (usa isAudAnulado)
    // Buscar uno activo por ID
    Optional<Producto> findByIdAndAudAnuladoFalse(Long id);

    // Buscar TODOS (incluyendo anulados, para admin?)
    List<Producto> findAllByOrderByProducto();
}