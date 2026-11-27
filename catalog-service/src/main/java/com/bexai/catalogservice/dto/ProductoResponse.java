package com.bexai.catalogservice.dto;

import com.bexai.catalogservice.enums.UnidadMedida;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder 
@NoArgsConstructor
@AllArgsConstructor 
public class ProductoResponse {

    private String sku;
    private String ean13;
    private String nombre;
    private String descripcion;
    private UnidadMedida unidadMedida;
    private Double valor;
    private Integer stockMinimo;
    private Boolean activo;
}
