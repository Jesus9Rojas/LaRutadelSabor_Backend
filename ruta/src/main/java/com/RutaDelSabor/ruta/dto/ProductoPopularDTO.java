package com.RutaDelSabor.ruta.dto;

public class ProductoPopularDTO {
    private Long productoId;
    private String nombreProducto;
    private long cantidadVendida;

    // Constructor vacío
    public ProductoPopularDTO() {}

    public ProductoPopularDTO(Long productoId, String nombreProducto, long cantidadVendida) {
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
    }

    // Constructor para resultados de query (Object[])
    public ProductoPopularDTO(Object[] result) {
         if (result != null && result.length >= 3) {
             // Ajusta los índices y tipos según tu query JPQL/SQL
             this.productoId = (result[0] instanceof Number) ? ((Number) result[0]).longValue() : null;
             this.nombreProducto = (result[1] instanceof String) ? (String) result[1] : null;
             this.cantidadVendida = (result[2] instanceof Number) ? ((Number) result[2]).longValue() : 0L;
         }
    }

    // Getters y Setters
    public Long getProductoId() { return productoId; }
    public void setProductoId(Long productoId) { this.productoId = productoId; }
    public String getNombreProducto() { return nombreProducto; }
    public void setNombreProducto(String nombreProducto) { this.nombreProducto = nombreProducto; }
    public long getCantidadVendida() { return cantidadVendida; }
    public void setCantidadVendida(long cantidadVendida) { this.cantidadVendida = cantidadVendida; }
}