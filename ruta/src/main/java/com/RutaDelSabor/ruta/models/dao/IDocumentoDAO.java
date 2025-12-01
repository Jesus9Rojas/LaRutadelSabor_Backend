package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.RutaDelSabor.ruta.models.entities.Documento;

public interface IDocumentoDAO extends CrudRepository<Documento, Long> {}
