package com.RutaDelSabor.ruta.dto;

public class AuthResponse {
    private String token;
    private String message; // Opcional

    public AuthResponse(String token) {
        this.token = token;
    }
     public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
     public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}