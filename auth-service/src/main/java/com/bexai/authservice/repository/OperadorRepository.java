package com.bexai.authservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bexai.authservice.model.Operador;

@Repository
public interface OperadorRepository extends JpaRepository<Operador, Long> {
    Optional<Operador> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
