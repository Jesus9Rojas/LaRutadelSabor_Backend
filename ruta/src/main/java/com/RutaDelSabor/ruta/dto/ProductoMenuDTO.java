package com.RutaDelSabor.ruta.dto;

import java.math.BigDecimal;

import com.RutaDelSabor.ruta.models.entities.Producto;

import jakarta.persistence.Column;

public class ProductoMenuDTO {
    private Long id;
    private String nombre; // Usar 'nombre' en lugar de 'Producto'
    private String descripcion;
    @Column(precision = 10, scale = 2)
    private BigDecimal precio;
    private String imagen;

    // Constructor vacío, constructor con parámetros, getters, setters

    public ProductoMenuDTO() {}

     public ProductoMenuDTO(Long id, String nombre, String descripcion, BigDecimal precio, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters y Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
     public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }
    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }


    // Método estático para convertir Entidad a DTO
    public static ProductoMenuDTO fromEntity(Producto producto) {
        if (producto == null) return null;
        return new ProductoMenuDTO(
                producto.getId(),
                producto.getProducto(), // Usar getProducto() si no renombraste el campo
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getImagen()
        );
    }
}