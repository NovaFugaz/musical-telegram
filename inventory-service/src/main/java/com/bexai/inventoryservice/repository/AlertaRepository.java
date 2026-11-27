package com.bexai.inventoryservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bexai.inventoryservice.model.Alerta;

public interface AlertaRepository extends JpaRepository<Alerta, String> {

    List<Alerta> findAllByOrderByFechaHoraDesc();

    List<Alerta> findByAtendidaOrderByFechaHoraDesc(Boolean atendida);
}
