package com.bexai.inventoryservice.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.inventoryservice.dto.TareaAccionRequest;
import com.bexai.inventoryservice.dto.TareaResponse;
import com.bexai.inventoryservice.service.TareaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/tareas", "/tareas"})
@RequiredArgsConstructor
public class TareaController {

    private final TareaService tareaService;

    @GetMapping
    public ResponseEntity<List<TareaResponse>> listar(
            @RequestParam(required = false) String operadorCodigo,
            @RequestParam(required = false) Long sucursalId,
            Principal principal) {
        String codigo = (operadorCodigo == null && sucursalId == null && principal != null)
                ? principal.getName()
                : operadorCodigo;
        return ResponseEntity.ok(tareaService.listar(codigo, sucursalId));
    }

    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PatchMapping("/{id}/accion")
    public ResponseEntity<TareaResponse> actualizarAccion(
            @PathVariable Long id,
            @Valid @RequestBody TareaAccionRequest request) {
        return ResponseEntity.ok(tareaService.actualizarAccion(id, request));
    }
}
