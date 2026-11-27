package com.bexai.catalogservice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.catalogservice.dto.EstadoRequest;
import com.bexai.catalogservice.dto.PageResponse;
import com.bexai.catalogservice.dto.ProductoRequest;
import com.bexai.catalogservice.dto.ProductoResponse;
import com.bexai.catalogservice.dto.ProductoUpdateRequest;
import com.bexai.catalogservice.service.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/productos", "/productos"})
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    // listar productos
    @GetMapping
    public ResponseEntity<PageResponse<ProductoResponse>> listar(
        @RequestParam(defaultValue = "") String q,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size){
            return ResponseEntity.ok(productoService.listar(q, page, size));
        }

    // busca un producto por sku
    @GetMapping("/{sku}")
    public ResponseEntity<ProductoResponse> obtenerPorSku(@PathVariable String sku){
        return ResponseEntity.ok(productoService.obtenerPorSku(sku));
    }

    // buscar un producto por sku o ean13
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<ProductoResponse> obtenerPorCodigo(@PathVariable String codigo){
        return ResponseEntity.ok(productoService.obtenerPorCodigo(codigo));
    }

    // crea un nuevo producto
    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PostMapping
    public ResponseEntity<ProductoResponse> crear(@Valid @RequestBody ProductoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productoService.crear(request));
    }

    // actualizar datos de un producto
    @PreAuthorize("hasAnyRole('TERRENO','BODEGA','GERENCIA')")
    @PutMapping("/{sku}")
    public ResponseEntity<ProductoResponse> actualizar(
            @PathVariable String sku,
            @Valid @RequestBody ProductoUpdateRequest request) {
        return ResponseEntity.ok(productoService.actualizar(sku, request));
    }

    // cambiar el estado de un producto
    @PreAuthorize("hasRole('GERENCIA')")
    @PatchMapping("/{sku}/estado")
    public ResponseEntity<ProductoResponse> cambiarEstado(
            @PathVariable String sku,
            @Valid @RequestBody EstadoRequest request) {
        return ResponseEntity.ok(productoService.cambiarEstado(sku, request));
    }
}
