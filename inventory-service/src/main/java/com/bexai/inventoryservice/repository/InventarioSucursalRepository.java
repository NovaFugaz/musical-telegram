package com.bexai.inventoryservice.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bexai.inventoryservice.model.InventarioSucursal;

public interface InventarioSucursalRepository extends JpaRepository<InventarioSucursal, Long> {

    List<InventarioSucursal> findBySucursalIdOrderBySkuAsc(Long sucursalId);

    Optional<InventarioSucursal> findBySkuIgnoreCaseAndSucursalId(String sku, Long sucursalId);
}
