package com.bexai.inventoryservice.dto;

import com.bexai.inventoryservice.model.Ubicacion;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class StockRequest {

    @NotNull(message = "El id de la sucursal es obligatorio")
    private Long sucursalId;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    private Integer stockTienda;

    @Valid
    private Ubicacion ubicacion;
}
