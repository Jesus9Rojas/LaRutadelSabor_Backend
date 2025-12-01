package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IRolDAO;
import com.RutaDelSabor.ruta.models.entities.Rol;

@Service
public class RolServiceImpl implements IRolService{

     @Autowired
     private IRolDAO rolDAO;

    @Override
	public List<Rol> GetAll() {
		List<Rol> list = new ArrayList<Rol>(); 
		rolDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Rol Save(Rol object) {
		return rolDAO.save(object);
	}

	@Override
	public Rol FindByID(Long id) {
		return rolDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Rol object) {
		rolDAO.delete(object);
	}




}
