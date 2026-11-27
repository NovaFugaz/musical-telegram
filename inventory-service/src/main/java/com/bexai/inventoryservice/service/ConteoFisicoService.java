package com.bexai.inventoryservice.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.inventoryservice.dto.ConteoRequest;
import com.bexai.inventoryservice.dto.ConteoResponse;
import com.bexai.inventoryservice.model.ConteoFisico;
import com.bexai.inventoryservice.repository.ConteoFisicoRepository;
import com.bexai.inventoryservice.repository.InventarioSucursalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConteoFisicoService {

    private final ConteoFisicoRepository conteoRepository;
    private final InventarioSucursalRepository inventarioRepository;

    @Transactional(readOnly = true)
    public List<ConteoResponse> listarPorSucursal(Long sucursalId) {
        return conteoRepository.findBySucursalIdOrderByFechaHoraDesc(sucursalId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public ConteoResponse registrar(ConteoRequest request, String operadorAutenticado) {
        String operador = (request.getOperadorCodigo() != null && !request.getOperadorCodigo().isBlank())
                ? request.getOperadorCodigo().trim()
                : operadorAutenticado;

        int diferencia = request.getConteoFisico() - request.getStockTeorico();
        String sku = request.getSku().trim();

        ConteoFisico conteo = new ConteoFisico();
        conteo.setSku(sku);
        conteo.setStockTeorico(request.getStockTeorico());
        conteo.setConteoFisico(request.getConteoFisico());
        conteo.setDiferencia(diferencia);
        conteo.setMotivo(request.getMotivo());
        conteo.setOperadorCodigo(operador);
        conteo.setSucursalId(request.getSucursalId());
        conteo.setFechaHora(LocalDateTime.now());

        // Sincronizar stock en tienda si existe el registro en inventario_sucursal
        inventarioRepository.findBySkuIgnoreCaseAndSucursalId(sku, request.getSucursalId())
                .ifPresent(inv -> {
                    inv.setStockTienda(request.getConteoFisico());
                    inventarioRepository.save(inv);
                });

        log.info("Conteo registrado: sku={}, sucursal={}, dif={}, operador={}",
                sku, request.getSucursalId(), diferencia, operador);
        return toResponse(conteoRepository.save(conteo));
    }

    private ConteoResponse toResponse(ConteoFisico c) {
        return ConteoResponse.builder()
                .id(c.getId())
                .sku(c.getSku())
                .stockTeorico(c.getStockTeorico())
                .conteoFisico(c.getConteoFisico())
                .diferencia(c.getDiferencia())
                .motivo(c.getMotivo())
                .operadorCodigo(c.getOperadorCodigo())
                .sucursalId(c.getSucursalId())
                .fechaIso(c.getFechaHora().toLocalDate().format(DateTimeFormatter.ISO_LOCAL_DATE))
                .fechaHora(c.getFechaHora())
                .build();
    }
}
