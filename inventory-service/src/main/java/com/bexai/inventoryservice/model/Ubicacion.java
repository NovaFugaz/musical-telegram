package com.bexai.inventoryservice.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class Ubicacion {

    @NotBlank(message = "La zona es obligatoria")
    @Size(max = 10)
    private String zona;

    @NotBlank(message = "El pasillo es obligatorio")
    @Size(max = 10)
    private String pasillo;

    @NotBlank(message = "El estante es obligatorio")
    @Size(max = 10)
    private String estante;

    @NotBlank(message = "El nivel es obligatorio")
    @Size(max = 10)
    private String nivel;
}