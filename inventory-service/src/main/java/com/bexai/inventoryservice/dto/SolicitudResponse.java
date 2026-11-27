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
public class SolicitudResponse {
    private Long id;
    private String sku;
    private String nombreProducto;
    private Integer cantidad;
    private String destino;
    private String prioridad;
    private String observacion;
    private String estado;
    private String fechaIso;
    private LocalDateTime fechaHora;
    private Long sucursalId;
}
