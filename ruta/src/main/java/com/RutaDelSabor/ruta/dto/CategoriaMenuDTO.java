package com.RutaDelSabor.ruta.dto;

import com.RutaDelSabor.ruta.models.entities.Categoria;
import java.util.List;
import java.util.stream.Collectors;

public class CategoriaMenuDTO {
    private Long id;
    private String nombre; // Usar 'nombre' en lugar de 'Categoria'
    private String icono;
    private List<ProductoMenuDTO> productos;

    // Constructor vacío, constructor con parámetros, getters, setters

     public CategoriaMenuDTO() {}

    public CategoriaMenuDTO(Long id, String nombre, String icono, List<ProductoMenuDTO> productos) {
        this.id = id;
        this.nombre = nombre;
        this.icono = icono;
        this.productos = productos;
    }

    // Getters y Setters...
     public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
     public List<ProductoMenuDTO> getProductos() { return productos; }
    public void setProductos(List<ProductoMenuDTO> productos) { this.productos = productos; }


    // Método estático para convertir Entidad a DTO
    public static CategoriaMenuDTO fromEntity(Categoria categoria) {
        if (categoria == null) return null;
        List<ProductoMenuDTO> productosDTO = categoria.getProductos() == null ? null :
                categoria.getProductos().stream()
                        .map(ProductoMenuDTO::fromEntity)
                        .collect(Collectors.toList());
        return new CategoriaMenuDTO(
                categoria.getId(),
                categoria.getCategoria(), // Usar getCategoria() si no renombraste el campo
                categoria.getIcono(),
                productosDTO
        );
    }
}