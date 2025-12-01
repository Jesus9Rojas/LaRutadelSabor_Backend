package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Documento;

public interface IDocumentoService {
    public List<Documento> GetAll();
	public Documento Save(Documento object);
	public Documento FindByID(Long id);
	public void Delete(Documento object);

}
