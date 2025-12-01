package com.RutaDelSabor.ruta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.RutaDelSabor.ruta.models.entities.Comentario;
import com.RutaDelSabor.ruta.services.IComentarioService;

@RestController
@RequestMapping("/api")
public class ComentarioController {

    @Autowired
    private IComentarioService m_Service;

    @GetMapping("/comentarios")
	public List<Comentario> getAll() {
		return m_Service.GetAll();
	}

	@PostMapping("/comentario")
	@ResponseStatus(HttpStatus.CREATED)
	public Comentario saveComentario(@RequestBody Comentario comentario) {
		return m_Service.Save(comentario);
	}

	@GetMapping("/comentario/{id}")
	public Comentario getComentarioByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/comentario/{id}")
	public Comentario updateComentario(@RequestBody Comentario comentario, @PathVariable Long id) {
		comentario.setId(id);
		return m_Service.Save(comentario);
	}

	@DeleteMapping("/comentario/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteComentario(@PathVariable Long id) {
		Comentario entity = m_Service.FindByID(id);
		if (entity != null) {
			m_Service.Delete(entity);
		}
	}

}
