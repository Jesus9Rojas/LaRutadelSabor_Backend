package com.RutaDelSabor.ruta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.RutaDelSabor.ruta.models.entities.Documento;
import com.RutaDelSabor.ruta.services.IDocumentoService;



@RestController
@RequestMapping("/api")
public class DocumentoController {

    @Autowired
    private IDocumentoService m_Service;

     @GetMapping("/documentos")
	public List<Documento> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/documento")
	@ResponseStatus(HttpStatus.CREATED)
	public Documento saveDocumento(@RequestBody Documento documento) {
		return m_Service.Save(documento);
	}

	@GetMapping("/documento/{id}")
	public Documento getDocumentoByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/documento/{id}")
	public Documento updateDocumento(@RequestBody Documento documento, @PathVariable Long id) {
		documento.setId(id);
		return m_Service.Save(documento);
	}

	@DeleteMapping("/documento/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteDocumento(@PathVariable Long id) {
		Documento entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}

}
