package com.bexai.catalogservice.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable 
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class Ubicacion 
{
    @NotBlank(message = "La zona es obligatoria")
    @Size(max = 10) // ni idea puse todo de 10 luego se modificara
    private String zona;

    @NotBlank(message = "El pasillo es obligatoria")
    @Size(max = 10)
    private String pasillo;

    @NotBlank(message = "El estante es obligatoria")
    @Size(max = 10)
    private String estante;

    @NotBlank(message = "El nivel es obligatoria")
    @Size(max = 10)
    private String nivel;
}
