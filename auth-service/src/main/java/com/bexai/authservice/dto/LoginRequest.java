package com.bexai.authservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "El código es obligatorio")
    @Pattern(regexp = "^[A-Z]{2}-\\d{4}$", message = "El código debe tener formato OP-XXXX (ej. OP-1001)")
    private String codigo;

    @NotBlank(message = "El PIN es obligatorio")
    @Pattern(regexp = "^\\d{6}$", message = "El PIN debe tener exactamente 6 dígitos")
    private String pin;
}
