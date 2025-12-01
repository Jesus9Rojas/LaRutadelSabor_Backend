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

import com.RutaDelSabor.ruta.models.entities.Estado;
import com.RutaDelSabor.ruta.services.IEstadoService;

@RestController
@RequestMapping("/api")
public class EstadoController {

    @Autowired
    private IEstadoService m_Service;


     @GetMapping("/estados")
	public List<Estado> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/estado")
	@ResponseStatus(HttpStatus.CREATED)
	public Estado saveEstado(@RequestBody Estado estado) {
		return m_Service.Save(estado);
	}

	@GetMapping("/estado/{id}")
	public Estado getEstadoByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/estado/{id}")
	public Estado updateEstado(@RequestBody Estado estado, @PathVariable Long id) {
		estado.setId(id);
		return m_Service.Save(estado);
	}

	@DeleteMapping("/estado/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEstado(@PathVariable Long id) {
		Estado entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}



}
