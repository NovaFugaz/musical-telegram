package com.bexai.catalogservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data 
public class EstadoRequest {

    @NotNull(message = "El estado del producto es obligatorio")
    private Boolean activo;
}
