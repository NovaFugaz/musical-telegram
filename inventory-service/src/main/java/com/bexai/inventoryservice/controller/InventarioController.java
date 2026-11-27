package com.bexai.inventoryservice.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.inventoryservice.dto.StockRequest;
import com.bexai.inventoryservice.dto.StockResponse;
import com.bexai.inventoryservice.service.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/stock", "/stock"})
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<StockResponse>> listarPorSucursal(@RequestParam Long sucursalId) {
        return ResponseEntity.ok(inventarioService.listarPorSucursal(sucursalId));
    }

    @GetMapping("/{sku}")
    public ResponseEntity<StockResponse> obtenerPorSku(
            @PathVariable String sku,
            @RequestParam Long sucursalId) {
        return ResponseEntity.ok(inventarioService.obtenerPorSkuYSucursal(sku, sucursalId));
    }

    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PutMapping("/{sku}")
    public ResponseEntity<StockResponse> actualizarStock(
            @PathVariable String sku,
            @Valid @RequestBody StockRequest request) {
        return ResponseEntity.ok(inventarioService.actualizarStock(sku, request));
    }
}
