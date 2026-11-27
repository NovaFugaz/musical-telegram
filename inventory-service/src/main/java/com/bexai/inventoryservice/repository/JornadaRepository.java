package com.bexai.inventoryservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bexai.inventoryservice.model.Jornada;

public interface JornadaRepository extends JpaRepository<Jornada, Long> {

    Optional<Jornada> findFirstByOperadorCodigoAndFechaFinIsNullOrderByFechaInicioDesc(String operadorCodigo);
}
