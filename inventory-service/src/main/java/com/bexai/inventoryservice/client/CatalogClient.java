package com.bexai.inventoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "catalog-service")
public interface CatalogClient {

    @GetMapping("/api/productos/codigo/{codigo}")
    CatalogProductoDto obtenerPorCodigo(@PathVariable("codigo") String codigo);
}
