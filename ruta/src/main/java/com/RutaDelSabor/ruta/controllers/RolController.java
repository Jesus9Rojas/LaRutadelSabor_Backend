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

import com.RutaDelSabor.ruta.models.entities.Rol;
import com.RutaDelSabor.ruta.services.IRolService;

@RestController
@RequestMapping("/api")
public class RolController {

    @Autowired
	private IRolService m_Service;


	@GetMapping("/roles")
	public List<Rol> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/rol")
	@ResponseStatus(HttpStatus.CREATED)
	public Rol saveRol(@RequestBody Rol rol) {
		return m_Service.Save(rol);
	}

	@GetMapping("/rol/{id}")
	public Rol getRolByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/rol/{id}")
	public Rol updateRol(@RequestBody Rol rol, @PathVariable Long id) {
		rol.setId(id);
		return m_Service.Save(rol);
	}

	@DeleteMapping("/rol/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteRol(@PathVariable Long id) {
		Rol entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	} 

}
