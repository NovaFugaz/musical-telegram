package com.bexai.authservice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import com.bexai.authservice.dto.AuthResponse;
import com.bexai.authservice.dto.LoginRequest;
import com.bexai.authservice.dto.OperadorResponse;
import com.bexai.authservice.enums.Rol;
import com.bexai.authservice.enums.Turno;
import com.bexai.authservice.exception.OperadorInactivoException;
import com.bexai.authservice.model.Operador;
import com.bexai.authservice.repository.OperadorRepository;
import com.bexai.authservice.security.FlexiblePasswordEncoder;
import com.bexai.authservice.security.JwtService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private OperadorRepository operadorRepository;

    @Mock
    private FlexiblePasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private Operador operadorValido;

    @BeforeEach
    void setUp() {
        operadorValido = new Operador();
        operadorValido.setId(1L);
        operadorValido.setCodigo("OP-1001");
        operadorValido.setPin("123456");
        operadorValido.setNombre("Rin Tohsaka");
        operadorValido.setPNombre("Rin");
        operadorValido.setApPaterno("Tohsaka");
        operadorValido.setApMaterno("Tohsaka");
        operadorValido.setRol(Rol.TERRENO);
        operadorValido.setSucursalId(1L);
        operadorValido.setTurno(Turno.TARDE);
        operadorValido.setFechaIngreso(LocalDate.now());
        operadorValido.setUltimoLogin(LocalDateTime.now().minusDays(1));
        operadorValido.setActivo(true);
    }

    @Test
    void login_Exitoso() {
        LoginRequest request = new LoginRequest("OP-1001", "123456");

        when(operadorRepository.findByCodigo("OP-1001")).thenReturn(Optional.of(operadorValido));
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(jwtService.generateToken(operadorValido)).thenReturn("mocked.jwt.token");

        AuthResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("mocked.jwt.token", response.getToken());
        assertEquals("OP-1001", response.getCodigo());
        assertEquals(Rol.TERRENO, response.getRol());
        assertEquals(1L, response.getSucursalId());
        verify(operadorRepository).save(any(Operador.class));
    }

    @Test
    void login_PinIncorrecto_LanzaBadCredentialsException() {
        LoginRequest request = new LoginRequest("OP-1001", "999999");

        when(operadorRepository.findByCodigo("OP-1001")).thenReturn(Optional.of(operadorValido));
        when(passwordEncoder.matches("999999", "123456")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_OperadorNoExiste_LanzaBadCredentialsException() {
        LoginRequest request = new LoginRequest("OP-9999", "123456");

        when(operadorRepository.findByCodigo("OP-9999")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

    @Test
    void login_OperadorInactivo_LanzaOperadorInactivoException() {
        operadorValido.setActivo(false);
        LoginRequest request = new LoginRequest("OP-1001", "123456");

        when(operadorRepository.findByCodigo("OP-1001")).thenReturn(Optional.of(operadorValido));

        assertThrows(OperadorInactivoException.class, () -> authService.login(request));
    }

    @Test
    void getPerfil_Exitoso() {
        when(operadorRepository.findByCodigo("OP-1001")).thenReturn(Optional.of(operadorValido));

        OperadorResponse response = authService.getPerfil("OP-1001");

        assertNotNull(response);
        assertEquals("OP-1001", response.getCodigo());
        assertEquals("Rin Tohsaka", response.getNombre());
    }

    @Test
    void validarToken_Exitoso() {
        when(jwtService.isTokenValid("valid.token")).thenReturn(true);

        boolean valido = authService.validarToken("valid.token");

        assertTrue(valido);
    }
}
