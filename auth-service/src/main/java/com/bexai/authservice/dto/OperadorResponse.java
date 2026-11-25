package com.bexai.authservice.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.bexai.authservice.enums.Rol;
import com.bexai.authservice.enums.Turno;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperadorResponse {
    private Long id;
    private String codigo;
    private String nombre;
    private String pNombre;
    private String apPaterno;
    private String apMaterno;
    private Rol rol;
    private Long sucursalId;
    private Turno turno;
    private LocalDate fechaIngreso;
    private LocalDateTime ultimoLogin;
    private Boolean activo;
}
