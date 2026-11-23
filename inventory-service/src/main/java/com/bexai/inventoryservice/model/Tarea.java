package com.bexai.inventoryservice.model;

import com.bexai.inventoryservice.enums.AccionTarea;
import com.bexai.inventoryservice.enums.TipoTarea;

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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tareas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 7)
    private String operadorCodigo;

    @NotBlank(message = "El título de la tarea es obligatorio")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String titulo;

    @Size(max = 500)
    @Column(length = 500)
    private String detalle;

    @NotNull(message = "La acción es obligatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccionTarea accion;

    @NotNull(message = "El tipo de tarea es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTarea tipo;

    @NotNull(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private Long sucursalId;
}
