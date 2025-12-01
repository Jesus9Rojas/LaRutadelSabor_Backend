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

import com.RutaDelSabor.ruta.models.entities.Empleado;
import com.RutaDelSabor.ruta.services.IEmpleadoService;

@RestController
@RequestMapping("/api")
public class EmpleadoController {

    @Autowired
    private IEmpleadoService m_Service;

     @GetMapping("/empleados")
	public List<Empleado> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/empleado")
	@ResponseStatus(HttpStatus.CREATED)
	public Empleado saveEmpleado(@RequestBody Empleado empleado) {
		return m_Service.Save(empleado);
	}

	@GetMapping("/empleado/{id}")
	public Empleado getEmpleadoByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/empleado/{id}")
	public Empleado updateEmpleado(@RequestBody Empleado empleado, @PathVariable Long id) {
		empleado.setId(id);
		return m_Service.Save(empleado);
	}

	@DeleteMapping("/empleado/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteEmpleado(@PathVariable Long id) {
		Empleado entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}

}
