package com.bexai.inventoryservice.dto;

import com.bexai.inventoryservice.enums.AccionTarea;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TareaAccionRequest {

    @NotNull(message = "La acción es obligatoria")
    private AccionTarea accion;
}
