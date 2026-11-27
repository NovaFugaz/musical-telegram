package com.bexai.inventoryservice.dto;

import java.time.LocalDateTime;

import com.bexai.inventoryservice.enums.Turno;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JornadaResponse {
    private Long id;
    private String operadorCodigo;
    private Long sucursalId;
    private Turno turno;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private boolean activa;
}
