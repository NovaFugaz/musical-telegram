package com.bexai.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bexai.inventoryservice.model.ConteoFisico;

public interface ConteoFisicoRepository extends JpaRepository<ConteoFisico, Long> {

    List<ConteoFisico> findBySucursalIdOrderByFechaHoraDesc(Long sucursalId);
}
