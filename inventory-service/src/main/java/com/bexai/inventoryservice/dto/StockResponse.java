package com.bexai.inventoryservice.dto;

import com.bexai.inventoryservice.model.Ubicacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockResponse {
    private Long id;
    private String sku;
    private Long sucursalId;
    private Integer stockTienda;
    private Ubicacion ubicacion;
}
