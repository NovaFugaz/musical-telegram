package com.bexai.catalogservice.security;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bexai.catalogservice.dto.SucursalRequest;
import com.bexai.catalogservice.dto.SucursalResponse;

import com.bexai.catalogservice.controller.SucursalController;
import com.bexai.catalogservice.service.SucursalService;

@WebMvcTest(SucursalController.class)
@Import(SecurityConfig.class) 
public class SucursalControllerSeguridadTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SucursalService sucursalService;

    @MockitoBean
    private JwtService jwtService;

    // Operador no puede crear sucursales
    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void crear_ConRolTerreno_Retorna403() throws Exception {
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
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
        verifyNoInteractions(sucursalService);
    }

    // bodega no puede crear sucursales
    @Test
    @WithMockUser(username = "OP-1002", roles = "BODEGA")
    void crear_ConRolBodega_Retorna403() throws Exception {
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
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
        verifyNoInteractions(sucursalService);
    }

    // gerencia puede crear nuevas sucursales
        @Test
    @WithMockUser(username = "OP-1003", roles = "GERENCIA")
    void crear_ConRolGerencia_Retorna201() throws Exception {
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
                .ciudad("Temuco")
                .region("Araucania")
                .build();

        when(sucursalService.crear(any(SucursalRequest.class))).thenReturn(creada);
        mockMvc.perform(post("/api/sucursales")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idSucursal").value(5))
                .andExpect(jsonPath("$.nombreSucursal").value("Sucursal Temuco"));
        verify(sucursalService).crear(any(SucursalRequest.class));
    }

    // operador no puede editar sucursales
    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void actualizar_ConRolTerreno_Retorna403() throws Exception {
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
        mockMvc.perform(put("/api/sucursales/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));

        verifyNoInteractions(sucursalService);
    }

    // gerencia puede editar sucursales
    @Test
    @WithMockUser(username = "OP-1003", roles = "GERENCIA")
    void actualizar_ConRolGerencia_Retorna200() throws Exception {
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
                .ciudad("Temuco")
                .region("Araucania")
                .build();

        when(sucursalService.actualizar(eq(5L), any(SucursalRequest.class))).thenReturn(actualizada);
        mockMvc.perform(put("/api/sucursales/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idSucursal").value(5))
                .andExpect(jsonPath("$.nombreSucursal").value("Sucursal Temuco Centro"));
        verify(sucursalService).actualizar(eq(5L), any(SucursalRequest.class));
    }

    // petición sin token es rechazada
    @Test
    void listar_SinToken_Retorna401() throws Exception {
        mockMvc.perform(get("/api/sucursales"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(sucursalService);
    }
}
