package com.bexai.inventoryservice.model;

import java.time.LocalDateTime;

import com.bexai.inventoryservice.enums.Turno;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "jornadas")
@Getter 
@Setter
@NoArgsConstructor 
@AllArgsConstructor 
public class Jornada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código del operador es obligatorio")
    @Column(nullable = false, length = 7)
    private String operadorCodigo;

    @NotNull(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private Long sucursalId;

    @NotNull(message = "El turno es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
