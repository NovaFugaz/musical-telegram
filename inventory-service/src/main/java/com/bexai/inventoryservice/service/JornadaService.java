package com.bexai.inventoryservice.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.inventoryservice.dto.JornadaRequest;
import com.bexai.inventoryservice.dto.JornadaResponse;
import com.bexai.inventoryservice.exception.RecursoNoEncontradoException;
import com.bexai.inventoryservice.model.Jornada;
import com.bexai.inventoryservice.repository.JornadaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class JornadaService {

    private final JornadaRepository jornadaRepository;

    @Transactional(readOnly = true)
    public JornadaResponse obtenerActiva(String operadorCodigo) {
        Jornada jornada = jornadaRepository
                .findFirstByOperadorCodigoAndFechaFinIsNullOrderByFechaInicioDesc(operadorCodigo.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay jornada activa para el operador: " + operadorCodigo));
        return toResponse(jornada);
    }

    @Transactional
    public JornadaResponse iniciar(JornadaRequest request, String operadorAutenticado) {
        String codigo = (request.getOperadorCodigo() != null && !request.getOperadorCodigo().isBlank())
                ? request.getOperadorCodigo().trim()
                : operadorAutenticado;

        // Cerrar cualquier jornada abierta previa para mantener consistencia
        jornadaRepository.findFirstByOperadorCodigoAndFechaFinIsNullOrderByFechaInicioDesc(codigo)
                .ifPresent(previa -> {
                    previa.setFechaFin(LocalDateTime.now());
                    jornadaRepository.save(previa);
                });

        Jornada nueva = new Jornada();
        nueva.setOperadorCodigo(codigo);
        nueva.setSucursalId(request.getSucursalId());
        nueva.setTurno(request.getTurno());
        nueva.setFechaInicio(LocalDateTime.now());

        log.info("Jornada iniciada: operador={}, sucursal={}, turno={}", codigo, request.getSucursalId(), request.getTurno());
        return toResponse(jornadaRepository.save(nueva));
    }

    @Transactional
    public JornadaResponse terminar(String operadorCodigo) {
        Jornada jornada = jornadaRepository
                .findFirstByOperadorCodigoAndFechaFinIsNullOrderByFechaInicioDesc(operadorCodigo.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay jornada activa para finalizar del operador: " + operadorCodigo));
        jornada.setFechaFin(LocalDateTime.now());
        log.info("Jornada finalizada: operador={}", operadorCodigo);
        return toResponse(jornadaRepository.save(jornada));
    }

    private JornadaResponse toResponse(Jornada j) {
        return JornadaResponse.builder()
                .id(j.getId())
                .operadorCodigo(j.getOperadorCodigo())
                .sucursalId(j.getSucursalId())
                .turno(j.getTurno())
                .fechaInicio(j.getFechaInicio())
                .fechaFin(j.getFechaFin())
                .activa(j.getFechaFin() == null)
                .build();
    }
}
