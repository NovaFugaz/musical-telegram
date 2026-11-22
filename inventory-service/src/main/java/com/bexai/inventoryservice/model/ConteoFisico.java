package com.bexai.inventoryservice.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conteos_fisicos")
@Getter 
@Setter 
@NoArgsConstructor
@AllArgsConstructor
public class ConteoFisico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(nullable = false, length = 50)
    private String sku;

    @NotNull(message = "El stock teórico es obligatorio")
    @PositiveOrZero(message = "El stock teórico no puede ser negativo")
    @Column(nullable = false)
    private Integer stockTeorico;

    @NotNull(message = "El conteo físico es obligatorio")
    @PositiveOrZero(message = "El conteo físico no puede ser negativo")
    @Column(nullable = false)
    private Integer conteoFisico;

    @NotNull(message = "La diferencia es obligatoria")
    @Column(nullable = false)
    private Integer diferencia;

    @Size(max = 200)
    @Column(length = 200)
    private String motivo;

    @Column(length = 7)
    private String operadorCodigo;

    @NotNull(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private Long sucursalId;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Column(nullable = false)
    private LocalDateTime fechaHora;
}
