package com.bexai.catalogservice.dto;

import com.bexai.catalogservice.enums.UnidadMedida;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class ProductoUpdateRequest {
    @Pattern(regexp = "^\\d{13}$", message = "El EAN-13 debe tener 13 dígitos")
    private String ean13;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String descripcion;

    @NotNull(message = "La unidad de medida es obligatoria")
    private UnidadMedida unidadMedida;

    @NotNull(message = "El valor del producto es obligatorio")
    @PositiveOrZero(message = "El valor no puede ser negativo")
    private Double valor;

    @NotNull(message = "El stock mínimo es obligatorio")
    @PositiveOrZero(message = "El stock mínimo no puede ser negativo")
    private Integer stockMinimo;
}
