package com.bexai.inventoryservice.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.inventoryservice.dto.SolicitudEstadoRequest;
import com.bexai.inventoryservice.dto.SolicitudRequest;
import com.bexai.inventoryservice.dto.SolicitudResponse;
import com.bexai.inventoryservice.exception.RecursoNoEncontradoException;
import com.bexai.inventoryservice.model.SolicitudReposicion;
import com.bexai.inventoryservice.repository.SolicitudReposicionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SolicitudReposicionService {

    private final SolicitudReposicionRepository solicitudRepository;

    @Transactional(readOnly = true)
    public List<SolicitudResponse> listarPorSucursal(Long sucursalId) {
        return solicitudRepository.findBySucursalIdOrderByFechaIsoDesc(sucursalId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SolicitudResponse emitir(SolicitudRequest request) {
        SolicitudReposicion solicitud = new SolicitudReposicion();
        solicitud.setSku(request.getSku().trim());
        solicitud.setNombreProducto(request.getNombreProducto().trim());
        solicitud.setCantidad(request.getCantidad());
        solicitud.setDestino(request.getDestino());
        solicitud.setPrioridad(request.getPrioridad());
        solicitud.setObservacion(request.getObservacion());
        solicitud.setEstado("Pendiente");
        solicitud.setFechaIso(LocalDateTime.now());
        solicitud.setSucursalId(request.getSucursalId());

        log.info("Solicitud emitida: sku={}, cantidad={}, sucursal={}",
                solicitud.getSku(), solicitud.getCantidad(), solicitud.getSucursalId());
        return toResponse(solicitudRepository.save(solicitud));
    }

    @Transactional
    public SolicitudResponse actualizarEstado(Long id, SolicitudEstadoRequest request) {
        SolicitudReposicion solicitud = solicitudRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Solicitud no encontrada: " + id));
        solicitud.setEstado(request.getEstado());
        log.info("Solicitud {} -> estado={}", id, request.getEstado());
        return toResponse(solicitudRepository.save(solicitud));
    }

    private SolicitudResponse toResponse(SolicitudReposicion s) {
        return SolicitudResponse.builder()
                .id(s.getId())
                .sku(s.getSku())
                .nombreProducto(s.getNombreProducto())
                .cantidad(s.getCantidad())
                .destino(s.getDestino())
                .prioridad(s.getPrioridad())
                .observacion(s.getObservacion())
                .estado(s.getEstado())
                .fechaIso(s.getFechaIso().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .fechaHora(s.getFechaIso())
                .sucursalId(s.getSucursalId())
                .build();
    }
}
