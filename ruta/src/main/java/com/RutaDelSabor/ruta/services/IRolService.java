package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Rol;

public interface IRolService {

    

    public List<Rol> GetAll();
	public Rol Save(Rol object);
	public Rol FindByID(Long id);
	public void Delete(Rol object);

}
