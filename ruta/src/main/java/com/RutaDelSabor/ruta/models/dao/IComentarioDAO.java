package com.RutaDelSabor.ruta.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.RutaDelSabor.ruta.models.entities.Comentario;

public interface IComentarioDAO extends CrudRepository<Comentario, Long>{

}
