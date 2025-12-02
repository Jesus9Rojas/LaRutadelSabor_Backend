package com.RutaDelSabor.ruta.controllers;

import com.RutaDelSabor.ruta.dto.*;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import com.RutaDelSabor.ruta.models.entities.Producto;
import com.RutaDelSabor.ruta.services.IClienteService;
import com.RutaDelSabor.ruta.services.IPedidoService;
import com.RutaDelSabor.ruta.services.IProductoService;
import com.RutaDelSabor.ruta.services.IReporteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/webhook")
public class WebhookController {

    private static final Logger log = LoggerFactory.getLogger(WebhookController.class);

    @Autowired private IPedidoService pedidoService;
    @Autowired private IClienteService clienteService;
    @Autowired private IProductoService productoService;
    @Autowired private IReporteService reporteService;

    @PostMapping("/dialogflow")
    public ResponseEntity<Map<String, Object>> handleWebhook(@RequestBody DialogflowRequest request) {
        String tag = request.getFulfillmentInfo().getTag();
        Map<String, Object> params = request.getSessionInfo().getParameters();
        
        // CORRECCIÓN 1: Eliminada variable 'response' que no se usaba
        log.info("Webhook llamado con Tag: {}", tag);

        if ("finalizar_pedido".equals(tag)) {
            return processFinalizarPedido(params);
        } else if ("recomendar_producto".equals(tag)) {
            return processRecomendacion();
        } else if ("consultar_historial".equals(tag)) {
             return processHistorial(params);
        }

        return ResponseEntity.ok(crearRespuestaTexto("Webhook: Acción no reconocida."));
    }

    // --- 1. LÓGICA DE PEDIDOS DINÁMICOS ---
    private ResponseEntity<Map<String, Object>> processFinalizarPedido(Map<String, Object> params) {
        try {
            // 1. Obtener datos del cliente
            String email = (String) params.get("email_cliente");
            Cliente cliente = clienteService.buscarPorCorreo(email);

            // 2. Procesar productos
            String nombreProducto = (String) params.get("producto_solicitado"); 
            Integer cantidad = params.get("cantidad") != null ? ((Double) params.get("cantidad")).intValue() : 1;

            // Buscar producto real en BD
            Optional<Producto> prodOpt = productoService.buscarTodosActivos().stream()
                .filter(p -> p.getProducto().toLowerCase().contains(nombreProducto.toLowerCase()))
                .findFirst();

            if (prodOpt.isEmpty()) {
                return ResponseEntity.ok(crearRespuestaTexto("Lo siento, no encontré el producto: " + nombreProducto));
            }

            Producto productoReal = prodOpt.get();

            // 3. Crear DTO de Orden
            OrdenRequestDTO ordenRequest = new OrdenRequestDTO();
            ordenRequest.setItems(List.of(new ItemDTO(productoReal.getId(), cantidad)));
            ordenRequest.setDireccionEntrega((String) params.get("direccion"));
            ordenRequest.setMetodoPago("Pendiente Web");

            // 4. Crear la orden "Pendiente"
            Pedido nuevoPedido = pedidoService.crearNuevaOrden(ordenRequest, 
                new User(cliente.getCorreo(), cliente.getContraseña(), new ArrayList<>()));

            // 5. RESPUESTA ESPECIAL (PAYLOAD)
            Map<String, Object> jsonResponse = new HashMap<>();
            
            // CORRECCIÓN 2: Eliminada variable 'sessionInfo' que no se usaba

            // Enviamos un "Payload Personalizado" que tu Frontend (JS) leerá para redirigir
            List<Map<String, Object>> responseMessages = new ArrayList<>();
            Map<String, Object> payloadContainer = new HashMap<>();
            Map<String, Object> customPayload = new HashMap<>();
            
            customPayload.put("accion", "REDIRIGIR_PAGO");
            customPayload.put("url", "/checkout?ordenId=" + nuevoPedido.getId());
            customPayload.put("mensaje", "Pedido creado. Redirigiendo a pasarela de pago...");
            
            payloadContainer.put("payload", customPayload);
            responseMessages.add(payloadContainer);

            // Texto de respaldo
            Map<String, Object> textContainer = new HashMap<>();
            textContainer.put("text", Map.of("text", List.of("¡Listo! Tu orden #" + nuevoPedido.getId() + " está creada. Procede al pago.")));
            responseMessages.add(textContainer);

            jsonResponse.put("fulfillmentResponse", Map.of("messages", responseMessages));
            return ResponseEntity.ok(jsonResponse);

        } catch (Exception e) {
            log.error("Error webhook:", e);
            return ResponseEntity.ok(crearRespuestaTexto("Error procesando tu pedido: " + e.getMessage()));
        }
    }

    // --- 2. LÓGICA DE RECOMENDACIONES (MINERÍA SIMPLE) ---
    private ResponseEntity<Map<String, Object>> processRecomendacion() {
        List<ProductoPopularDTO> populares = reporteService.obtenerProductosPopulares(3);
        
        String texto = "Te recomiendo probar nuestros favoritos: ";
        texto += populares.stream()
                .map(p -> p.getNombreProducto())
                .collect(Collectors.joining(", "));

        return ResponseEntity.ok(crearRespuestaTexto(texto + ". ¿Te provoca alguno?"));
    }
    
    // --- 3. HISTORIAL DE USUARIO ---
    private ResponseEntity<Map<String, Object>> processHistorial(Map<String, Object> params) {
         // Lógica pendiente: devolver el último pedido en texto.
         return ResponseEntity.ok(crearRespuestaTexto("Aquí iría tu último pedido."));
    }

    // Helper para respuestas simples de texto
    private Map<String, Object> crearRespuestaTexto(String mensaje) {
        Map<String, Object> json = new HashMap<>();
        Map<String, Object> fulfillment = new HashMap<>();
        Map<String, Object> text = new HashMap<>();
        text.put("text", Collections.singletonList(mensaje));
        fulfillment.put("messages", Collections.singletonList(Map.of("text", text)));
        json.put("fulfillmentResponse", fulfillment);
        return json;
    }
}