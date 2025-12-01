// En: ReporteServiceImpl.java

package com.RutaDelSabor.ruta.services;

import com.RutaDelSabor.ruta.dto.ProductoPopularDTO;
import com.RutaDelSabor.ruta.dto.VentasPeriodoDTO;
import com.RutaDelSabor.ruta.models.dao.IPedidoDAO;
import com.RutaDelSabor.ruta.models.dao.IPedidoDetalladoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReporteServiceImpl implements IReporteService {

    @Autowired private IPedidoDAO pedidoRepository;
    @Autowired private IPedidoDetalladoDAO pedidoDetalladoRepository;

    @Override
    @Transactional(readOnly = true)
    public VentasPeriodoDTO calcularVentasPorPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        // Convertir LocalDate a Date para la consulta
        Date inicio = Date.from(fechaInicio.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fin = Date.from(fechaFin.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        Double totalVentas = pedidoRepository.sumTotalBetweenDates(inicio, fin);
        Long numeroPedidos = pedidoRepository.countBetweenDates(inicio, fin);

        // Manejar nulos si no hay ventas en el período
        double total = (totalVentas != null) ? totalVentas : 0.0;
        long count = (numeroPedidos != null) ? numeroPedidos : 0L;

        return new VentasPeriodoDTO(fechaInicio, fechaFin, total, count);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoPopularDTO> obtenerProductosPopulares(int limite) {
        Pageable topLimit = PageRequest.of(0, limite);
        List<Object[]> resultados = pedidoDetalladoRepository.findTopVentas(topLimit);

        // Mapear el resultado de la consulta (Object[]) al DTO
        return resultados.stream()
            .map(ProductoPopularDTO::new) // Usa el constructor que acepta Object[]
            .collect(Collectors.toList());
    }
}