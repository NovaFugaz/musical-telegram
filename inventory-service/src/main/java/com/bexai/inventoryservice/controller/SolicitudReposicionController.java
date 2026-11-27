package com.bexai.inventoryservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.inventoryservice.dto.SolicitudEstadoRequest;
import com.bexai.inventoryservice.dto.SolicitudRequest;
import com.bexai.inventoryservice.dto.SolicitudResponse;
import com.bexai.inventoryservice.service.SolicitudReposicionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/solicitudes", "/solicitudes"})
@RequiredArgsConstructor
public class SolicitudReposicionController {

    private final SolicitudReposicionService solicitudService;

    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> listar(@RequestParam(defaultValue = "1") Long sucursalId) {
        return ResponseEntity.ok(solicitudService.listarPorSucursal(sucursalId));
    }

    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PostMapping
    public ResponseEntity<SolicitudResponse> emitir(@Valid @RequestBody SolicitudRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(solicitudService.emitir(request));
    }

    @PreAuthorize("hasAnyRole('BODEGA','GERENCIA')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<SolicitudResponse> actualizarEstado(
            @PathVariable Long id,
            @Valid @RequestBody SolicitudEstadoRequest request) {
        return ResponseEntity.ok(solicitudService.actualizarEstado(id, request));
    }
}
