package com.RutaDelSabor.ruta.dto;

// Clase para mapear la respuesta de texto que Dialogflow CX espera de vuelta
public class DialogflowResponse {
    private String fulfillmentText;
    
    public DialogflowResponse() {}
    public DialogflowResponse(String fulfillmentText) {
        this.fulfillmentText = fulfillmentText;
    }
    
    public String getFulfillmentText() { return fulfillmentText; }
    public void setFulfillmentText(String fulfillmentText) { this.fulfillmentText = fulfillmentText; }
}