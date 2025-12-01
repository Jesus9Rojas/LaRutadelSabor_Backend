package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.RutaDelSabor.ruta.models.entities.Categoria;
import java.util.List;

public interface ICategoriaDAO extends CrudRepository<Categoria, Long> {
    
    // ✅ CORRECCIÓN EN LA CONSULTA (audAnulado y categoria)
    @Query("SELECT DISTINCT c FROM Categoria c LEFT JOIN FETCH c.productos p WHERE c.audAnulado = false AND p.audAnulado = false ORDER BY c.categoria")
    List<Categoria> findAllWithProducts();

    @Query("SELECT c FROM Categoria c WHERE c.audAnulado = false ORDER BY c.categoria")
    List<Categoria> findAllActive();
}