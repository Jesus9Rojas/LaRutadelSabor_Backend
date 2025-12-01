package com.RutaDelSabor.ruta.dto;

import java.util.Map;

// Clase principal para mapear el cuerpo de la solicitud de Dialogflow CX
public class DialogflowRequest {
    private SessionInfo sessionInfo;
    private FulfillmentInfo fulfillmentInfo;
    
    // --- Getters y Setters de la clase principal ---
    public SessionInfo getSessionInfo() { return sessionInfo; }
    public void setSessionInfo(SessionInfo sessionInfo) { this.sessionInfo = sessionInfo; }
    public FulfillmentInfo getFulfillmentInfo() { return fulfillmentInfo; }
    public void setFulfillmentInfo(FulfillmentInfo fulfillmentInfo) { this.fulfillmentInfo = fulfillmentInfo; }
    
    // --- Clases Internas ---
    
    public static class SessionInfo {
        private String session; // La ID de la sesión (para el cliente)
        private Map<String, Object> parameters; // Mapa de todos los parámetros de sesión/flujo
        
        public String getSession() { return session; }
        public void setSession(String session) { this.session = session; }
        public Map<String, Object> getParameters() { return parameters; }
        public void setParameters(Map<String, Object> parameters) { this.parameters = parameters; }
    }
    
    public static class FulfillmentInfo {
        private String tag; // Contiene "finalizar_pedido"
        
        public String getTag() { return tag; }
        public void setTag(String tag) { this.tag = tag; }
    }
}