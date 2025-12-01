package com.RutaDelSabor.ruta.controllers;

import com.RutaDelSabor.ruta.dto.ErrorResponseDTO;
import com.RutaDelSabor.ruta.dto.ProductoPopularDTO; // Importar DTOs
import com.RutaDelSabor.ruta.dto.VentasPeriodoDTO;
import com.RutaDelSabor.ruta.services.IReporteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat; // Para parsear fechas
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Proteger endpoints
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/admin/reportes") // Ruta base para reportes
@PreAuthorize("hasRole('ADMIN')") // Requiere rol ADMIN para todos los métodos
// @CrossOrigin(origins = "*") // Mejor en WebConfig
public class ReporteController {

    private static final Logger log = LoggerFactory.getLogger(ReporteController.class);

    @Autowired
    private IReporteService reporteService;

    @GetMapping("/ventas")
    public ResponseEntity<?> getReporteVentas(
            @RequestParam("inicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        log.info("Admin: Solicitando reporte de ventas entre {} y {}", fechaInicio, fechaFin);
        try {
            if (fechaInicio.isAfter(fechaFin)) {
                return ResponseEntity.badRequest().body(new ErrorResponseDTO("La fecha de inicio no puede ser posterior a la fecha de fin"));
            }
            VentasPeriodoDTO reporte = reporteService.calcularVentasPorPeriodo(fechaInicio, fechaFin);
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
             log.error("Admin: Error al generar reporte de ventas:", e);
            return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al generar reporte de ventas"));
        }
    }

    @GetMapping("/productos-populares")
    public ResponseEntity<?> getProductosPopulares(
            @RequestParam(value = "limite", defaultValue = "10") int limite) { // Límite por defecto
         log.info("Admin: Solicitando top {} productos populares", limite);
        try {
            if (limite <= 0) limite = 10; // Asegurar límite positivo
            List<ProductoPopularDTO> topProductos = reporteService.obtenerProductosPopulares(limite);
            return ResponseEntity.ok(topProductos);
        } catch (Exception e) {
             log.error("Admin: Error al generar reporte de productos populares:", e);
            return ResponseEntity.internalServerError().body(new ErrorResponseDTO("Error al generar reporte de productos populares"));
        }
    }
}