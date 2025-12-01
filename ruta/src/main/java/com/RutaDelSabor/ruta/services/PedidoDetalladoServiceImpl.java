package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.RutaDelSabor.ruta.models.dao.IPedidoDetalladoDAO;
import com.RutaDelSabor.ruta.models.entities.PedidoDetallado;

@Service
public class PedidoDetalladoServiceImpl implements IPedidoDetalladoService{

    @Autowired
    private IPedidoDetalladoDAO pedidoDetalladoDAO;


    
    @Override
	public List<PedidoDetallado> GetAll() {
		List<PedidoDetallado> list = new ArrayList<PedidoDetallado>(); 
		pedidoDetalladoDAO.findAll().forEach(list::add);
		return list;
	}

	@Override
	public PedidoDetallado Save(PedidoDetallado object) {
		return pedidoDetalladoDAO.save(object);
	}

	@Override
	public PedidoDetallado FindByID(Long id) {
		return pedidoDetalladoDAO.findById(id).orElse(null);
	}

	@Override
	public void Delete(PedidoDetallado object) {
		pedidoDetalladoDAO.delete(object);
	}








}
