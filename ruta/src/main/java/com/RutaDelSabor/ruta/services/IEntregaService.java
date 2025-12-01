package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Entrega;


public interface IEntregaService {

      public List<Entrega> GetAll();
	public Entrega Save(Entrega object);
	public Entrega FindByID(Long id);
	public void Delete(Entrega object);

}
