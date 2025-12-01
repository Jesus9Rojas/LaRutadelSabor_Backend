package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.RutaDelSabor.ruta.models.entities.Rol;
import java.util.Optional;

public interface IRolDAO extends CrudRepository<Rol, Long> {
Optional<Rol> findByName(String name);
}
