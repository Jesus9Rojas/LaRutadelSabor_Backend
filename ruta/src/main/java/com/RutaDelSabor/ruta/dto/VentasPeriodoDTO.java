package com.RutaDelSabor.ruta.dto;

import java.time.LocalDate;

public class VentasPeriodoDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private double totalVentas;
    private long numeroPedidos;

    // Constructor vacío para frameworks como Jackson
    public VentasPeriodoDTO() {}

    public VentasPeriodoDTO(LocalDate fechaInicio, LocalDate fechaFin, double totalVentas, long numeroPedidos) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.totalVentas = totalVentas;
        this.numeroPedidos = numeroPedidos;
    }

    // Getters y Setters
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaFin() { return fechaFin; }
    public void setFechaFin(LocalDate fechaFin) { this.fechaFin = fechaFin; }
    public double getTotalVentas() { return totalVentas; }
    public void setTotalVentas(double totalVentas) { this.totalVentas = totalVentas; }
    public long getNumeroPedidos() { return numeroPedidos; }
    public void setNumeroPedidos(long numeroPedidos) { this.numeroPedidos = numeroPedidos; }
}