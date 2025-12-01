package com.RutaDelSabor.ruta.services;

import com.RutaDelSabor.ruta.dto.ProductoPopularDTO; // Crear este DTO
import com.RutaDelSabor.ruta.dto.VentasPeriodoDTO; // Crear este DTO
import java.time.LocalDate;
import java.util.List;

public interface IReporteService {
    VentasPeriodoDTO calcularVentasPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin);
    List<ProductoPopularDTO> obtenerProductosPopulares(int limite); // Límite de top productos
}