package com.bexai.authservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bexai.authservice.enums.Rol;

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
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "operadores")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Operador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Z]{2}-\\d{4}$, el código debe tener 7 caracteres.")
    @Column(unique = true, nullable = false, length = 7)
    private String codigo;

    @NotBlank(message = "El PIN es obligatorio (6 dígitos)")
    @Column(nullable = false)
    private String pin;

    @NotBlank(message = "El primer nombre es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String pNombre;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String apPaterno;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String apMaterno;

    @NotNull(message = "El rol es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @NotNull(message = "La fecha de ingreso es obligatoria")
    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;

    private LocalDateTime ultimoLogin;

    @NotNull
    @Column(nullable = false)
    private Boolean activo;
}
