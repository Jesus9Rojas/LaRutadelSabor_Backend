package com.RutaDelSabor.ruta.dto;

public class ItemDTO {
    private Long productoId;
    private int cantidad;

    // Constructor, getters, setters
    public ItemDTO() {}
     public ItemDTO(Long productoId, int cantidad) {
        this.productoId = productoId;
        this.cantidad = cantidad;
    }
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}