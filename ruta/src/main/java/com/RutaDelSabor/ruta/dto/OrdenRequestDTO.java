// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/dto/OrdenRequestDTO.java

package com.RutaDelSabor.ruta.dto;

import java.util.List;
import jakarta.validation.constraints.NotEmpty; 

public class OrdenRequestDTO {

    @NotEmpty(message = "La lista de items no puede estar vacía.")
    private List<ItemDTO> items;

    // Datos del cliente
    private String nombreCliente;
    private String apellidoCliente;
    private String correoCliente;
    private String dniCliente;
    private String telefonoCliente; // String por corrección de cohesión
    private String tipoComprobante; 

    // Datos de entrega
    private String tipoEntrega; 
    private String direccionEntrega;
    private String referenciaEntrega;

    // Datos de pago
    private String metodoPago; 
    private String numeroTarjeta;
    private String fechaVencimiento; 
    private String cvv;
    private String titularTarjeta;
    private String numeroYape;
    
    // [CRÍTICO - CAMPO AÑADIDO] Cohesión con js/pago_detalles.js
    private String googlePayToken; 


    // --- GETTERS Y SETTERS ---

    public List<ItemDTO> getItems() { return items; }
    public void setItems(List<ItemDTO> items) { this.items = items; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getApellidoCliente() { return apellidoCliente; }
    public void setApellidoCliente(String apellidoCliente) { this.apellidoCliente = apellidoCliente; }

    public String getCorreoCliente() { return correoCliente; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }

    public String getDniCliente() { return dniCliente; }
    public void setDniCliente(String dniCliente) { this.dniCliente = dniCliente; }

    public String getTelefonoCliente() { return telefonoCliente; }
    public void setTelefonoCliente(String telefonoCliente) { this.telefonoCliente = telefonoCliente; }

    public String getTipoComprobante() { return tipoComprobante; }
    public void setTipoComprobante(String tipoComprobante) { this.tipoComprobante = tipoComprobante; }

    public String getTipoEntrega() { return tipoEntrega; }
    public void setTipoEntrega(String tipoEntrega) { this.tipoEntrega = tipoEntrega; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }

    public String getReferenciaEntrega() { return referenciaEntrega; }
    public void setReferenciaEntrega(String referenciaEntrega) { this.referenciaEntrega = referenciaEntrega; }

    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    public String getNumeroTarjeta() { return numeroTarjeta; }
    public void setNumeroTarjeta(String numeroTarjeta) { this.numeroTarjeta = numeroTarjeta; }

    public String getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(String fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getCvv() { return cvv; }
    public void setCvv(String cvv) { this.cvv = cvv; }

    public String getTitularTarjeta() { return titularTarjeta; }
    public void setTitularTarjeta(String titularTarjeta) { this.titularTarjeta = titularTarjeta; }

    public String getNumeroYape() { return numeroYape; }
    public void setNumeroYape(String numeroYape) { this.numeroYape = numeroYape; }
    
    public String getGooglePayToken() { return googlePayToken; }
    public void setGooglePayToken(String googlePayToken) { this.googlePayToken = googlePayToken; }

}