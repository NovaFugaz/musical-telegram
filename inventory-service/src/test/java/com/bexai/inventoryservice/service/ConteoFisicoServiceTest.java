package com.bexai.inventoryservice.service;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bexai.inventoryservice.dto.ConteoRequest;
import com.bexai.inventoryservice.dto.ConteoResponse;
import com.bexai.inventoryservice.model.ConteoFisico;
import com.bexai.inventoryservice.model.InventarioSucursal;
import com.bexai.inventoryservice.model.Ubicacion;
import com.bexai.inventoryservice.repository.ConteoFisicoRepository;
import com.bexai.inventoryservice.repository.InventarioSucursalRepository;

@ExtendWith(MockitoExtension.class)
class ConteoFisicoServiceTest {

    @Mock
    private ConteoFisicoRepository conteoRepository;

    @Mock
    private InventarioSucursalRepository inventarioRepository;

    @InjectMocks
    private ConteoFisicoService conteoService;

    @Test
    void registrar_CalculaDiferenciaYSincronizaStock() {
        ConteoRequest request = new ConteoRequest();
        request.setSku("SKU-7704219");
        request.setStockTeorico(24);
        request.setConteoFisico(21);
        request.setMotivo("Merma / Daño");
        request.setSucursalId(1L);

        InventarioSucursal inv = new InventarioSucursal(
                1L, "SKU-7704219", 1L, 24, new Ubicacion("Principal", "B-04", "3", "2"));

        when(inventarioRepository.findBySkuIgnoreCaseAndSucursalId("SKU-7704219", 1L))
                .thenReturn(Optional.of(inv));
        when(conteoRepository.save(any(ConteoFisico.class))).thenAnswer(i -> {
            ConteoFisico c = i.getArgument(0);
            c.setId(10L);
            return c;
        });

        ConteoResponse response = conteoService.registrar(request, "OP-1001");

        assertNotNull(response);
        assertEquals(-3, response.getDiferencia());
        assertEquals("OP-1001", response.getOperadorCodigo());
        assertEquals(21, inv.getStockTienda());
        verify(inventarioRepository).save(inv);
    }
}
