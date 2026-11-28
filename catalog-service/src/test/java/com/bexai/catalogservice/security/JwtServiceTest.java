package com.bexai.catalogservice.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.Keys;

import static org.junit.jupiter.api.Assertions.assertThrows;

import io.jsonwebtoken.security.SignatureException;

class JwtServiceTest {

    private static final String SECRET = "3ht7t87MNVRRAmDBBVXkOXBu8I4A4bafN6cB9vQKl0x";

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
    }

    private String crearToken(String secret, String codigo, String rol, long vigenciaMs) {
        return Jwts.builder()
                .subject(codigo)
                .claim("rol", rol)
                .expiration(new Date(System.currentTimeMillis() + vigenciaMs))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    // comprobar token valido
    @Test
    void extractAllClaims_TokenValido_RetornaCodigoYRol() {
        String token = crearToken(SECRET, "OP-1003", "GERENCIA", 60_000);
		
        Claims claims = jwtService.extractAllClaims(token);

        assertEquals("OP-1003", claims.getSubject());
        assertEquals("GERENCIA", claims.get("rol", String.class));

    }

    // token distinto es rechazado
    @Test
    void extractAllClaims_FirmaDeOtroSecret_LanzaSignatureException() {
    String secretAtacante = "12345678901234567890123456789012";
    String tokenFalsificado = crearToken(secretAtacante, "OP-9999", "GERENCIA", 60_000);

    assertThrows(SignatureException.class,
            () -> jwtService.extractAllClaims(tokenFalsificado));
    }

    // token expirado
    @Test
    void extractAllClaims_TokenExpirado_LanzaExpiredJwtException() {
        String tokenExpirado = crearToken(SECRET, "OP-1003", "GERENCIA", -60_000);
        assertThrows(ExpiredJwtException.class,
                () -> jwtService.extractAllClaims(tokenExpirado));

    }

    // Texto que no tiene formato de token es rechazado
    @Test
    void extractAllClaims_TextoQueNoEsToken_LanzaMalformedJwtException() {
        String tokenInvalido = "esto-no-es-un-token";

        assertThrows(MalformedJwtException.class,
                () -> jwtService.extractAllClaims(tokenInvalido));
    }
}
