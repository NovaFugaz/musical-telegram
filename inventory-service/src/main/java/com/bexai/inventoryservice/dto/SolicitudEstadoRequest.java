package com.bexai.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class SolicitudEstadoRequest {

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(Pendiente|Aprobada|En Tránsito|Completada)$",
            message = "Estado inválido (Pendiente, Aprobada, En Tránsito o Completada)")
    private String estado;
}
