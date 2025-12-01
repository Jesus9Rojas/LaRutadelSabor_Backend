package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.RutaDelSabor.ruta.models.entities.Comprobante;


public interface IComprobanteDAO extends CrudRepository<Comprobante, Long> {

}
