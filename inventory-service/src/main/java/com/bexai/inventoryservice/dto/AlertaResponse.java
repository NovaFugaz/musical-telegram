package com.bexai.inventoryservice.dto;

import java.time.LocalDateTime;

import com.bexai.inventoryservice.enums.SeveridadAlerta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponse {
    private String id;
    private String tituloAlerta;
    private String sku;
    private String ean13;
    private String nombreProducto;
    private Integer stockActual;
    private Integer stockRequerido;
    private String ubicacionNombre;
    private SeveridadAlerta severidad;
    private String fechaIso;
    private String horaIso;
    private LocalDateTime fechaHora;
    private Boolean atendida;
}
