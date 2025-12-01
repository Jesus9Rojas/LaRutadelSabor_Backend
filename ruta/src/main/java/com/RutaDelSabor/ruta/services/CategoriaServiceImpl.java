package com.RutaDelSabor.ruta.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger; // Para logging
import org.slf4j.LoggerFactory; // Para logging
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.RutaDelSabor.ruta.models.dao.ICategoriaDAO;
import com.RutaDelSabor.ruta.models.entities.Categoria;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

    private static final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);

    @Autowired
    private ICategoriaDAO categoriaRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> GetAll() {
         // Usar el método que filtra anulados si lo creaste
         // return categoriaRepository.findAllActive();
        log.info("Obteniendo todas las categorías (incluyendo anuladas).");
        List<Categoria> list = new ArrayList<>();
        categoriaRepository.findAll().forEach(list::add);
        return list;
    }

    @Override
    @Transactional
    public Categoria Save(Categoria object) {
         log.info("Guardando categoría: {}", object.getCategoria()); // Ajusta si renombraste
         if (object.getId() == null) { object.setCreatedAt(new java.util.Date()); }
         object.setUpdatedAt(new java.util.Date());
         object.setAudAnulado(false); // Asegurar que no esté anulada al guardar/actualizar
        return categoriaRepository.save(object);
    }

    @Override
    @Transactional(readOnly = true)
    public Categoria FindByID(Long id) {
         log.info("Buscando categoría por ID: {}", id);
         // Devuelve null si no existe, podrías lanzar una excepción
        return categoriaRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public void Delete(Long id) {
         log.warn("Realizando borrado lógico de categoría ID: {}", id);
         Optional<Categoria> optionalCategoria = categoriaRepository.findById(id);
         if (optionalCategoria.isPresent()) {
             Categoria categoria = optionalCategoria.get();
             categoria.setAudAnulado(true); // Borrado lógico
             categoria.setUpdatedAt(new java.util.Date());
             categoriaRepository.save(categoria);
             log.info("Categoría ID: {} marcada como anulada.", id);
         } else {
            log.error("No se encontró categoría con ID: {} para borrar.", id);
            // Podrías lanzar una excepción aquí
         }
        // categoriaRepository.deleteById(id); // Borrado físico
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categoria> GetAllWithProducts() {
         log.info("Obteniendo menú: categorías con productos activos.");
        return categoriaRepository.findAllWithProducts();
    }
}