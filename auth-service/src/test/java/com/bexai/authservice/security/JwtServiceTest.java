package com.bexai.authservice.security;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.bexai.authservice.enums.Rol;
import com.bexai.authservice.enums.Turno;
import com.bexai.authservice.model.Operador;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        // Clave secreta de prueba con longitud suficiente para HS256
        ReflectionTestUtils.setField(jwtService, "secretKey", "EstaEsUnaClaveSecretaSuperLargaYSeguraParaPruebas1234567890!");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", 3600000L); // 1 hora
    }

    @Test
    void generateToken_Y_ValidarToken_Exitoso() {
        Operador operador = new Operador();
        operador.setCodigo("OP-1001");
        operador.setNombre("Rin Tohsaka");
        operador.setRol(Rol.TERRENO);
        operador.setSucursalId(1L);
        operador.setTurno(Turno.TARDE);
        operador.setFechaIngreso(LocalDate.now());
        operador.setActivo(true);

        String token = jwtService.generateToken(operador);

        assertNotNull(token);
        assertTrue(jwtService.isTokenValid(token));
        assertEquals("OP-1001", jwtService.extractCodigo(token));
        assertTrue(jwtService.isTokenValid(token, "OP-1001"));
        assertFalse(jwtService.isTokenValid(token, "OP-9999"));
    }
}
