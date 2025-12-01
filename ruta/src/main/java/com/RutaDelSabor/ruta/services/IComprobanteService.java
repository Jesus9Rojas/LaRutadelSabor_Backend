package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Comprobante;

public interface IComprobanteService {

    public List<Comprobante> GetAll();
	public Comprobante Save(Comprobante object);
	public Comprobante FindByID(Long id);
	public void Delete(Comprobante object);

}
