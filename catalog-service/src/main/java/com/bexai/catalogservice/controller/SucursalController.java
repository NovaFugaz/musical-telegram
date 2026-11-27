package com.bexai.catalogservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.catalogservice.dto.SucursalRequest;
import com.bexai.catalogservice.dto.SucursalResponse;
import com.bexai.catalogservice.service.SucursalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({ "/api/sucursales", "/sucursales" })
@RequiredArgsConstructor
public class SucursalController {

    private final SucursalService sucursalService;

    // listar todas las sucursales
    @GetMapping
    public ResponseEntity<List<SucursalResponse>> listar() {
        return ResponseEntity.ok(sucursalService.listar());
    }

    // sucursal por id
    @GetMapping("/{id}")
    public ResponseEntity<SucursalResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(sucursalService.obtener(id));
    }

    // Crea una sucursal
    @PreAuthorize("hasRole('GERENCIA')")
    @PostMapping
    public ResponseEntity<SucursalResponse> crear(@Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(sucursalService.crear(request));
    }

    // actualiza los datos de una sucursal
    @PreAuthorize("hasRole('GERENCIA')")
    @PutMapping("/{id}")
    public ResponseEntity<SucursalResponse> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody SucursalRequest request) {
        return ResponseEntity.ok(sucursalService.actualizar(id, request));

    }
}
