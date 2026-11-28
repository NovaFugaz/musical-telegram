package com.bexai.catalogservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;

import com.bexai.catalogservice.dto.SucursalRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bexai.catalogservice.dto.SucursalResponse;
import com.bexai.catalogservice.exception.GlobalExceptionHandler;
import com.bexai.catalogservice.exception.RecursoDuplicadoException;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.service.SucursalService;

@ExtendWith(MockitoExtension.class)
public class SucursalControllerTest {

    @Mock
    private SucursalService sucursalService;
	
    @InjectMocks
    private SucursalController sucursalController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(sucursalController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // obtener todas las sucursales
    @Test
    void listar_Retorna200ConListaDeSucursales() throws Exception {
        SucursalResponse pudahuel = SucursalResponse.builder()
                .idSucursal(1L)
                .nombreSucursal("CD Pudahuel")
                .direccion("Av. Vespucio 1234")
                .latitud(-33.43)
                .longitud(-70.78)
                .ciudad("Santiago")
                .region("Metropolitana")
                .build();

        SucursalResponse norte = SucursalResponse.builder()
                .idSucursal(2L)
                .nombreSucursal("Sucursal Norte")
                .ciudad("Antofagasta")
                .region("Antofagasta")
                .build();

        when(sucursalService.listar()).thenReturn(List.of(pudahuel, norte));
        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].idSucursal").value(1))
                .andExpect(jsonPath("$[0].nombreSucursal").value("CD Pudahuel"))
                .andExpect(jsonPath("$[0].ciudad").value("Santiago"))
                .andExpect(jsonPath("$[1].idSucursal").value(2))
                .andExpect(jsonPath("$[1].nombreSucursal").value("Sucursal Norte"));
        verify(sucursalService).listar();
    }

    // obtener sucursal por id
    @Test
    void obtener_Existente_Retorna200ConDetalle() throws Exception {
        SucursalResponse sucursal = SucursalResponse.builder()
                .idSucursal(1L)
                .nombreSucursal("CD Pudahuel")
                .direccion("Av. Vespucio 1234")
                .latitud(-33.43)
                .longitud(-70.78)
                .ciudad("Santiago")
                .region("Metropolitana")
                .build();

        when(sucursalService.obtener(1L)).thenReturn(sucursal);
        mockMvc.perform(get("/api/sucursales/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSucursal").value(1))
                .andExpect(jsonPath("$.nombreSucursal").value("CD Pudahuel"))
                .andExpect(jsonPath("$.direccion").value("Av. Vespucio 1234"))
                .andExpect(jsonPath("$.latitud").value(-33.43))
                .andExpect(jsonPath("$.longitud").value(-70.78))
                .andExpect(jsonPath("$.ciudad").value("Santiago"))
                .andExpect(jsonPath("$.region").value("Metropolitana"));
        verify(sucursalService).obtener(1L);
    }

    // crea una nueva sucursal
    @Test
    void crear_DatosValidos_Retorna201() throws Exception {
        String body = """
                {
                  "nombreSucursal": "Sucursal Temuco",
                  "direccion": "Av. Alemania 100",
                  "latitud": -38.7359,
                  "longitud": -72.5904,
                  "ciudad": "Temuco",
                  "region": "Araucania"
                }
                """;

        SucursalResponse creada = SucursalResponse.builder()
                .idSucursal(5L)
                .nombreSucursal("Sucursal Temuco")
                .direccion("Av. Alemania 100")
                .latitud(-38.7359)
                .longitud(-72.5904)
                .ciudad("Temuco")
                .region("Araucania")
                .build();

        when(sucursalService.crear(any(SucursalRequest.class))).thenReturn(creada);
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idSucursal").value(5))
                .andExpect(jsonPath("$.nombreSucursal").value("Sucursal Temuco"))
                .andExpect(jsonPath("$.ciudad").value("Temuco"));
        ArgumentCaptor<SucursalRequest> captor = ArgumentCaptor.forClass(SucursalRequest.class);
        verify(sucursalService).crear(captor.capture());

        SucursalRequest enviado = captor.getValue();
        assertEquals("Sucursal Temuco", enviado.getNombreSucursal());
        assertEquals("Av. Alemania 100", enviado.getDireccion());
        assertEquals(-38.7359, enviado.getLatitud());
        assertEquals(-72.5904, enviado.getLongitud());
        assertEquals("Temuco", enviado.getCiudad());
    }

    // actualizar datos de sucursal
    @Test
    void actualizar_DatosValidos_Retorna200() throws Exception {
        String body = """
                {
                  "nombreSucursal": "Sucursal Temuco Centro",
                  "direccion": "Av. Alemania 250",
                  "latitud": -38.7400,
                  "longitud": -72.5900,
                  "ciudad": "Temuco",
                  "region": "Araucania"
                }
                """;

        SucursalResponse actualizada = SucursalResponse.builder()
                .idSucursal(5L)
                .nombreSucursal("Sucursal Temuco Centro")
                .direccion("Av. Alemania 250")
                .latitud(-38.7400)
                .longitud(-72.5900)
                .ciudad("Temuco")
                .region("Araucania")
                .build();

        when(sucursalService.actualizar(eq(5L), any(SucursalRequest.class))).thenReturn(actualizada);
        mockMvc.perform(put("/api/sucursales/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSucursal").value(5))
                .andExpect(jsonPath("$.nombreSucursal").value("Sucursal Temuco Centro"))
                .andExpect(jsonPath("$.direccion").value("Av. Alemania 250"));

        ArgumentCaptor<SucursalRequest> captor = ArgumentCaptor.forClass(SucursalRequest.class);
        verify(sucursalService).actualizar(eq(5L), captor.capture());
        SucursalRequest enviado = captor.getValue();
        assertEquals("Sucursal Temuco Centro", enviado.getNombreSucursal());
        assertEquals("Av. Alemania 250", enviado.getDireccion());
        assertEquals(-38.7400, enviado.getLatitud());
        assertEquals(-72.5900, enviado.getLongitud());
    }

    // sucursal no encontrada
    @Test
    void obtener_NoExiste_Retorna404() throws Exception {
        when(sucursalService.obtener(999L))
                .thenThrow(new RecursoNoEncontradoException("Sucursal no encontrada: 999"));
        mockMvc.perform(get("/api/sucursales/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Sucursal no encontrada: 999"))
                .andExpect(jsonPath("$.path").value("/api/sucursales/999"));
        verify(sucursalService).obtener(999L);
    }

    // Sucursal datos invalidos
    @Test
    void crear_DatosInvalidos_Retorna400() throws Exception {
        String bodyInvalido = """
                {
                  "nombreSucursal": "",
                  "direccion": "Av. Alemania 100",
                  "latitud": 95,
                  "longitud": -200,
                  "ciudad": "Temuco",
                  "region": "Araucania"
                }
                """;
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details.length()").value(3))
                .andExpect(jsonPath("$.details", hasItem("El nombre de la sucursal es obligatorio")));
        verifyNoInteractions(sucursalService);
    }

    // sucursal ya existente
    @Test
    void crear_NombreDuplicado_Retorna409() throws Exception {
        String body = """
                {
                  "nombreSucursal": "CD Pudahuel",
                  "direccion": "Av. Vespucio 1234",
                  "latitud": -33.43,
                  "longitud": -70.78,
                  "ciudad": "Santiago",
                  "region": "Metropolitana"
                }
                """;

        when(sucursalService.crear(any(SucursalRequest.class)))
                .thenThrow(new RecursoDuplicadoException("Ya existe una sucursal con nombre: CD Pudahuel"));
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Ya existe una sucursal con nombre: CD Pudahuel"))
                .andExpect(jsonPath("$.path").value("/api/sucursales"));
        verify(sucursalService).crear(any(SucursalRequest.class));
    }

}
