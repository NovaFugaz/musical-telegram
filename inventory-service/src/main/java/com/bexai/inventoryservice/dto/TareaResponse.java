package com.bexai.inventoryservice.dto;

import com.bexai.inventoryservice.enums.AccionTarea;
import com.bexai.inventoryservice.enums.TipoTarea;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TareaResponse {
    private Long id;
    private String operadorCodigo;
    private String titulo;
    private String detalle;
    private AccionTarea accion;
    private TipoTarea tipo;
    private Long sucursalId;
}
