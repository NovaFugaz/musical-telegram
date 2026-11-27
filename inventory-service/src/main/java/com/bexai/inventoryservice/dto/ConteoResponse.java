package com.bexai.inventoryservice.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConteoResponse {
    private Long id;
    private String sku;
    private Integer stockTeorico;
    private Integer conteoFisico;
    private Integer diferencia;
    private String motivo;
    private String operadorCodigo;
    private Long sucursalId;
    private String fechaIso;
    private LocalDateTime fechaHora;
}
