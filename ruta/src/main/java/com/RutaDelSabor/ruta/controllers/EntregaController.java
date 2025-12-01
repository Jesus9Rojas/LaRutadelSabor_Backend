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

import com.RutaDelSabor.ruta.models.entities.Entrega;
import com.RutaDelSabor.ruta.services.IEntregaService;

@RestController
@RequestMapping("/api")
public class EntregaController {

    @Autowired
    private IEntregaService m_Service;

     @GetMapping("/entregas")
	public List<Entrega> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/entrega")
	@ResponseStatus(HttpStatus.CREATED)
	public Entrega saveEntrega(@RequestBody Entrega entrega) {
		return m_Service.Save(entrega);
	}

	@GetMapping("/entrega/{id}")
	public Entrega getEntregaByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/entrega/{id}")
	public Entrega updateEntrega(@RequestBody Entrega entrega, @PathVariable Long id) {
		entrega.setId(id);
		return m_Service.Save(entrega);
	}

	@DeleteMapping("/entrega/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEntrega(@PathVariable Long id) {
		Entrega entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}
}
