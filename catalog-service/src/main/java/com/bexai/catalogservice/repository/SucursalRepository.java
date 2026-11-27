package com.bexai.catalogservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bexai.catalogservice.model.Sucursal;

public interface SucursalRepository extends JpaRepository<Sucursal, Long> {

    boolean existsByNombreSucursalIgnoreCase(String nombreSucursal);
    boolean existsByNombreSucursalIgnoreCaseAndIdSucursalNot(String nombreSucursal, Long idSucursal);

    @Query("SELECT COALESCE(MAX(s.idSucursal), 0) FROM Sucursal s")
    Long obtenerMaxId();
}
