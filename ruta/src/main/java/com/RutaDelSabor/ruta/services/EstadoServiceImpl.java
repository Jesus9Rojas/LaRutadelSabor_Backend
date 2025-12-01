package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IEstadoDAO;
import com.RutaDelSabor.ruta.models.entities.Estado;



@Service
public class EstadoServiceImpl implements IEstadoService{

     @Autowired
     private IEstadoDAO estadoDAO;

    @Override
	public List<Estado> GetAll() {
		List<Estado> list = new ArrayList<Estado>(); 
		estadoDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Estado Save(Estado object) {
		return estadoDAO.save(object);
	}

	@Override
	public Estado FindByID(Long id) {
		return estadoDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Estado object) {
		estadoDAO.delete(object);
	}

}
