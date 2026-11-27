package com.bexai.catalogservice.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class SucursalRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombreSucursal;

    @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
    private String direccion;

    @DecimalMin(value = "-90.0", message = "Latitud inválida")
    @DecimalMax(value = "90.0", message = "Latitud inválida")
    private Double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud inválida")
    @DecimalMax(value = "180.0", message = "Longitud inválida")
    private Double longitud;

    @Size(max = 80, message = "La ciudad no puede superar los 80 caracteres")
    private String ciudad;

    @Size(max = 80, message = "La región no puede superar los 80 caracteres")
    private String region;
}
