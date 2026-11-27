package com.bexai.inventoryservice.dto;

import com.bexai.inventoryservice.enums.Turno;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JornadaRequest {

    @Size(max = 7)
    private String operadorCodigo;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;

    @NotNull(message = "El turno es obligatorio")
    private Turno turno;
}
