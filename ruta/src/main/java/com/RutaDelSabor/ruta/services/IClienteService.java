// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/services/IClienteService.java

package com.RutaDelSabor.ruta.services;

import java.util.List;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import com.RutaDelSabor.ruta.exception.RecursoNoEncontradoException;
public interface IClienteService {

    // Métodos para la lógica de negocio (los que ya tienes en ClienteServiceImpl)
    List<Cliente> buscarTodosActivos();
    
    Cliente buscarPorIdActivo(Long id);
    
    Cliente guardar(Cliente cliente);
    
    void eliminarLogico(Long id);
    Cliente buscarPorCorreo(String correo) throws RecursoNoEncontradoException;

    // Métodos para la administración (si los necesitas aparte)
    List<Cliente> buscarTodos();
    
    Cliente buscarPorId(Long id);
}