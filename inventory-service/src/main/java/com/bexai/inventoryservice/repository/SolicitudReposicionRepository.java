package com.bexai.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bexai.inventoryservice.model.SolicitudReposicion;

public interface SolicitudReposicionRepository extends JpaRepository<SolicitudReposicion, Long> {

    List<SolicitudReposicion> findBySucursalIdOrderByFechaIsoDesc(Long sucursalId);
}
