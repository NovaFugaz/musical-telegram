package com.bexai.catalogservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor 
@AllArgsConstructor 
public class SucursalResponse {
    private Long idSucursal;
    private String nombreSucursal;
    private String direccion;
    private Double latitud;
    private Double longitud;
    private String ciudad;
    private String region;
}
