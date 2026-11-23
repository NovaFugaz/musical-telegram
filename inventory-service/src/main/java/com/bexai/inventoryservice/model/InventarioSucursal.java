package com.bexai.inventoryservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "inventario_sucursal", uniqueConstraints = @UniqueConstraint(columnNames = { "sku", "sucursal_id" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventarioSucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El SKU es obligatorio")
    @Column(nullable = false, length = 50)
    private String sku;

    @NotNull(message = "El id de la sucursal es obligatorio")
    @Column(name = "sucursal_id", nullable = false)
    private Long sucursalId;

    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stockTienda;

    @Valid
    @Embedded
    private Ubicacion ubicacion;
}
