package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.MetodoPago;

public interface IMetodoPagoService {

    public List<MetodoPago> GetAll();
	public MetodoPago Save(MetodoPago object);
	public MetodoPago FindByID(Long id);
	public void Delete(MetodoPago object);





}
