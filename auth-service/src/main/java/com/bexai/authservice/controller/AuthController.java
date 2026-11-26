package com.bexai.authservice.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.authservice.dto.AuthResponse;
import com.bexai.authservice.dto.LoginRequest;
import com.bexai.authservice.dto.OperadorResponse;
import com.bexai.authservice.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint de inicio de sesión con código y PIN.
     * Mapeado tanto a /login como a /api/auth/login para compatibilidad directa o vía Gateway.
     */
    @PostMapping({"/login", "/api/auth/login"})
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    /**
     * Obtiene los datos del operador actualmente autenticado a partir del token JWT.
     */
    @GetMapping({"/me", "/api/auth/me"})
    public ResponseEntity<OperadorResponse> getPerfil(Principal principal) {
        return ResponseEntity.ok(authService.getPerfil(principal.getName()));
    }

    /**
     * Valida si un token JWT continúa activo y no ha expirado.
     */
    @PostMapping({"/validar-token", "/api/auth/validar-token"})
    public ResponseEntity<Map<String, Object>> validarToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(Map.of("valido", false, "mensaje", "Cabecera Authorization no presente o inválida"));
        }
        String token = authHeader.substring(7);
        boolean valido = authService.validarToken(token);
        return ResponseEntity.ok(Map.of("valido", valido));
    }
}
