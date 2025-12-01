package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IComprobanteDAO;
import com.RutaDelSabor.ruta.models.entities.Comprobante;

@Service
public class ComprobanteServiceImpl implements IComprobanteService{

    @Autowired
    private IComprobanteDAO comprobanteDAO;

    
    @Override
	public List<Comprobante> GetAll() {
		List<Comprobante> list = new ArrayList<Comprobante>(); 
		comprobanteDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public Comprobante Save(Comprobante object) {
		return comprobanteDAO.save(object);
	}

	@Override
	public Comprobante FindByID(Long id) {
		return comprobanteDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(Comprobante object) {
		comprobanteDAO.delete(object);
	}

}
