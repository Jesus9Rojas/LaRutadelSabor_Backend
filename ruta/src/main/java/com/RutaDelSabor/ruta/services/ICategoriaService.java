package com.RutaDelSabor.ruta.services;

import java.util.List;
import com.RutaDelSabor.ruta.models.entities.Categoria;

public interface ICategoriaService {
    List<Categoria> GetAll();
    Categoria Save(Categoria object);
    Categoria FindByID(Long id);
    void Delete(Long id); // Cambiado para recibir ID

    List<Categoria> GetAllWithProducts(); // Método para el menú
}