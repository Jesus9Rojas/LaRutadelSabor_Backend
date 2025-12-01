package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Estado;

public interface IEstadoService {

     public List<Estado> GetAll();
	public Estado Save(Estado object);
	public Estado FindByID(Long id);
	public void Delete(Estado object);

}
