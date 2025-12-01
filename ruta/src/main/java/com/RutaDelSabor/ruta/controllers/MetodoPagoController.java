package com.RutaDelSabor.ruta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.RutaDelSabor.ruta.models.entities.MetodoPago;
import com.RutaDelSabor.ruta.services.IMetodoPagoService;

@RestController
@RequestMapping("/api")
public class MetodoPagoController {


    @Autowired
    private IMetodoPagoService m_Service;


     @GetMapping("/metodopagos")
	public List<MetodoPago> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/metodopago")
	@ResponseStatus(HttpStatus.CREATED)
	public MetodoPago saveMetodoPago(@RequestBody MetodoPago metodopago) {
		return m_Service.Save(metodopago);
	}

	@GetMapping("/metodopago/{id}")
	public MetodoPago getMetodoPagoByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/metodopago/{id}")
	public MetodoPago updateMetodoPago (@RequestBody MetodoPago metodopago, @PathVariable Long id) {
		metodopago.setId(id);
		return m_Service.Save(metodopago);
	}

	@DeleteMapping("/metodopago/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteMetodoPago(@PathVariable Long id) {
		MetodoPago entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}

}
