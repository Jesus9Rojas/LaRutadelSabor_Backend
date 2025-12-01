package com.RutaDelSabor.ruta.services;

import com.RutaDelSabor.ruta.dto.LoginRequest;
import com.RutaDelSabor.ruta.dto.RegisterRequest;
import com.RutaDelSabor.ruta.models.dao.IClienteDAO;
import com.RutaDelSabor.ruta.models.dao.IRolDAO;
import com.RutaDelSabor.ruta.models.entities.Cliente;
import com.RutaDelSabor.ruta.models.entities.Rol;
import com.RutaDelSabor.ruta.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails; // Importar UserDetails
import org.springframework.security.core.userdetails.UserDetailsService; // Importar UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService; // Usar UserDetailsServiceImpl

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IClienteDAO clienteRepository; // Repositorio de clientes
    @Autowired
  private IRolDAO rolRepository;

    public Cliente register(RegisterRequest request) {
        // 1. Verificar si el correo ya existe
        if (clienteRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado"); // O una excepción personalizada
        }
        
       // --- AÑADIR ESTA LÓGICA ---
    Rol userRol = rolRepository.findByName("USER") // Asume que tienes un rol "USER" en tu BBDD
        .orElseThrow(() -> new RuntimeException("Error: Rol 'USER' no encontrado."));
    // --- FIN ---

    Cliente nuevoCliente = new Cliente();
    nuevoCliente.setNombre(request.getNombre());
    nuevoCliente.setApellido(request.getApellido());
    nuevoCliente.setCorreo(request.getCorreo());
    nuevoCliente.setTelefono(String.valueOf(request.getTelefono())); // Convertir int a String
    nuevoCliente.setContraseña(passwordEncoder.encode(request.getContraseña()));
    nuevoCliente.setRol(userRol); // ¡ASIGNAR EL ROL!

    return clienteRepository.save(nuevoCliente);
    }
    

    public String login(LoginRequest request) {
        // 1. Autenticar usando Spring Security
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContraseña())
        );

        // 2. Si la autenticación es exitosa, cargar UserDetails
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());

        // 3. Generar y devolver el token JWT
        return jwtUtil.generateToken(userDetails);
    }
}