package com.RutaDelSabor.ruta.services;

import java.util.List;
import com.RutaDelSabor.ruta.models.entities.Pedido;
import com.RutaDelSabor.ruta.dto.OrdenRequestDTO;
import org.springframework.security.core.userdetails.UserDetails; // Para obtener usuario autenticado

public interface IPedidoService {
    List<Pedido> GetAll(); // Probablemente solo para admin
    Pedido Save(Pedido object);
    Pedido FindByID(Long id);
    void Delete(Long id); // Cambiado para recibir ID

    // Métodos de Negocio
    Pedido crearNuevaOrden(OrdenRequestDTO ordenRequest, UserDetails userDetails); // Recibe UserDetails
    String obtenerEstadoPedido(Long pedidoId, UserDetails userDetails); // Recibe UserDetails para validación
    List<Pedido> obtenerHistorialPedidos(UserDetails userDetails); // Para historial de cliente
}