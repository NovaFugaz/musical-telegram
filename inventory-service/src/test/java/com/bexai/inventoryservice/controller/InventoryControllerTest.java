package com.bexai.inventoryservice.controller;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bexai.inventoryservice.dto.AlertaResponse;
import com.bexai.inventoryservice.dto.SolicitudEstadoRequest;
import com.bexai.inventoryservice.dto.SolicitudRequest;
import com.bexai.inventoryservice.dto.SolicitudResponse;
import com.bexai.inventoryservice.enums.SeveridadAlerta;
import com.bexai.inventoryservice.service.AlertaService;
import com.bexai.inventoryservice.service.SolicitudReposicionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class InventoryControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private AlertaService alertaService;

    @MockitoBean
    private SolicitudReposicionService solicitudService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void listarAlertas_SinAutenticacion_Retorna401() throws Exception {
        mockMvc.perform(get("/api/alertas"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void listarAlertas_Autenticado_Retorna200() throws Exception {
        AlertaResponse alerta = AlertaResponse.builder()
                .id("ALT-001")
                .tituloAlerta("QUIEBRE ACTIVO")
                .sku("SKU-3361284")
                .severidad(SeveridadAlerta.QUIEBRE_CRITICO)
                .atendida(false)
                .build();

        when(alertaService.listar(null)).thenReturn(List.of(alerta));

        mockMvc.perform(get("/alertas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("ALT-001"));
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void emitirSolicitud_ConRolTerreno_Retorna201() throws Exception {
        SolicitudRequest request = new SolicitudRequest();
        request.setSku("SKU-3361284");
        request.setNombreProducto("Disco Norton");
        request.setCantidad(30);
        request.setDestino("Bodega Central");
        request.setPrioridad("Urgente");
        request.setSucursalId(1L);

        SolicitudResponse response = SolicitudResponse.builder()
                .id(1L)
                .sku("SKU-3361284")
                .estado("Pendiente")
                .build();

        when(solicitudService.emitir(any(SolicitudRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/solicitudes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.estado").value("Pendiente"));
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void actualizarEstadoSolicitud_ConRolTerreno_Retorna403() throws Exception {
        SolicitudEstadoRequest request = new SolicitudEstadoRequest();
        request.setEstado("Aprobada");

        mockMvc.perform(patch("/api/solicitudes/1/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }
}
