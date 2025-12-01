package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.repository.CrudRepository;
import com.RutaDelSabor.ruta.models.entities.Cliente; // Cambiar entidad
import java.util.List;
import java.util.Optional;

// Renombrar a ClienteRepository
public interface IClienteDAO extends CrudRepository<Cliente, Long> { // Cambiar entidad

    // Método para buscar por correo (ya lo teníamos)
    Optional<Cliente> findByCorreo(String correo);

    // Buscar solo activos
    List<Cliente> findByAudAnuladoFalseOrderByApellidoAscNombreAsc(); // Ordenar

    // Buscar uno activo por ID
    Optional<Cliente> findByIdAndAudAnuladoFalse(Long id);

    // Buscar TODOS (incluyendo anulados)
    List<Cliente> findAllByOrderByApellidoAscNombreAsc(); // Ordenar
}