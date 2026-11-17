package com.bexai.inventoryservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @NotNull(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private Long sucursalId;

    @NotNull(message = "La cantidad contada es obligatoria")
    @PositiveOrZero(message = "La cantidad contada no puede ser negativa")
    @Column(nullable = false)
    private Integer cantidadContada;

    @NotNull(message = "La diferencia es obligatoria")
    @Column(nullable = false)
    private Integer diferencia;

    @NotBlank(message = "El código del operador es obligatorio")
    @Column(nullable = false, length = 7)
    private String operadorCodigo;

    @NotNull(message = "La fecha y hora son obligatorias")
    @PastOrPresent
    @Column(nullable = false)
    private LocalDateTime fechaHora;
}