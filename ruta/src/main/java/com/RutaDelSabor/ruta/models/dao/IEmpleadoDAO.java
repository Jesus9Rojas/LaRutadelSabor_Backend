package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.RutaDelSabor.ruta.models.entities.Empleado;

public interface IEmpleadoDAO extends CrudRepository<Empleado, Long> {

}
