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
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "solicitudes_reposicion")
@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor 
public class SolicitudReposicion {
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(nullable = false, length = 50)
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombreProducto;

    @NotNull(message = "La cantida es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    @Column(nullable = false)
    private Integer cantidad;

    @NotNull(message = "El destino es obligatorio")
    @Pattern(regexp = "^(Bodega Central|Proveedor Directo)$",
        message = "Destino inválido (Bodega Central o Proveedor Directo)")
    @Column(nullable = false)
    private String destino;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "^(Normal|Urgente)$", 
        message = "Prioridad inválida (Normal o Urgente)")
    @Column(nullable = false)
    private String prioridad;

    @Size(max = 300)
    @Column(length = 300) 
    private String observacion;

    @NotBlank(message = "El estado es obligatorio")
    @Pattern(regexp = "^(Pendiente|Aprobada|En Tránsito|Completada)$",
             message = "Estado inválido")
    @Column(nullable = false)
    private String estado = "Pendiente";

    @NotNull(message = "La fecha de emisión es obligatoria")
    @Column(nullable = false)
    private LocalDateTime fechaIso;

    @NotNull(message = "La sucursal es obligatoria")
    @Column(nullable = false)
    private Long sucursalId;
}
