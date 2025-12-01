package com.RutaDelSabor.ruta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.RutaDelSabor.ruta.models.entities.PedidoDetallado;
import com.RutaDelSabor.ruta.services.IPedidoDetalladoService;


@RestController
@RequestMapping("/api")
public class PedidoDetalladoController {

    @Autowired
    private IPedidoDetalladoService m_Service;

     @GetMapping("/pedidodetallados")
	public List<PedidoDetallado> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/pedidodetallado")
	@ResponseStatus(HttpStatus.CREATED)
	public PedidoDetallado savePedidoDetallado(@RequestBody PedidoDetallado pedidodetallado) {
		return m_Service.Save(pedidodetallado);
	}

	@GetMapping("/pedidodetallado/{id}")
	public PedidoDetallado getPedidoDetalladoByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/pedidodetallado/{id}")
	public PedidoDetallado updatePedidoDetallado (@RequestBody PedidoDetallado pedidodetallado, @PathVariable Long id) {
		pedidodetallado.setId(id);
		return m_Service.Save(pedidodetallado);
	}

	@DeleteMapping("/pedidodetallado/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deletePedidoDetallado (@PathVariable Long id) {
		PedidoDetallado entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}



}
