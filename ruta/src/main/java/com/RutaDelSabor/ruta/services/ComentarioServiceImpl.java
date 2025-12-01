package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IComentarioDAO;
import com.RutaDelSabor.ruta.models.entities.Comentario;

@Service
public class ComentarioServiceImpl implements IComentarioService {

     @Autowired
    private IComentarioDAO comentarioDAO;

    @Override
	public List<Comentario> GetAll() {
		List<Comentario> list = new ArrayList<Comentario>(); 
		comentarioDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Comentario Save(Comentario object) {
		return comentarioDAO.save(object);
	}

	@Override
	public Comentario FindByID(Long id) {
		return comentarioDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Comentario object) {
		comentarioDAO.delete(object);
	}
    





}
