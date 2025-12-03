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
import com.RutaDelSabor.ruta.services.PedidoServiceImpl; 
import jakarta.validation.Valid;

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
    private PedidoServiceImpl pedidoServiceImpl; 

    @PostMapping("/ordenes")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> crearOrden(@Valid @RequestBody OrdenRequestDTO ordenRequest, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Pedido nuevoPedido = pedidoService.crearNuevaOrden(ordenRequest, userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrdenResponseDTO(nuevoPedido.getId(), "Pedido recibido."));
        } catch (StockInsuficienteException | ProductoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            log.error("Error crear orden:", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error interno."));
        }
    }

    @GetMapping("/ordenes/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPedidoByIdCliente(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Pedido pedido = pedidoService.FindByID(id); 
            if (!pedido.getCliente().getCorreo().equals(userDetails.getUsername())) {
                throw new PedidoNoEncontradoException("No autorizado.");
            }
            return ResponseEntity.ok(pedido);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener pedido."));
        }
    }

    @GetMapping("/ordenes/{id}/estado")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getEstadoOrden(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            String estado = pedidoService.obtenerEstadoPedido(id, userDetails);
            return ResponseEntity.ok(new EstadoResponseDTO(estado));
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error al obtener estado."));
        }
    }

    @GetMapping("/clientes/me/historial")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getHistorialPedidos(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            List<Pedido> historial = pedidoService.obtenerHistorialPedidos(userDetails);
            List<PedidoHistorialDTO> historialDTO = historial.stream().map(PedidoHistorialDTO::fromEntity).collect(Collectors.toList());
            return ResponseEntity.ok(historialDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error historial."));
        }
    }

    // Endpoint crítico para Admin/Vendedor
    @GetMapping("/admin/pedidos")
    @PreAuthorize("hasAnyRole('ADMIN', 'VENDEDOR', 'DELIVERY')")
    public ResponseEntity<List<Pedido>> getAllPedidosAdmin() {
        try {
            List<Pedido> pedidos = pedidoService.GetAll();
            return ResponseEntity.ok(pedidos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/admin/pedidos/{id}/estado") 
    @PreAuthorize("hasAnyRole('ADMIN', 'DELIVERY')")
    public ResponseEntity<?> updatePedidoStatus(@PathVariable Long id, @RequestBody EstadoUpdateRequestDTO estadoRequest) {
        try {
            Pedido actualizado = pedidoServiceImpl.actualizarEstadoPedido(id, estadoRequest.getNuevoEstado(), estadoRequest.getNotas());
            return ResponseEntity.ok(actualizado); 
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error actualizar estado."));
        }
    }

    @GetMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getPedidoByIdAdmin(@PathVariable Long id) {
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
        try {
            Pedido pedidoExistente = pedidoService.FindByID(id); 
            if (pedidoActualizado.getEstadoActual() != null) {
                pedidoExistente.setEstadoActual(pedidoActualizado.getEstadoActual());
            }
            Pedido guardado = pedidoService.Save(pedidoExistente); 
            return ResponseEntity.ok(guardado);
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDTO(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDTO("Error actualizar."));
        }
    }

    @DeleteMapping("/pedidos/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deletePedidoAdmin(@PathVariable Long id) {
        try {
            pedidoService.Delete(id);
            return ResponseEntity.noContent().build();
        } catch (PedidoNoEncontradoException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}