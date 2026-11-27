package com.bexai.catalogservice.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import com.bexai.catalogservice.dto.SucursalRequest;
import com.bexai.catalogservice.dto.SucursalResponse;
import com.bexai.catalogservice.exception.RecursoDuplicadoException;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.model.Sucursal;
import com.bexai.catalogservice.repository.SucursalRepository;

@ExtendWith(MockitoExtension.class)
class SucursalServiceTest {

    @Mock
    private SucursalRepository sucursalRepository;

    @InjectMocks
    private SucursalService sucursalService;

    private Sucursal sucursal;

    @BeforeEach
    void setUp() {
        sucursal = new Sucursal(1L, "CD Pudahuel", "Av. Vespucio 1234", -33.43, -70.78, "Santiago", "Metropolitana");
    }

    @Test
    void listar_Exitoso() {
        when(sucursalRepository.findAll(any(Sort.class))).thenReturn(List.of(sucursal));

        List<SucursalResponse> result = sucursalService.listar();

        assertEquals(1, result.size());
        assertEquals("CD Pudahuel", result.get(0).getNombreSucursal());
    }

    @Test
    void obtener_NoExiste_LanzaRecursoNoEncontrado() {
        when(sucursalRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> sucursalService.obtener(99L));
    }

    @Test
    void crear_Exitoso_AsignaSiguienteId() {
        SucursalRequest request = new SucursalRequest();
        request.setNombreSucursal("Sucursal Norte");
        request.setCiudad("Antofagasta");
        request.setRegion("Antofagasta");

        when(sucursalRepository.existsByNombreSucursalIgnoreCase("Sucursal Norte")).thenReturn(false);
        when(sucursalRepository.obtenerMaxId()).thenReturn(4L);
        when(sucursalRepository.save(any(Sucursal.class))).thenAnswer(inv -> inv.getArgument(0));

        SucursalResponse response = sucursalService.crear(request);

        assertNotNull(response);
        assertEquals(5L, response.getIdSucursal());
        assertEquals("Sucursal Norte", response.getNombreSucursal());
    }

    @Test
    void crear_NombreDuplicado_LanzaRecursoDuplicado() {
        SucursalRequest request = new SucursalRequest();
        request.setNombreSucursal("CD Pudahuel");

        when(sucursalRepository.existsByNombreSucursalIgnoreCase("CD Pudahuel")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> sucursalService.crear(request));
    }
}
