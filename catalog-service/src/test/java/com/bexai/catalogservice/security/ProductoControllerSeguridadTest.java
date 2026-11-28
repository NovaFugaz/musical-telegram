package com.bexai.catalogservice.security;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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

import com.bexai.catalogservice.controller.ProductoController;
import com.bexai.catalogservice.service.ProductoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.bexai.catalogservice.dto.ProductoRequest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.bexai.catalogservice.dto.EstadoRequest;
import com.bexai.catalogservice.dto.ProductoResponse;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)
public class ProductoControllerSeguridadTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductoService productoService;

    @MockitoBean
    private JwtService jwtService;

    // Operador no puede desactivar productos
    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void cambiarEstado_ConRolTerreno_Retorna403() throws Exception {
        String body = """
                {
                  "activo": false
                }
                """;
        mockMvc.perform(patch("/api/productos/SKU-7704219/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
        verifyNoInteractions(productoService);
    }

    // Gerente puede desactivar productos
    @Test
    @WithMockUser(username = "OP-1003", roles = "GERENCIA")
    void cambiarEstado_ConRolGerencia_Retorna200() throws Exception {
        String body = """
                {
                  "activo": false
                }
                """;

        ProductoResponse desactivado = ProductoResponse.builder()
                .sku("SKU-7704219")
                .nombre("Esmeril Angular 850W Dewalt")
                .activo(false)
                .build();

        when(productoService.cambiarEstado(eq("SKU-7704219"), any(EstadoRequest.class)))
                .thenReturn(desactivado);
        mockMvc.perform(patch("/api/productos/SKU-7704219/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-7704219"))
                .andExpect(jsonPath("$.activo").value(false));
        verify(productoService).cambiarEstado(eq("SKU-7704219"), any(EstadoRequest.class));
    }

    // Petición sin token es rechazada
    @Test
    void listar_SinToken_Retorna401() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(productoService);
    }

    // Operador crear productos
    @Test
    @WithMockUser(username = "OP-1001", roles = "TERRENO")
    void crear_ConRolTerreno_Retorna201() throws Exception {
        String body = """
                {
                  "sku": "SKU-9999001",
                  "ean13": "7801111111111",
                  "nombre": "Martillo 16oz",
                  "descripcion": "Mango fibra",
                  "unidadMedida": "UN",
                  "valor": 9990,
                  "stockMinimo": 10
                }
                """;

        ProductoResponse creado = ProductoResponse.builder()
                .sku("SKU-9999001")
                .nombre("Martillo 16oz")
                .activo(true)
                .build();

        when(productoService.crear(any(ProductoRequest.class))).thenReturn(creado);
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-9999001"))
                .andExpect(jsonPath("$.activo").value(true));
        verify(productoService).crear(any(ProductoRequest.class));
    }

    // Bodega no puede desactivar productos
    @Test
    @WithMockUser(username = "OP-1002", roles = "BODEGA")
    void cambiarEstado_ConRolBodega_Retorna403() throws Exception {
        String body = """
                {
                  "activo": false
                }
                """;
        mockMvc.perform(patch("/api/productos/SKU-7704219/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
        verifyNoInteractions(productoService);
    }
}
