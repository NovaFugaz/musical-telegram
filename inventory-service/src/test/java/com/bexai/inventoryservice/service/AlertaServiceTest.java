package com.bexai.inventoryservice.service;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bexai.inventoryservice.client.CatalogClient;
import com.bexai.inventoryservice.client.CatalogProductoDto;
import com.bexai.inventoryservice.dto.AlertaRequest;
import com.bexai.inventoryservice.dto.AlertaResponse;
import com.bexai.inventoryservice.enums.SeveridadAlerta;
import com.bexai.inventoryservice.exception.RecursoNoEncontradoException;
import com.bexai.inventoryservice.model.Alerta;
import com.bexai.inventoryservice.repository.AlertaRepository;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @Mock
    private CatalogClient catalogClient;

    @InjectMocks
    private AlertaService alertaService;

    @Test
    void emitir_EnriqueceDesdeCatalogoCuandoFaltaNombre() {
        AlertaRequest request = new AlertaRequest();
        request.setTituloAlerta("QUIEBRE ACTIVO");
        request.setSku("SKU-3361284");
        request.setStockActual(0);
        request.setStockRequerido(25);
        request.setSeveridad(SeveridadAlerta.QUIEBRE_CRITICO);

        CatalogProductoDto prod = new CatalogProductoDto();
        prod.setSku("SKU-3361284");
        prod.setEan13("7805541299831");
        prod.setNombre("Disco de Corte Norton");

        when(catalogClient.obtenerPorCodigo("SKU-3361284")).thenReturn(prod);
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(i -> i.getArgument(0));

        AlertaResponse response = alertaService.emitir(request);

        assertNotNull(response.getId());
        assertTrue(response.getId().startsWith("ALT-"));
        assertEquals("7805541299831", response.getEan13());
        assertEquals("Disco de Corte Norton", response.getNombreProducto());
        assertFalse(response.getAtendida());
    }

    @Test
    void atender_MarcaComoAtendida() {
        Alerta alerta = new Alerta("ALT-001", "QUIEBRE", "SKU-1", "123", "Prod",
                0, 10, "Pasillo 1", SeveridadAlerta.QUIEBRE_CRITICO, LocalDateTime.now(), false);

        when(alertaRepository.findById("ALT-001")).thenReturn(Optional.of(alerta));
        when(alertaRepository.save(any(Alerta.class))).thenAnswer(i -> i.getArgument(0));

        AlertaResponse response = alertaService.atender("ALT-001");

        assertTrue(response.getAtendida());
    }

    @Test
    void atender_NoExiste_LanzaRecursoNoEncontrado() {
        when(alertaRepository.findById("ALT-999")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> alertaService.atender("ALT-999"));
    }
}
