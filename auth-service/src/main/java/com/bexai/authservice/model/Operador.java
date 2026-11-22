package com.bexai.authservice.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bexai.authservice.enums.Rol;
import com.bexai.authservice.enums.Turno;

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
    @Pattern(regexp = "^[A-Z]{2}-\\d{4}$", message = "El código debe tener 7 caracteres (ej. OP-1001)")
    @Column(unique = true, nullable = false, length = 7)
    private String codigo;

    @NotBlank(message = "El PIN es obligatorio")
    @Pattern(regexp = "^\\d{6}$", message = "El PIN debe tener 6 dígitos")
    @Column(nullable = false)
    private String pin;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

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
    @NotNull(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private Long sucursalId;

    @NotNull(message = "El turno es obligatorio")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Turno turno;

    @PastOrPresent(message = "La fecha de ingreso no puede ser futura")
    private LocalDate fechaIngreso;

    private LocalDateTime ultimoLogin;

    @NotNull
    @Column(nullable = false)
    private Boolean activo = true;
}
