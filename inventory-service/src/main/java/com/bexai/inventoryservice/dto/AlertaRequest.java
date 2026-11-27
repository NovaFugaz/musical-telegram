package com.bexai.inventoryservice.dto;

import com.bexai.inventoryservice.enums.SeveridadAlerta;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AlertaRequest {

    @Size(max = 50)
    private String id;

    @NotBlank(message = "El título de la alerta es obligatorio")
    @Size(max = 150)
    private String tituloAlerta;

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50)
    private String sku;

    @Size(max = 13)
    private String ean13;

    @Size(max = 150)
    private String nombreProducto;

    @NotNull(message = "El stock actual es obligatorio")
    @PositiveOrZero
    private Integer stockActual;

    @NotNull(message = "El stock requerido es obligatorio")
    @PositiveOrZero
    private Integer stockRequerido;

    @Size(max = 200)
    private String ubicacionNombre;

    @NotNull(message = "La severidad es obligatoria")
    private SeveridadAlerta severidad;
}
