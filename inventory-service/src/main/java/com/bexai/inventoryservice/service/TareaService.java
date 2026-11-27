package com.bexai.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.inventoryservice.dto.TareaAccionRequest;
import com.bexai.inventoryservice.dto.TareaResponse;
import com.bexai.inventoryservice.exception.RecursoNoEncontradoException;
import com.bexai.inventoryservice.model.Tarea;
import com.bexai.inventoryservice.repository.TareaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TareaService {

    private final TareaRepository tareaRepository;

    @Transactional(readOnly = true)
    public List<TareaResponse> listar(String operadorCodigo, Long sucursalId) {
        List<Tarea> tareas;
        if (operadorCodigo != null && !operadorCodigo.isBlank()) {
            tareas = tareaRepository.findByOperadorCodigoOrderByIdAsc(operadorCodigo.trim());
        } else if (sucursalId != null) {
            tareas = tareaRepository.findBySucursalIdOrderByIdAsc(sucursalId);
        } else {
            tareas = tareaRepository.findAll();
        }
        return tareas.stream().map(this::toResponse).toList();
    }

    @Transactional
    public TareaResponse actualizarAccion(Long id, TareaAccionRequest request) {
        Tarea tarea = tareaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Tarea no encontrada: " + id));
        tarea.setAccion(request.getAccion());
        log.info("Tarea {} -> accion={}", id, request.getAccion());
        return toResponse(tareaRepository.save(tarea));
    }

    private TareaResponse toResponse(Tarea t) {
        return TareaResponse.builder()
                .id(t.getId())
                .operadorCodigo(t.getOperadorCodigo())
                .titulo(t.getTitulo())
                .detalle(t.getDetalle())
                .accion(t.getAccion())
                .tipo(t.getTipo())
                .sucursalId(t.getSucursalId())
                .build();
    }
}
