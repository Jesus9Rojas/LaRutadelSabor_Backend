package com.RutaDelSabor.ruta.dto;

public class OrdenResponseDTO {
    private Long pedidoId;
    private String message;
    // Otros campos si necesitas devolver más info (e.g., total calculado)

    // Constructor, getters, setters
    public OrdenResponseDTO(Long pedidoId, String message) {
        this.pedidoId = pedidoId;
        this.message = message;
    }
     public Long getPedidoId() { return pedidoId; }
    public void setPedidoId(Long pedidoId) { this.pedidoId = pedidoId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}