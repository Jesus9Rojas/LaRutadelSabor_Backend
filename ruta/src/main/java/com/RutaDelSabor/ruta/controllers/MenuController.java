package com.RutaDelSabor.ruta.controllers;

import com.RutaDelSabor.ruta.dto.CategoriaMenuDTO; // Importar DTOs
import com.RutaDelSabor.ruta.models.entities.Categoria;
import com.RutaDelSabor.ruta.services.ICategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin; // Para CORS
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Mejor configurar en WebConfig globalmente
public class MenuController {

    @Autowired
    private ICategoriaService categoriaService;

    @GetMapping("/menu")
    public ResponseEntity<List<CategoriaMenuDTO>> getMenu() {
        try {
            List<Categoria> categoriasConProductos = categoriaService.GetAllWithProducts();

            List<CategoriaMenuDTO> menu = categoriasConProductos.stream()
                    .map(CategoriaMenuDTO::fromEntity) // Usar el método estático del DTO
                    .collect(Collectors.toList());

            return ResponseEntity.ok(menu);
        } catch (Exception e) {
            // Loggear el error e.printStackTrace();
            return ResponseEntity.internalServerError().build(); // Error 500
        }
    }
}