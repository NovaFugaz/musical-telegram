package com.bexai.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ConteoRequest {

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50)
    private String sku;

    @NotNull(message = "El stock teórico es obligatorio")
    @PositiveOrZero(message = "El stock teórico no puede ser negativo")
    private Integer stockTeorico;

    @NotNull(message = "El conteo físico es obligatorio")
    @PositiveOrZero(message = "El conteo físico no puede ser negativo")
    private Integer conteoFisico;

    @Size(max = 200)
    private String motivo;

    @Size(max = 7)
    private String operadorCodigo;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;
}
