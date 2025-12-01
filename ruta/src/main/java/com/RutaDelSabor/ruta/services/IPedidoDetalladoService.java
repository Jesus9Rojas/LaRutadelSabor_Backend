package com.RutaDelSabor.ruta.services;

import java.util.List;

import com.RutaDelSabor.ruta.models.entities.PedidoDetallado;

public interface IPedidoDetalladoService {

    public List<PedidoDetallado> GetAll();
	public PedidoDetallado Save(PedidoDetallado object);
	public PedidoDetallado FindByID(Long id);
	public void Delete(PedidoDetallado object);

}
