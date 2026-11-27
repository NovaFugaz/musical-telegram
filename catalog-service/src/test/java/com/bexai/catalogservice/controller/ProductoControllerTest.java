package com.bexai.catalogservice.controller;

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

import com.bexai.catalogservice.dto.EstadoRequest;
import com.bexai.catalogservice.dto.ProductoRequest;
import com.bexai.catalogservice.dto.ProductoResponse;
import com.bexai.catalogservice.enums.UnidadMedida;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.service.ProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
class ProductoControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    void obtenerPorCodigo_SinAutenticacion_Retorna401() throws Exception {
        mockMvc.perform(get("/api/productos/codigo/SKU-7704219"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void obtenerPorCodigo_Autenticado_Retorna200() throws Exception {
        ProductoResponse response = ProductoResponse.builder()
                .sku("SKU-7704219")
                .ean13("7801234567890")
                .nombre("Esmeril Angular 850W")
                .unidadMedida(UnidadMedida.UN)
                .valor(148420.0)
                .stockMinimo(10)
                .activo(true)
                .build();

        when(productoService.obtenerPorCodigo("SKU-7704219")).thenReturn(response);

        mockMvc.perform(get("/api/productos/codigo/SKU-7704219"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-7704219"))
                .andExpect(jsonPath("$.nombre").value("Esmeril Angular 850W"));
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void obtenerPorCodigo_NoExiste_Retorna404() throws Exception {
        when(productoService.obtenerPorCodigo("NO-EXISTE"))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado"));

        mockMvc.perform(get("/productos/codigo/NO-EXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void crear_ConRolTerreno_Retorna201() throws Exception {
        ProductoRequest request = new ProductoRequest();
        request.setSku("SKU-1000");
        request.setEan13("7801234567890");
        request.setNombre("Nuevo Producto");
        request.setUnidadMedida(UnidadMedida.UN);
        request.setValor(5000.0);
        request.setStockMinimo(2);

        ProductoResponse response = ProductoResponse.builder()
                .sku("SKU-1000")
                .nombre("Nuevo Producto")
                .activo(true)
                .build();

        when(productoService.crear(any(ProductoRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/productos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-1000"));
    }

    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void cambiarEstado_ConRolTerreno_Retorna403() throws Exception {
        EstadoRequest request = new EstadoRequest();
        request.setActivo(false);

        mockMvc.perform(patch("/api/productos/SKU-7704219/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "OP-1003", roles = "GERENCIA")
    void cambiarEstado_ConRolGerencia_Retorna200() throws Exception {
        EstadoRequest request = new EstadoRequest();
        request.setActivo(false);

        ProductoResponse response = ProductoResponse.builder()
                .sku("SKU-7704219")
                .activo(false)
                .build();

        when(productoService.cambiarEstado(any(), any(EstadoRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/api/productos/SKU-7704219/estado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value(false));
    }
}
