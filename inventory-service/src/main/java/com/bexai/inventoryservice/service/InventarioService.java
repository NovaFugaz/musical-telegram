package com.bexai.inventoryservice.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.inventoryservice.dto.StockRequest;
import com.bexai.inventoryservice.dto.StockResponse;
import com.bexai.inventoryservice.exception.RecursoNoEncontradoException;
import com.bexai.inventoryservice.model.InventarioSucursal;
import com.bexai.inventoryservice.model.Ubicacion;
import com.bexai.inventoryservice.repository.InventarioSucursalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioSucursalRepository inventarioRepository;

    @Transactional(readOnly = true)
    public List<StockResponse> listarPorSucursal(Long sucursalId) {
        return inventarioRepository.findBySucursalIdOrderBySkuAsc(sucursalId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public StockResponse obtenerPorSkuYSucursal(String sku, Long sucursalId) {
        InventarioSucursal inv = inventarioRepository.findBySkuIgnoreCaseAndSucursalId(sku.trim(), sucursalId)
                .orElseThrow(() -> new RecursoNoEncontradoException(
                        "No hay registro de stock para SKU " + sku + " en sucursal " + sucursalId));
        return toResponse(inv);
    }

    @Transactional
    public StockResponse actualizarStock(String sku, StockRequest request) {
        String skuLimpio = sku.trim().toUpperCase();
        InventarioSucursal inv = inventarioRepository
                .findBySkuIgnoreCaseAndSucursalId(skuLimpio, request.getSucursalId())
                .orElseGet(() -> {
                    InventarioSucursal nuevo = new InventarioSucursal();
                    nuevo.setSku(skuLimpio);
                    nuevo.setSucursalId(request.getSucursalId());
                    nuevo.setUbicacion(new Ubicacion("General", "P-01", "1", "1"));
                    return nuevo;
                });

        inv.setStockTienda(request.getStockTienda());
        if (request.getUbicacion() != null) {
            inv.setUbicacion(request.getUbicacion());
        }

        log.info("Stock actualizado: sku={}, sucursal={}, stock={}", skuLimpio, request.getSucursalId(), request.getStockTienda());
        return toResponse(inventarioRepository.save(inv));
    }

    private StockResponse toResponse(InventarioSucursal inv) {
        return StockResponse.builder()
                .id(inv.getId())
                .sku(inv.getSku())
                .sucursalId(inv.getSucursalId())
                .stockTienda(inv.getStockTienda())
                .ubicacion(inv.getUbicacion())
                .build();
    }
}
