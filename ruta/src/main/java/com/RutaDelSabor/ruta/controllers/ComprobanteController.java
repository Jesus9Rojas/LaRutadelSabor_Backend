package com.RutaDelSabor.ruta.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.RutaDelSabor.ruta.models.entities.Comprobante;
import com.RutaDelSabor.ruta.services.IComprobanteService;

@RestController
@RequestMapping("/api")
public class ComprobanteController {

    @Autowired
    private IComprobanteService m_Service;


    @GetMapping("/comprobantes")
	public List<Comprobante> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/comprobante")
	@ResponseStatus(HttpStatus.CREATED)
	public Comprobante saveComprobante(@RequestBody Comprobante comprobante) {
		return m_Service.Save(comprobante);
	}

	@GetMapping("/comprobante/{id}")
	public Comprobante getComprobanteByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/comprobante/{id}")
	public Comprobante updateComprobante(@RequestBody Comprobante comprobante, @PathVariable Long id) {
		comprobante.setId(id);
		return m_Service.Save(comprobante);
	}

	@DeleteMapping("/comprobante/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteComprobante(@PathVariable Long id) {
		Comprobante entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}






}
