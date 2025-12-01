package com.RutaDelSabor.ruta.controllers;

import java.util.List;
import com.RutaDelSabor.ruta.dto.ErrorResponseDTO;
import com.RutaDelSabor.ruta.exception.RecursoNoEncontradoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import com.RutaDelSabor.ruta.services.IClienteService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private static final Logger log = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private IClienteService clienteService;

    // --- Endpoints de Administración ---

    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Cliente>> getAllAdmin() {
        log.info("Admin: Solicitud GET /api/clientes/admin/all");
        List<Cliente> clientes = clienteService.buscarTodos();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getByIdAdmin(@PathVariable Long id) {
        log.info("Admin: Solicitud GET /api/clientes/admin/{}", id);
        try {
            Cliente cliente = clienteService.buscarPorId(id);
            return ResponseEntity.ok(cliente);
        } catch (RecursoNoEncontradoException e) {
            log.warn("Admin: Cliente no encontrado: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Admin: Error al obtener cliente ID {}:", id, e);
            return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al obtener el cliente."));
        }
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createCliente(@Valid @RequestBody Cliente cliente) {
        log.info("Admin: Solicitud POST /api/clientes/admin");
        if (cliente.getId() != null) { // CORREGIDO: getId()
            return ResponseEntity.badRequest().body(new ErrorResponseDTO("No incluya ID al crear."));
        }
        try {
            Cliente nuevoCliente = clienteService.guardar(cliente);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoCliente);
        } catch (Exception e) {
            log.error("Admin: Error al crear cliente:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al crear el cliente: " + e.getMessage()));
        }
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateCliente(@PathVariable Long id, @Valid @RequestBody Cliente clienteDetalles) {
        log.info("Admin: Solicitud PUT /api/clientes/admin/{}", id);
        try {
            Cliente clienteExistente = clienteService.buscarPorId(id);

            // Copiar campos actualizables
            clienteExistente.setNombre(clienteDetalles.getNombre());
            clienteExistente.setApellido(clienteDetalles.getApellido());
            clienteExistente.setCorreo(clienteDetalles.getCorreo());
            clienteExistente.setTelefono(clienteDetalles.getTelefono());
            // ✅ CORRECCIÓN AQUÍ
            clienteExistente.setFechaNacimiento(clienteDetalles.getFechaNacimiento());
            clienteExistente.setDireccion(clienteDetalles.getDireccion());

            Cliente actualizado = clienteService.guardar(clienteExistente);
            return ResponseEntity.ok(actualizado);
        } catch (RecursoNoEncontradoException e) {
            log.warn("Admin: Cliente no encontrado para actualizar: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Admin: Error al actualizar cliente ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al actualizar el cliente: " + e.getMessage()));
        }
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCliente(@PathVariable Long id) {
        log.warn("Admin: Solicitud DELETE /api/clientes/admin/{}", id);
        try {
            clienteService.eliminarLogico(id);
            log.info("Admin: Borrado lógico de cliente ID {} completado.", id);
            return ResponseEntity.noContent().build();
        } catch (RecursoNoEncontradoException e) {
            log.error("Admin: Error al borrar, cliente ID {} no encontrado.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Admin: Error inesperado al borrar cliente ID {}:", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        log.info("Solicitud GET /api/clientes/me para usuario autenticado");

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Intento de acceso a /me no autenticado.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDTO("Acceso no autorizado."));
        }

        try {
            // El "name" en el principal es el correo (configurado en UserDetailsServiceImpl)
            String userEmail = authentication.getName();
            log.info("Buscando datos para el usuario: {}", userEmail);

            // --- ¡NECESITARÁS ESTE MÉTODO EN TU SERVICE! ---
            // (Lo creamos en el Paso 2)
            Cliente cliente = clienteService.buscarPorCorreo(userEmail);

            return ResponseEntity.ok(cliente);

        } catch (RecursoNoEncontradoException e) {
            log.warn("Usuario autenticado no encontrado en la BBDD: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener datos de /me:", e);
            return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al obtener datos del usuario."));
        }
    }
}