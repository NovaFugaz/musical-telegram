package com.bexai.inventoryservice.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.inventoryservice.dto.ConteoRequest;
import com.bexai.inventoryservice.dto.ConteoResponse;
import com.bexai.inventoryservice.service.ConteoFisicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/conteos", "/conteos"})
@RequiredArgsConstructor
public class ConteoFisicoController {

    private final ConteoFisicoService conteoService;

    @GetMapping
    public ResponseEntity<List<ConteoResponse>> listar(@RequestParam(defaultValue = "1") Long sucursalId) {
        return ResponseEntity.ok(conteoService.listarPorSucursal(sucursalId));
    }

    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PostMapping
    public ResponseEntity<ConteoResponse> registrar(
            @Valid @RequestBody ConteoRequest request,
            Principal principal) {
        String operador = (principal != null) ? principal.getName() : "SISTEMA";
        return ResponseEntity.status(HttpStatus.CREATED).body(conteoService.registrar(request, operador));
    }
}
