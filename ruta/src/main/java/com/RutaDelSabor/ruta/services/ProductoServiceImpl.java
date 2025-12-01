// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/services/ProductoServiceImpl.java

package com.RutaDelSabor.ruta.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.RutaDelSabor.ruta.exception.ProductoNoEncontradoException;
import com.RutaDelSabor.ruta.exception.StockInsuficienteException; // <--- ¡AQUÍ ESTÁ LA CORRECCIÓN!
import com.RutaDelSabor.ruta.models.dao.IProductoDAO;
import com.RutaDelSabor.ruta.models.entities.Producto;

@Service
public class ProductoServiceImpl implements IProductoService {

    private static final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    @Autowired
    private IProductoDAO productoRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarTodosActivos() {
        log.info("Buscando todos los productos activos.");
        return productoRepository.findByAudAnuladoFalseOrderByProducto();
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarPorIdActivo(Long id) {
        log.info("Buscando producto activo por ID: {}", id);
        return productoRepository.findByIdAndAudAnuladoFalse(id)
                .orElseThrow(() -> {
                    log.error("Producto activo no encontrado con ID: {}", id);
                    return new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Producto buscarPorId(Long id) {
        log.info("Buscando producto (activo o inactivo) por ID: {}", id);
        return productoRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Producto (activo o inactivo) no encontrado con ID: {}", id);
                    return new ProductoNoEncontradoException("Producto no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> buscarTodos() {
        log.info("Buscando todos los productos (incluyendo inactivos).");
        return productoRepository.findAllByOrderByProducto();
    }

    @Override
    @Transactional
    public Producto guardar(Producto producto) {
        boolean esNuevo = producto.getId() == null;
        log.info("{} producto: {}", esNuevo ? "Creando" : "Actualizando", producto.getProducto());

        producto.setAudAnulado(false);

        Producto guardado = productoRepository.save(producto);
        log.info("Producto {} exitosamente con ID: {}", esNuevo ? "creado" : "actualizado", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public void eliminarLogico(Long id) {
        log.warn("Solicitud de eliminación lógica para producto ID: {}", id);
        Producto producto = buscarPorId(id);
        if (!producto.isAudAnulado()) {
            producto.setAudAnulado(true);
            productoRepository.save(producto);
            log.info("Producto ID: {} marcado como anulado.", id);
        } else {
            log.info("Producto ID: {} ya estaba anulado.", id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Producto actualizarStock(Long id, int cantidadComprada) {
        log.info("Actualizando stock para producto ID: {}, cantidad comprada: {}", id, cantidadComprada);
        Producto producto = productoRepository.findByIdAndLock(id)
            .orElseThrow(() -> new ProductoNoEncontradoException("Producto no encontrado o anulado con ID: " + id));

        if (producto.getStock() < cantidadComprada) {
            log.error("Stock insuficiente al actualizar. Producto ID: {}, Stock: {}, Solicitado: {}", id, producto.getStock(), cantidadComprada);
            throw new StockInsuficienteException("Stock insuficiente para " + producto.getProducto());
        }

        producto.setStock(producto.getStock() - cantidadComprada);
        log.info("Stock actualizado para producto ID {}: Nuevo stock = {}", id, producto.getStock());
        return producto;
    }
}