package com.bexai.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SolicitudRequest {

    @NotBlank(message = "El SKU es obligatorio")
    @Size(max = 50)
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150)
    private String nombreProducto;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El destino es obligatorio")
    @Pattern(regexp = "^(Bodega Central|Proveedor Directo)$",
            message = "Destino inválido (Bodega Central o Proveedor Directo)")
    private String destino;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "^(Normal|Urgente)$",
            message = "Prioridad inválida (Normal o Urgente)")
    private String prioridad;

    @Size(max = 300)
    private String observacion;

    @NotNull(message = "La sucursal es obligatoria")
    private Long sucursalId;
}
