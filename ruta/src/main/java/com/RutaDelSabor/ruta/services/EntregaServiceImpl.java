package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IEntregaDAO;
import com.RutaDelSabor.ruta.models.entities.Entrega;

@Service
public class EntregaServiceImpl implements IEntregaService {

     @Autowired
     private IEntregaDAO entregaDAO;

    @Override
	public List<Entrega> GetAll() {
		List<Entrega> list = new ArrayList<Entrega>(); 
		entregaDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Entrega Save(Entrega object) {
		return entregaDAO.save(object);
	}

	@Override
	public Entrega FindByID(Long id) {
		return entregaDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Entrega object) {
		entregaDAO.delete(object);
	}
}
