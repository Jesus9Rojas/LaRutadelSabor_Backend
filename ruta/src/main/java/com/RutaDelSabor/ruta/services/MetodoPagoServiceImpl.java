package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IMetodoPagoDAO;
import com.RutaDelSabor.ruta.models.entities.MetodoPago;

@Service
public class MetodoPagoServiceImpl implements IMetodoPagoService {


    @Autowired
     private IMetodoPagoDAO metodopagoDAO;

    @Override
	public List<MetodoPago> GetAll() {
		List<MetodoPago> list = new ArrayList<MetodoPago>(); 
		metodopagoDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public MetodoPago Save(MetodoPago object) {
		return metodopagoDAO.save(object);
	}

	@Override
	public MetodoPago FindByID(Long id) {
		return metodopagoDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(MetodoPago object) {
		metodopagoDAO.delete(object);
	}







}
