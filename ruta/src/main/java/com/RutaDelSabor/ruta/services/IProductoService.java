package com.RutaDelSabor.ruta.services;

import java.util.List;
import com.RutaDelSabor.ruta.models.entities.Producto;

public interface IProductoService {
    List<Producto> buscarTodosActivos(); // Cambiado nombre para claridad
    Producto buscarPorIdActivo(Long id); // Cambiado nombre
    Producto guardar(Producto producto); // Cambiado nombre
    void eliminarLogico(Long id); // Cambiado nombre y parámetro
    Producto actualizarStock(Long id, int cantidad); // Nuevo método útil
    // Mantener métodos antiguos si son usados internamente o por admin
    List<Producto> buscarTodos();
    Producto buscarPorId(Long id);
}