package com.bexai.inventoryservice.model;

import java.time.LocalDateTime;

import com.bexai.inventoryservice.enums.SeveridadAlerta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "alertas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @Column(nullable = false, length = 50)
    private String id;

    @NotBlank(message = "El titulo de la alerta es obligatorio")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String tituloAlerta;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(nullable = false, length = 50)
    private String sku;

    @Column(length = 13)
    private String ean13;

    @Size(max = 150)
    @Column(length = 150)
    private String nombreProducto;

    @NotNull(message = "El stock actual es obligatorio")
    @PositiveOrZero
    @Column(nullable = false)
    private Integer stockActual;

    @NotNull(message = "El stock requerido es obligatorio")
    @PositiveOrZero
    @Column(nullable = false)
    private Integer stockRequerido;

    @Size(max = 200)
    @Column(length = 200)
    private String ubicacionNombre;

    @NotNull(message = "La severidad es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeveridadAlerta severidad;

    @NotNull(message = "La fecha y hora son obligatorias")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(nullable = false)
    private Boolean atendida = false;
}
