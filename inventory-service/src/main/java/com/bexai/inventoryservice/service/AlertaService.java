package com.bexai.inventoryservice.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.inventoryservice.client.CatalogClient;
import com.bexai.inventoryservice.client.CatalogProductoDto;
import com.bexai.inventoryservice.dto.AlertaRequest;
import com.bexai.inventoryservice.dto.AlertaResponse;
import com.bexai.inventoryservice.exception.RecursoNoEncontradoException;
import com.bexai.inventoryservice.model.Alerta;
import com.bexai.inventoryservice.repository.AlertaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaService {

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final AlertaRepository alertaRepository;
    private final CatalogClient catalogClient;

    @Transactional(readOnly = true)
    public List<AlertaResponse> listar(Boolean atendida) {
        List<Alerta> alertas = (atendida == null)
                ? alertaRepository.findAllByOrderByFechaHoraDesc()
                : alertaRepository.findByAtendidaOrderByFechaHoraDesc(atendida);
        return alertas.stream().map(this::toResponse).toList();
    }

    @Transactional
    public AlertaResponse emitir(AlertaRequest request) {
        String id = (request.getId() != null && !request.getId().isBlank())
                ? request.getId().trim()
                : "ALT-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        String ean13 = request.getEan13();
        String nombreProducto = request.getNombreProducto();

        if (ean13 == null || nombreProducto == null || nombreProducto.isBlank()) {
            try {
                CatalogProductoDto prod = catalogClient.obtenerPorCodigo(request.getSku().trim());
                if (prod != null) {
                    if (ean13 == null) ean13 = prod.getEan13();
                    if (nombreProducto == null || nombreProducto.isBlank()) nombreProducto = prod.getNombre();
                }
            } catch (Exception ex) {
                log.debug("No se pudo enriquecer producto {} desde catalog-service: {}", request.getSku(), ex.getMessage());
            }
        }

        Alerta alerta = new Alerta();
        alerta.setId(id);
        alerta.setTituloAlerta(request.getTituloAlerta().trim());
        alerta.setSku(request.getSku().trim());
        alerta.setEan13(ean13);
        alerta.setNombreProducto(nombreProducto);
        alerta.setStockActual(request.getStockActual());
        alerta.setStockRequerido(request.getStockRequerido());
        alerta.setUbicacionNombre(request.getUbicacionNombre());
        alerta.setSeveridad(request.getSeveridad());
        alerta.setFechaHora(LocalDateTime.now());
        alerta.setAtendida(false);

        log.info("Alerta emitida: {} (sku={})", id, alerta.getSku());
        return toResponse(alertaRepository.save(alerta));
    }

    @Transactional
    public AlertaResponse atender(String id) {
        Alerta alerta = alertaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Alerta no encontrada: " + id));
        alerta.setAtendida(true);
        log.info("Alerta atendida: {}", id);
        return toResponse(alertaRepository.save(alerta));
    }

    private AlertaResponse toResponse(Alerta a) {
        return AlertaResponse.builder()
                .id(a.getId())
                .tituloAlerta(a.getTituloAlerta())
                .sku(a.getSku())
                .ean13(a.getEan13())
                .nombreProducto(a.getNombreProducto())
                .stockActual(a.getStockActual())
                .stockRequerido(a.getStockRequerido())
                .ubicacionNombre(a.getUbicacionNombre())
                .severidad(a.getSeveridad())
                .fechaIso(a.getFechaHora().toLocalDate().format(FECHA_FMT))
                .horaIso(a.getFechaHora().toLocalTime().format(HORA_FMT))
                .fechaHora(a.getFechaHora())
                .atendida(a.getAtendida())
                .build();
    }
}
