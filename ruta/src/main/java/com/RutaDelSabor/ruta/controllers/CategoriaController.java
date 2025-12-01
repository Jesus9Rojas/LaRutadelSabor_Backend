// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/controllers/CategoriaController.java

package com.RutaDelSabor.ruta.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Categoria;
import com.RutaDelSabor.ruta.services.ICategoriaService;


@RestController
@RequestMapping("/api")
public class CategoriaController {

    @Autowired
    private ICategoriaService m_Service;

    // --- ENDPOINTS PÚBLICOS ---
    @GetMapping("/categorias")
	public List<Categoria> getAll() {
		return m_Service.GetAll();
	}

    // [CRÍTICO - COHESIÓN CON FRONTEND] ENDPOINT DE ADMINISTRACIÓN (admin.js)
    @GetMapping("/categorias/admin/all") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Categoria>> getAllAdmin() {
        return ResponseEntity.ok(m_Service.GetAll()); 
    }
    
    // --- ENDPOINTS CRUD (Internos/Admin) ---
	@PostMapping("/categoria")
	@ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')") 
	public Categoria saveAbono(@RequestBody Categoria categoria) {
		return m_Service.Save(categoria);
	}

	@GetMapping("/categoria/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public Categoria getCategoriaByid(@PathVariable Long id) {
		return m_Service.FindByID(id);
	}

	@PutMapping("/categoria/{id}")
    @PreAuthorize("hasRole('ADMIN')")
	public Categoria updateCategoria(@RequestBody Categoria categoria, @PathVariable Long id) {
		categoria.setId(id);
		return m_Service.Save(categoria);
	}

	@DeleteMapping("/categoria/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
	public void deleteCategoria(@PathVariable Long id) {
		m_Service.Delete(id); 
	}
}