package com.bexai.authservice.service;

import java.time.LocalDateTime;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.authservice.dto.AuthResponse;
import com.bexai.authservice.dto.LoginRequest;
import com.bexai.authservice.dto.OperadorResponse;
import com.bexai.authservice.exception.OperadorInactivoException;
import com.bexai.authservice.model.Operador;
import com.bexai.authservice.repository.OperadorRepository;
import com.bexai.authservice.security.FlexiblePasswordEncoder;
import com.bexai.authservice.security.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final OperadorRepository operadorRepository;
    private final FlexiblePasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthResponse login(LoginRequest request) {
        log.info("Intento de login para operador: {}", request.getCodigo());

        Operador operador = operadorRepository.findByCodigo(request.getCodigo())
                .orElseThrow(() -> new BadCredentialsException("Código de operador o PIN incorrecto"));

        if (Boolean.FALSE.equals(operador.getActivo())) {
            throw new OperadorInactivoException("El operador " + request.getCodigo() + " se encuentra inactivo");
        }

        if (!passwordEncoder.matches(request.getPin(), operador.getPin())) {
            throw new BadCredentialsException("Código de operador o PIN incorrecto");
        }

        // Actualizar último login
        operador.setUltimoLogin(LocalDateTime.now());
        operadorRepository.save(operador);

        // Generar JWT
        String token = jwtService.generateToken(operador);

        return AuthResponse.builder()
                .token(token)
                .tipo("Bearer")
                .codigo(operador.getCodigo())
                .nombre(operador.getNombre())
                .pNombre(operador.getPNombre())
                .apPaterno(operador.getApPaterno())
                .rol(operador.getRol())
                .sucursalId(operador.getSucursalId())
                .turno(operador.getTurno())
                .build();
    }

    @Transactional(readOnly = true)
    public OperadorResponse getPerfil(String codigo) {
        Operador operador = operadorRepository.findByCodigo(codigo)
                .orElseThrow(() -> new UsernameNotFoundException("Operador no encontrado: " + codigo));

        return toResponse(operador);
    }

    public boolean validarToken(String token) {
        return jwtService.isTokenValid(token);
    }

    private OperadorResponse toResponse(Operador op) {
        return OperadorResponse.builder()
                .id(op.getId())
                .codigo(op.getCodigo())
                .nombre(op.getNombre())
                .pNombre(op.getPNombre())
                .apPaterno(op.getApPaterno())
                .apMaterno(op.getApMaterno())
                .rol(op.getRol())
                .sucursalId(op.getSucursalId())
                .turno(op.getTurno())
                .fechaIngreso(op.getFechaIngreso())
                .ultimoLogin(op.getUltimoLogin())
                .activo(op.getActivo())
                .build();
    }
}
