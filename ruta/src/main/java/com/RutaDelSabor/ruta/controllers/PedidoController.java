// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/controllers/PedidoController.java

package com.RutaDelSabor.ruta.controllers;

import java.util.List;
import java.util.stream.Collectors;


import com.RutaDelSabor.ruta.dto.ErrorResponseDTO;
import com.RutaDelSabor.ruta.dto.EstadoResponseDTO;
import com.RutaDelSabor.ruta.dto.OrdenRequestDTO;
import com.RutaDelSabor.ruta.dto.OrdenResponseDTO;
import com.RutaDelSabor.ruta.dto.PedidoHistorialDTO;
import com.RutaDelSabor.ruta.exception.PedidoNoEncontradoException;
import com.RutaDelSabor.ruta.exception.ProductoNoEncontradoException;
import com.RutaDelSabor.ruta.exception.StockInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import com.RutaDelSabor.ruta.services.IPedidoService;
import com.RutaDelSabor.ruta.services.PedidoServiceImpl; // [CRÍTICO] Necesario para updatePedidoStatus
import jakarta.validation.Valid;


// [CRÍTICO - DTO ADICIONAL] DTO de solicitud para actualizar estado (debe estar en el paquete DTOs, se incluye aquí para contexto)
class EstadoUpdateRequestDTO {
    private String nuevoEstado;
    private String notas;
    
    public String getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }
    public String getNotas() { return notas; }
    public void setNotas(String notas) { this.notas = notas; }
}


@RestController
@RequestMapping("/api")
public class PedidoController {

    private static final Logger log = LoggerFactory.getLogger(PedidoController.class);

    @Autowired
    private IPedidoService pedidoService;
    
    @Autowired
    private PedidoServiceImpl pedidoServiceImpl; // <<-- INYECCIÓN CRÍTICA para el PUT de estado


    // --- ENDPOINT CREAR ORDEN ---
    @PostMapping("/ordenes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> crearOrden(
            @Valid @RequestBody OrdenRequestDTO ordenRequest,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Recibida petición POST /api/ordenes por usuario: {}", userDetails.getUsername());
        try {
            Pedido nuevoPedido = pedidoService.crearNuevaOrden(ordenRequest, userDetails);
            log.info("Orden creada exitosamente con ID: {}", nuevoPedido.getId());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new OrdenResponseDTO(nuevoPedido.getId(),
                            "Pedido recibido exitosamente con ID: " + nuevoPedido.getId()));
        } catch (StockInsuficienteException | ProductoNoEncontradoException e) {
            log.warn("Error de validación al crear orden: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error inesperado al crear orden:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error interno al procesar el pedido. Intente más tarde."));
        }
    }

    // [CRÍTICO - COHESIÓN CON FRONTEND] ENDPOINT PARA OBTENER DETALLES DE UN PEDIDO POR CLIENTE (js/confirmacion.js)
    @GetMapping("/ordenes/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPedidoByIdCliente(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Cliente: Solicitud GET /api/ordenes/{}", id);
        try {
            Pedido pedido = pedidoService.FindByID(id); 
            // Verificación de propiedad para seguridad
            if (!pedido.getCliente().getCorreo().equals(userDetails.getUsername())) {
                log.warn("Intento de acceso no autorizado al pedido ID {} por usuario {}", id, userDetails.getUsername());
                throw new PedidoNoEncontradoException("Pedido no encontrado o no pertenece al usuario.");
            }
            return ResponseEntity.ok(pedido);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener el pedido ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener el pedido."));
        }
    }

    // --- ENDPOINT OBTENER ESTADO ---
    @GetMapping("/ordenes/{id}/estado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getEstadoOrden(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Recibida petición GET /api/ordenes/{}/estado por usuario: {}", id, userDetails.getUsername());
        try {
            String estado = pedidoService.obtenerEstadoPedido(id, userDetails);
            log.info("Estado obtenido para pedido ID {}: {}", id, estado);
            return ResponseEntity.ok(new EstadoResponseDTO(estado));
        } catch (PedidoNoEncontradoException e) {
            log.warn("Pedido no encontrado o sin acceso para usuario {}: {}", userDetails.getUsername(), e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error al obtener estado del pedido ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener el estado del pedido."));
        }
    }

    // --- ENDPOINT OBTENER HISTORIAL ---
    @GetMapping("/clientes/me/historial")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getHistorialPedidos(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("Recibida petición GET /api/clientes/me/historial por usuario: {}", userDetails.getUsername());
        try {
            List<Pedido> historial = pedidoService.obtenerHistorialPedidos(userDetails);
            List<PedidoHistorialDTO> historialDTO = historial.stream()
                    .map(PedidoHistorialDTO::fromEntity)
                    .collect(Collectors.toList());
            log.info("Historial obtenido para usuario {}: {} pedidos.", userDetails.getUsername(), historialDTO.size());
            return ResponseEntity.ok(historialDTO);
        } catch (Exception e) {
            log.error("Error al obtener historial para usuario {}:", userDetails.getUsername(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener el historial de pedidos."));
        }
    }

    // --- Endpoints CRUD y Paneles Internos ---

    // [CRÍTICO - PERMISOS] Permisos para obtener todos los pedidos (Cohesión con Admin, Vendedor y Delivery)
    @GetMapping("/pedidos")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'DELIVERY')") // <<-- CORRECCIÓN DE PERMISOS
    public List<Pedido> getAll() {
        log.info("Admin/Internal: Obteniendo todos los pedidos.");
        return pedidoService.GetAll();
    }

    // [CRÍTICO - COHESIÓN CON FRONTEND] ENDPOINT PARA ACTUALIZAR ESTADO DE PEDIDO (Delivery/Admin)
    @PutMapping("/admin/pedidos/{id}/estado") 
    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY')")
    public ResponseEntity<?> updatePedidoStatus(
            @PathVariable Long id,
            @RequestBody EstadoUpdateRequestDTO estadoRequest) {
        log.info("Admin/Delivery: Actualizando estado del pedido ID: {} a '{}'", id, estadoRequest.getNuevoEstado());
        try {
            // Llama al método que gestiona la lógica de la entidad Estado y actualiza el pedido
            Pedido actualizado = pedidoServiceImpl.actualizarEstadoPedido( 
                    id, 
                    estadoRequest.getNuevoEstado(), 
                    estadoRequest.getNotas()
            );
            return ResponseEntity.ok(actualizado); 
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Admin/Delivery: Error al actualizar estado del pedido ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al actualizar el estado del pedido."));
        }
    }

    @GetMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPedidoByIdAdmin(@PathVariable Long id) {
        log.info("Admin: Obteniendo pedido ID: {}", id);
        try {
            Pedido pedido = pedidoService.FindByID(id);
            return ResponseEntity.ok(pedido);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        }
    }

    @PutMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updatePedidoAdmin(@PathVariable Long id, @RequestBody Pedido pedidoActualizado) {
        log.info("Admin: Actualizando pedido ID: {}", id);
        try {
            Pedido pedidoExistente = pedidoService.FindByID(id); 
            
            // Permite que el administrador actualice el estado directamente.
            if (pedidoActualizado.getEstadoActual() != null) {
                pedidoExistente.setEstadoActual(pedidoActualizado.getEstadoActual());
            }

            Pedido guardado = pedidoService.Save(pedidoExistente); 
            return ResponseEntity.ok(guardado);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Admin: Error al actualizar pedido ID {}:", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al actualizar pedido."));
        }
    }

    @DeleteMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePedidoAdmin(@PathVariable Long id) {
        log.warn("Admin: Solicitando borrado lógico de pedido ID: {}", id);
        try {
            pedidoService.Delete(id);
            log.info("Admin: Borrado lógico de pedido ID {} completado.", id);
            return ResponseEntity.noContent().build();
        } catch (PedidoNoEncontradoException e) {
            log.error("Admin: Error al borrar, pedido ID {} no encontrado.", id);
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Admin: Error inesperado al borrar pedido ID {}:", id, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}