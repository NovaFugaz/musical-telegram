package com.bexai.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bexai.inventoryservice.model.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByOperadorCodigoOrderByIdAsc(String operadorCodigo);

    List<Tarea> findBySucursalIdOrderByIdAsc(Long sucursalId);
}
