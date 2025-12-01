package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Empleado;

public interface IEmpleadoService {

      public List<Empleado> GetAll();
	public Empleado Save(Empleado object);
	public Empleado FindByID(Long id);
	public void Delete(Empleado object);

    

}
