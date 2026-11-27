package com.bexai.catalogservice.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.bexai.catalogservice.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findBySkuIgnoreCase(String sku);
    boolean existsBySkuIgnoreCase(String sku);
    boolean existsByEan13(String ean13);
    boolean existsByEan13AndIdNot(String ean13, Long id);

    // Listar productos activos
    @Query("""
            SELECT p FROM Producto p
            WHERE p.activo = true
              AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR LOWER(p.sku) LIKE LOWER(CONCAT('%', :q, '%'))
                   OR p.ean13 LIKE CONCAT('%', :q, '%'))
            """)
    Page<Producto> buscar(@Param("q") String q, Pageable pageable);

    // Escáner o búsqueda manual
    @Query("""
            SELECT p FROM Producto p
            WHERE p.activo = true
              AND (LOWER(p.sku) = LOWER(:codigo) OR p.ean13 = :codigo)
            """)
    Optional<Producto> buscarPorCodigo(@Param("codigo") String codigo);
}
