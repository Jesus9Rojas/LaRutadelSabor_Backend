package com.RutaDelSabor.ruta.security;

import com.RutaDelSabor.ruta.models.dao.IClienteDAO;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IClienteDAO clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // --- CORRECCIÓN AQUÍ ---
        String nombreRol = cliente.getRol().getName().toUpperCase();
        
        // Solo agregamos "ROLE_" si NO lo tiene ya.
        // Como tu BD ya tiene "ROLE_ADMIN", esto evita que quede "ROLE_ROLE_ADMIN"
        if (!nombreRol.startsWith("ROLE_")) {
            nombreRol = "ROLE_" + nombreRol;
        }

        GrantedAuthority authority = new SimpleGrantedAuthority(nombreRol);
        // -----------------------

        return new User(cliente.getCorreo(), cliente.getContraseña(), Collections.singletonList(authority));
    }
}