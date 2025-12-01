package com.RutaDelSabor.ruta.config;
import com.RutaDelSabor.ruta.security.UserDetailsServiceImpl; // <--- AÑADIR IMPORTACIÓN
import org.springframework.security.authentication.AuthenticationProvider; // <--- AÑADIR IMPORTACIÓN
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // <--- AÑADIR IMPORTACIÓN
import com.RutaDelSabor.ruta.security.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // Para especificar métodos HTTP
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // Para @PreAuthorize
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsServiceImpl userDetailsService; // <-- AÑADIDO

    // Define el codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Expone el AuthenticationManager como un Bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // --- ESTE ES EL BEAN QUE FALTABA ---
    // Le dice a Spring Security CÓMO autenticar
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        // 1. Le dice cuál es el servicio para buscar usuarios
        authProvider.setUserDetailsService(userDetailsService);
        // 2. Le dice cuál es el codificador para comparar contraseñas
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    // Define la cadena de filtros de seguridad principal
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // ... (tus reglas de requestMatchers están bien) ...
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/menu").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/webhook/dialogflow").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/categorias/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // --- ESTA ES LA LÍNEA QUE FALTA ---
                // 3. Le dice a HttpSecurity que use el proveedor que acabas de crear
                .authenticationProvider(authenticationProvider()); // <-- AÑADIDO

        // Añade nuestro filtro JWT
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}