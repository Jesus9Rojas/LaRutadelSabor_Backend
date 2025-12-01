// Archivo: ruta/src/main/java/com/RutaDelSabor/ruta/security/UserDetailsServiceImpl.java

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
// import java.util.ArrayList; // <--- ESTA LÍNEA SE HA ELIMINADO

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IClienteDAO clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByCorreo(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con correo: " + username));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + cliente.getRol().getName().toUpperCase());

        return new User(cliente.getCorreo(), cliente.getContraseña(), Collections.singletonList(authority));
    }
}