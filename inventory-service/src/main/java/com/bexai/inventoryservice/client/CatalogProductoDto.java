package com.bexai.inventoryservice.client;

import lombok.Data;

@Data
public class CatalogProductoDto {
    private String sku;
    private String ean13;
    private String nombre;
    private String descripcion;
    private String unidadMedida;
    private Double valor;
    private Integer stockMinimo;
    private Boolean activo;
}
