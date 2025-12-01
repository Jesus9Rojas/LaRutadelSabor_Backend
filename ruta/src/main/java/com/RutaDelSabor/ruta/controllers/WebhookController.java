package com.RutaDelSabor.ruta.controllers;

import com.RutaDelSabor.ruta.dto.DialogflowRequest;
import com.RutaDelSabor.ruta.dto.DialogflowResponse;
import com.RutaDelSabor.ruta.dto.OrdenRequestDTO;
import com.RutaDelSabor.ruta.dto.ItemDTO;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import com.RutaDelSabor.ruta.services.IPedidoService;
import com.RutaDelSabor.ruta.services.IClienteService;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);
    
    @Autowired private IPedidoService pedidoService;
    @Autowired private IClienteService clienteService; // Necesario para obtener el cliente por correo

    // [CRÍTICO] Endpoint que Dialogflow llamará
    @PostMapping("/dialogflow")
    public ResponseEntity<DialogflowResponse> handleWebhook(@RequestBody DialogflowRequest request) {
        
        String tag = request.getFulfillmentInfo().getTag();
        log.info("Webhook llamado con Tag: {}", tag);
        
        if ("finalizar_pedido".equals(tag)) {
            return processFinalizarPedido(request);
        } else if ("consultar_menu".equals(tag)) {
             // Aquí iría la lógica para consultar el menú dinámico
             return ResponseEntity.ok(new DialogflowResponse("¡Claro! El menú de hoy tiene X promociones. Pregunta por ellas."));
        }
        
        // Respuesta por defecto si el tag no se reconoce
        return ResponseEntity.ok(new DialogflowResponse("Webhook: No se reconoció la acción solicitada."));
    }

    private ResponseEntity<DialogflowResponse> processFinalizarPedido(DialogflowRequest request) {
        Map<String, Object> params = request.getSessionInfo().getParameters();
        String fulfillmentText;
        
        // 1. Recolección de Parámetros
        String nombre = (String) params.get("nombre_cliente");
        String telefono = (String) params.get("telefono_cliente");
        String direccion = (String) params.get("direccion");
        String tipoOrden = (String) params.get("Tipo_Orden");
        String metodoPago = (String) params.get("Metodo_Pago");
        // [ATENCIÓN CRÍTICA] Debes asegurar que la lista de items se guarde en Dialogflow bajo un parámetro
        // que puedas parsear aquí (ej. un parámetro llamado 'lista_items_json' o similar)
        // Por ahora, asumimos que el Agente no colecta items completos en este flujo simple.
        
        try {
            // ** SIMULACIÓN DE CLIENTE (Asumimos que el cliente existe o es anónimo temporal) **
            // Para la prueba local, asumimos un cliente de prueba, ya que Dialogflow no autentica.
            // Si el cliente estuviera logueado, usarías la sesión del widget para autenticarlo.
            Cliente cliente;
            try {
                 // ** Asume que tienes un cliente de prueba con un correo conocido **
                 cliente = clienteService.buscarPorCorreo("gabaledan@gmail.com"); // Reemplaza por un correo real de tu DB
            } catch (UsernameNotFoundException e) {
                // Si no se encuentra el cliente de prueba, no se puede crear el pedido.
                 throw new UsernameNotFoundException("Cliente de prueba no encontrado en la DB.");
            }
            
            // 2. Mapeo a OrdenRequestDTO (Simplificado, se asume 1 item estático por la limitación del agente)
            OrdenRequestDTO ordenRequest = new OrdenRequestDTO();
            // Esto fallará si la lógica de items no está en Dialogflow, pero es el esqueleto necesario:
            ordenRequest.setItems(new ArrayList<>(List.of(
                 new ItemDTO(1L, 1) // ID del producto 'Hamburguesa Clásica' (ASUNCIÓN) y cantidad 1
            )));
            
            ordenRequest.setNombreCliente(nombre);
            ordenRequest.setApellidoCliente("AI"); // Apellido de relleno para el DTO
            ordenRequest.setCorreoCliente(cliente.getCorreo());
            ordenRequest.setTelefonoCliente(telefono);
            ordenRequest.setTipoComprobante("Boleta");
            ordenRequest.setTipoEntrega(tipoOrden.replace("_", " ")); // delivery -> Delivery, recojo_local -> Recojo en Local
            ordenRequest.setDireccionEntrega("delivery".equals(tipoOrden) ? direccion : "MegaPlaza Ica");
            ordenRequest.setMetodoPago(metodoPago);
            
            // 3. Creación del Pedido (Llamada al servicio)
            // Usamos un UserDetails Dummy para crear la orden, basado en el cliente encontrado
            Pedido nuevoPedido = pedidoService.crearNuevaOrden(ordenRequest, new org.springframework.security.core.userdetails.User(cliente.getCorreo(), cliente.getContraseña(), new ArrayList<>()));
            
            // 4. Respuesta a Dialogflow
            fulfillmentText = String.format(
                "¡Pedido #%d registrado! Lo enviamos a %s. Total: S/ %.2f. ¡Gracias por tu compra!", 
                nuevoPedido.getId(), 
                "delivery".equals(tipoOrden) ? direccion : "el local",
                nuevoPedido.getTotal().doubleValue()
            );

        } catch (Exception e) {
            log.error("Error al procesar el pedido del Webhook:", e);
            fulfillmentText = "Lo siento, hubo un error crítico al registrar tu pedido. Por favor, llámanos para ordenar.";
        }

        return ResponseEntity.ok(new DialogflowResponse(fulfillmentText));
    }
}