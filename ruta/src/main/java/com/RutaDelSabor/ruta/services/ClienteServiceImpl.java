package com.RutaDelSabor.ruta.services;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.RutaDelSabor.ruta.exception.RecursoNoEncontradoException;
import com.RutaDelSabor.ruta.models.dao.IClienteDAO;
import com.RutaDelSabor.ruta.models.entities.Cliente;

@Service
public class ClienteServiceImpl implements IClienteService {

    private static final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);

    @Autowired
    private IClienteDAO clienteRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodosActivos() {
        log.info("Buscando todos los clientes activos.");
        return clienteRepository.findByAudAnuladoFalseOrderByApellidoAscNombreAsc();
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorIdActivo(Long id) {
        log.info("Buscando cliente activo por ID: {}", id);
        return clienteRepository.findByIdAndAudAnuladoFalse(id)
                .orElseThrow(() -> {
                    log.error("Cliente activo no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
        log.info("Buscando cliente (activo o inactivo) por ID: {}", id);
        return clienteRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Cliente (activo o inactivo) no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public Cliente buscarPorCorreo(String correo) {
        log.info("Buscando cliente (activo o inactivo) por correo: {}", correo);
        return clienteRepository.findByCorreo(correo)
                .orElseThrow(() -> {
                    log.error("Cliente (activo o inactivo) no encontrado con correo: {}", correo);
                    return new RecursoNoEncontradoException("Cliente no encontrado con correo: " + correo);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscarTodos() {
        log.info("Buscando todos los clientes (incluyendo inactivos).");
        return clienteRepository.findAllByOrderByApellidoAscNombreAsc();
    }

    @Override
    @Transactional
    public Cliente guardar(Cliente cliente) {
        // ✅ CORRECCIÓN AQUÍ
        boolean esNuevo = cliente.getId() == null;
        log.info("{} cliente: {}", esNuevo ? "Creando" : "Actualizando", cliente.getCorreo());

        // ✅ CORRECCIÓN AQUÍ (x2)
        if (passwordEncoder != null && cliente.getContraseña() != null && !cliente.getContraseña().startsWith("$2a$")) {
            log.debug("Hasheando contraseña para cliente {}", cliente.getCorreo());
            cliente.setContraseña(passwordEncoder.encode(cliente.getContraseña()));
        }

        cliente.setAudAnulado(false);

        Cliente guardado = clienteRepository.save(cliente);
        log.info("Cliente {} exitosamente con ID: {}", esNuevo ? "creado" : "actualizado", guardado.getId());
        return guardado;
    }

    @Override
    @Transactional
    public void eliminarLogico(Long id) {
        log.warn("Solicitud de eliminación lógica para cliente ID: {}", id);
        Cliente cliente = buscarPorId(id);
        if (!cliente.isAudAnulado()) {
            cliente.setAudAnulado(true);
            clienteRepository.save(cliente);
            log.info("Cliente ID: {} marcado como anulado.", id);
        } else {
            log.info("Cliente ID: {} ya estaba anulado.", id);
        }
    }

    
}