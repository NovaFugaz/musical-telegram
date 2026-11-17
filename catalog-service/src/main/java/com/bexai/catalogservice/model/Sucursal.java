package com.bexai.catalogservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = "sucursales")
@Getter 
@Setter
@AllArgsConstructor 
@NoArgsConstructor 
public class Sucursal {

    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY) no lleva en el modelo (?)
    private Long idSucursal;

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombreSucursal;

    @DecimalMin(value = "-90.0", message = "Latitud inválida")
    @DecimalMax(value = "90.0", message = "Latitud inválida")
    private Double latitud;

    @DecimalMin(value = "-180.0", message = "Longitud inválida")
    @DecimalMax(value = "180.0", message = "Longitud inválida")
    private Double longitud;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String ciudad;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String region;

}
