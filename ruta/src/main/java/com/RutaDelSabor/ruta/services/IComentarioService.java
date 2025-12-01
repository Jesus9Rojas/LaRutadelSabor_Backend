package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.Comentario;

public interface IComentarioService {

    public List<Comentario> GetAll();
	public Comentario Save(Comentario object);
	public Comentario FindByID(Long id);
	public void Delete(Comentario object);

    

}
