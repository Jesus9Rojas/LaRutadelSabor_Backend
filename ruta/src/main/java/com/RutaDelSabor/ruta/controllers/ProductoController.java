package com.RutaDelSabor.ruta.controllers;

import java.util.List;
import com.RutaDelSabor.ruta.dto.ErrorResponseDTO; // Para respuestas de error
import com.RutaDelSabor.ruta.exception.ProductoNoEncontradoException; // Para catch
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Para proteger endpoints
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Producto;
import com.RutaDelSabor.ruta.services.IProductoService;
import jakarta.validation.Valid; // Para validar el body

@RestController
@RequestMapping("/api/productos") // Ruta base para productos
// @CrossOrigin(origins = "*") // Mejor en WebConfig
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @Autowired
    private IProductoService productoService; // Renombrar m_Service

    // --- Endpoints Públicos (si aplica) ---

    // GET /api/productos (Lista pública de productos activos)
    @GetMapping
    public ResponseEntity<List<Producto>> getAllPublic() {
         log.info("Solicitud GET /api/productos (público)");
         try {
            List<Producto> productos = productoService.buscarTodosActivos();
            return ResponseEntity.ok(productos);
         } catch (Exception e) {
             log.error("Error al obtener productos activos:", e);
             return ResponseEntity.internalServerError().build();
         }
    }

    // GET /api/productos/{id} (Detalle público de un producto activo)
    @GetMapping("/{id}")
    public ResponseEntity<?> getByIdPublic(@PathVariable Long id) {
         log.info("Solicitud GET /api/productos/{} (público)", id);
         try {
            Producto producto = productoService.buscarPorIdActivo(id);
            return ResponseEntity.ok(producto);
         } catch (ProductoNoEncontradoException e) {
             log.warn("Producto activo no encontrado (público): {}", e.getMessage());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
         } catch (Exception e) {
             log.error("Error al obtener producto activo ID {}:", id, e);
             return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al obtener el producto."));
         }
    }

    // --- Endpoints de Administración ---

    // GET /api/productos/admin/all (Lista completa para admin)
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Producto>> getAllAdmin() {
        log.info("Admin: Solicitud GET /api/productos/admin/all");
        List<Producto> productos = productoService.buscarTodos();
        return ResponseEntity.ok(productos);
    }

     // GET /api/productos/admin/{id} (Detalle para admin, incluso si está anulado)
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByIdAdmin(@PathVariable Long id) {
         log.info("Admin: Solicitud GET /api/productos/admin/{}", id);
         try {
            Producto producto = productoService.buscarPorId(id); // Busca incluso anulados
            return ResponseEntity.ok(producto);
         } catch (ProductoNoEncontradoException e) {
             log.warn("Admin: Producto no encontrado: {}", e.getMessage());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
         } catch (Exception e) {
             log.error("Admin: Error al obtener producto ID {}:", id, e);
             return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al obtener el producto."));
         }
    }


    // POST /api/productos/admin (Crear nuevo producto)
    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createProducto(@Valid @RequestBody Producto producto) {
        log.info("Admin: Solicitud POST /api/productos/admin");
         // Asegurarse que no venga con ID para evitar actualizar por error
         if (producto.getId() != null) {
              return ResponseEntity.badRequest().body(new ErrorResponseDTO("No incluya ID al crear un nuevo producto."));
         }
        try {
            Producto nuevoProducto = productoService.guardar(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProducto);
        } catch (Exception e) { // Capturar DataIntegrityViolationException si hay nombres duplicados, etc.
             log.error("Admin: Error al crear producto:", e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al crear el producto: " + e.getMessage()));
        }
    }

    // PUT /api/productos/admin/{id} (Actualizar producto)
    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProducto(@PathVariable Long id, @Valid @RequestBody Producto productoDetalles) {
         log.info("Admin: Solicitud PUT /api/productos/admin/{}", id);
         try {
            Producto productoExistente = productoService.buscarPorId(id); // Buscar (puede estar anulado para reactivar?)
            // Copiar los campos actualizables de productoDetalles a productoExistente
            productoExistente.setProducto(productoDetalles.getProducto()); // Ajustar si se renombró
            productoExistente.setDescripcion(productoDetalles.getDescripcion());
            productoExistente.setPrecio(productoDetalles.getPrecio());
            productoExistente.setStock(productoDetalles.getStock());
            productoExistente.setImagen(productoDetalles.getImagen());
            // Asegurarse que al actualizar se reactive si estaba anulado? O manejarlo aparte?
            // productoExistente.setAudAnulado(false); // Opcional: reactivar al actualizar
            if (productoDetalles.getCategoria() != null && productoDetalles.getCategoria().getId() != null) {
                 // Aquí necesitarías buscar la Categoria por ID para asociarla correctamente
                 // Categoria categoria = categoriaService.FindByID(productoDetalles.getCategoria().getID());
                 // productoExistente.setCategoria(categoria);
                 // Por ahora, asumimos que el ID viene y JPA lo maneja (puede dar error si no existe)
                 productoExistente.setCategoria(productoDetalles.getCategoria());
            } else {
                productoExistente.setCategoria(null); // Permitir desasociar categoría?
            }


            Producto actualizado = productoService.guardar(productoExistente); // Guardar actualiza timestamps
            return ResponseEntity.ok(actualizado);
         } catch (ProductoNoEncontradoException e) {
             log.warn("Admin: Producto no encontrado para actualizar: {}", e.getMessage());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
         } catch (Exception e) {
             log.error("Admin: Error al actualizar producto ID {}:", id, e);
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al actualizar el producto: " + e.getMessage()));
         }
    }

    // DELETE /api/productos/admin/{id} (Borrado lógico)
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProducto(@PathVariable Long id) {
         log.warn("Admin: Solicitud DELETE /api/productos/admin/{}", id);
         try {
            productoService.eliminarLogico(id);
             log.info("Admin: Borrado lógico de producto ID {} completado.", id);
            return ResponseEntity.noContent().build(); // 204 No Content
         } catch (ProductoNoEncontradoException e) {
             log.error("Admin: Error al borrar, producto ID {} no encontrado.", id);
             // Incluso si no se encontró, la petición DELETE tuvo éxito semánticamente (ya no existe)
             return ResponseEntity.noContent().build(); // O podrías devolver 404 si prefieres
         } catch (Exception e) {
             log.error("Admin: Error inesperado al borrar producto ID {}:", id, e);
             return ResponseEntity.internalServerError().build(); // 500 Internal Server Error
         }
    }
}