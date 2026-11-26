package com.bexai.authservice.dto;

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
public class AuthResponse {
    private String token;
    @Builder.Default
    private String tipo = "Bearer";
    private String codigo;
    private String nombre;
    private String pNombre;
    private String apPaterno;
    private Rol rol;
    private Long sucursalId;
    private Turno turno;
}
