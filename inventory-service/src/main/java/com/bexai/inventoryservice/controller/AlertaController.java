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

import com.bexai.inventoryservice.dto.AlertaRequest;
import com.bexai.inventoryservice.dto.AlertaResponse;
import com.bexai.inventoryservice.service.AlertaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/alertas", "/alertas"})
@RequiredArgsConstructor
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    public ResponseEntity<List<AlertaResponse>> listar(@RequestParam(required = false) Boolean atendida) {
        return ResponseEntity.ok(alertaService.listar(atendida));
    }

    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PostMapping
    public ResponseEntity<AlertaResponse> emitir(@Valid @RequestBody AlertaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(alertaService.emitir(request));
    }

    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PatchMapping("/{id}/atender")
    public ResponseEntity<AlertaResponse> atender(@PathVariable String id) {
        return ResponseEntity.ok(alertaService.atender(id));
    }
}
