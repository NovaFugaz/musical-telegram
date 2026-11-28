package com.bexai.catalogservice.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.bexai.catalogservice.dto.EstadoRequest;
import com.bexai.catalogservice.dto.PageResponse;
import com.bexai.catalogservice.dto.ProductoRequest;
import com.bexai.catalogservice.dto.ProductoResponse;
import com.bexai.catalogservice.dto.ProductoUpdateRequest;
import com.bexai.catalogservice.enums.UnidadMedida;
import com.bexai.catalogservice.exception.GlobalExceptionHandler;
import com.bexai.catalogservice.service.ProductoService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.exception.RecursoDuplicadoException;
import static org.hamcrest.Matchers.hasItem;

@ExtendWith(MockitoExtension.class)
class ProductoControllerTest {

    @Mock
    private ProductoService productoService;

    @InjectMocks 
    private ProductoController productoController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productoController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    // paginado
   @Test
    void listar_ConFiltro_Retorna200ConPagina() throws Exception {
        ProductoResponse producto = ProductoResponse.builder()
                .sku("SKU-8841920")
                .ean13("7809482100412")
                .nombre("Taladro Percutor 20V Dewalt")
                .unidadMedida(UnidadMedida.UN)
                .valor(95000.0)
                .stockMinimo(8)
                .activo(true)
                .build();

        PageResponse<ProductoResponse> pagina = new PageResponse<>(List.of(producto), 0, 5, 1L, 1);

        when(productoService.listar("taladro", 0, 5)).thenReturn(pagina);

        mockMvc.perform(get("/api/productos")
                        .param("q", "taladro")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].sku").value("SKU-8841920"))
                .andExpect(jsonPath("$.content[0].nombre").value("Taladro Percutor 20V Dewalt"))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.size").value(5))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1));
        verify(productoService).listar("taladro", 0, 5);
    }

    // get sku
    @Test
    void obtenerPorSku_Existente_Retorna200ConDetalle() throws Exception {
        ProductoResponse producto = ProductoResponse.builder()
                .sku("SKU-7704219")
                .ean13("7801234567890")
                .nombre("Esmeril Angular 850W Dewalt")
                .descripcion("Herramientas electricas pesadas")
                .unidadMedida(UnidadMedida.UN)
                .valor(148420.0)
                .stockMinimo(10)
                .activo(true)
                .build();

        when(productoService.obtenerPorSku("SKU-7704219")).thenReturn(producto);

        mockMvc.perform(get("/api/productos/SKU-7704219"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-7704219"))
                .andExpect(jsonPath("$.ean13").value("7801234567890"))
                .andExpect(jsonPath("$.nombre").value("Esmeril Angular 850W Dewalt"))
                .andExpect(jsonPath("$.unidadMedida").value("UN"))
                .andExpect(jsonPath("$.valor").value(148420.0))
                .andExpect(jsonPath("$.stockMinimo").value(10))
                .andExpect(jsonPath("$.activo").value(true));
        verify(productoService).obtenerPorSku("SKU-7704219");
    }

    // get codigo
    @Test
    void obtenerPorCodigo_Ean13Existente_Retorna200() throws Exception {
        ProductoResponse producto = ProductoResponse.builder()
                .sku("SKU-7704219")
                .ean13("7801234567890")
                .nombre("Esmeril Angular 850W Dewalt")
                .unidadMedida(UnidadMedida.UN)
                .valor(148420.0)
                .stockMinimo(10)
                .activo(true)
                .build();

        when(productoService.obtenerPorCodigo("7801234567890")).thenReturn(producto);
        mockMvc.perform(get("/api/productos/codigo/7801234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-7704219"))
                .andExpect(jsonPath("$.ean13").value("7801234567890"))
                .andExpect(jsonPath("$.nombre").value("Esmeril Angular 850W Dewalt"))
                .andExpect(jsonPath("$.activo").value(true));
        verify(productoService).obtenerPorCodigo("7801234567890");
    }

    // productos
    @Test
    void crear_DatosValidos_Retorna201() throws Exception {
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
                .ean13("7801111111111")
                .nombre("Martillo 16oz")
                .descripcion("Mango fibra")
                .unidadMedida(UnidadMedida.UN)
                .valor(9990.0)
                .stockMinimo(10)
                .activo(true)
                .build();

        when(productoService.crear(any(ProductoRequest.class))).thenReturn(creado);
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.sku").value("SKU-9999001"))
                .andExpect(jsonPath("$.nombre").value("Martillo 16oz"))
                .andExpect(jsonPath("$.activo").value(true));
        ArgumentCaptor<ProductoRequest> captor = ArgumentCaptor.forClass(ProductoRequest.class);
        verify(productoService).crear(captor.capture());

        ProductoRequest enviado = captor.getValue();
        assertEquals("SKU-9999001", enviado.getSku());
        assertEquals("7801111111111", enviado.getEan13());
        assertEquals("Martillo 16oz", enviado.getNombre());
        assertEquals(UnidadMedida.UN, enviado.getUnidadMedida());
        assertEquals(9990.0, enviado.getValor());
        assertEquals(10, enviado.getStockMinimo());
    }

    // actualizar datos
    @Test
    void actualizar_DatosValidos_Retorna200() throws Exception {
        String body = """
                {
                  "ean13": "7801111111111",
                  "nombre": "Martillo 20oz",
                  "descripcion": "Mango fibra reforzado",
                  "unidadMedida": "UN",
                  "valor": 11990,
                  "stockMinimo": 8
                }
                """;

        ProductoResponse actualizado = ProductoResponse.builder()
                .sku("SKU-9999001")
                .ean13("7801111111111")
                .nombre("Martillo 20oz")
                .descripcion("Mango fibra reforzado")
                .unidadMedida(UnidadMedida.UN)
                .valor(11990.0)
                .stockMinimo(8)
                .activo(true)
                .build();

        when(productoService.actualizar(eq("SKU-9999001"), any(ProductoUpdateRequest.class)))
                .thenReturn(actualizado);
        mockMvc.perform(put("/api/productos/SKU-9999001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-9999001"))
                .andExpect(jsonPath("$.nombre").value("Martillo 20oz"))
                .andExpect(jsonPath("$.valor").value(11990.0))
                .andExpect(jsonPath("$.stockMinimo").value(8));
        ArgumentCaptor<ProductoUpdateRequest> captor = ArgumentCaptor.forClass(ProductoUpdateRequest.class);
        verify(productoService).actualizar(eq("SKU-9999001"), captor.capture());

        ProductoUpdateRequest enviado = captor.getValue();
        assertEquals("7801111111111", enviado.getEan13());
        assertEquals("Martillo 20oz", enviado.getNombre());
        assertEquals(UnidadMedida.UN, enviado.getUnidadMedida());
        assertEquals(11990.0, enviado.getValor());
        assertEquals(8, enviado.getStockMinimo());
    }

    // activar desactivar producto
    @Test
    void cambiarEstado_Desactivar_Retorna200() throws Exception {
        String body = """
                {
                  "activo": false
                }
                """;

        ProductoResponse desactivado = ProductoResponse.builder()
                .sku("SKU-9999001")
                .nombre("Martillo 16oz")
                .activo(false)
                .build();

        when(productoService.cambiarEstado(eq("SKU-9999001"), any(EstadoRequest.class)))
                .thenReturn(desactivado);
        mockMvc.perform(patch("/api/productos/SKU-9999001/estado")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU-9999001"))
                .andExpect(jsonPath("$.activo").value(false));
        ArgumentCaptor<EstadoRequest> captor = ArgumentCaptor.forClass(EstadoRequest.class);
        verify(productoService).cambiarEstado(eq("SKU-9999001"), captor.capture());

        assertFalse(captor.getValue().getActivo());
    }

    // sku no existe
    @Test
    void obtenerPorSku_NoExiste_Retorna404() throws Exception {
        when(productoService.obtenerPorSku("SKU-NOEXISTE"))
                .thenThrow(new RecursoNoEncontradoException("Producto no encontrado: SKU-NOEXISTE"));
        mockMvc.perform(get("/api/productos/SKU-NOEXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Producto no encontrado: SKU-NOEXISTE"))
                .andExpect(jsonPath("$.path").value("/api/productos/SKU-NOEXISTE"));
        verify(productoService).obtenerPorSku("SKU-NOEXISTE");
    }

    // producto con datos invalidos
    @Test
    void crear_DatosInvalidos_Retorna400() throws Exception {
        String bodyInvalido = """
                {
                  "sku": "",
                  "ean13": "123",
                  "nombre": "",
                  "descripcion": "Mango fibra",
                  "unidadMedida": "UN",
                  "valor": 9990,
                  "stockMinimo": 10
                }
                """;
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bodyInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.details.length()").value(3))
                .andExpect(jsonPath("$.details", hasItem("El SKU es obligatorio")))
                .andExpect(jsonPath("$.details", hasItem("El nombre del producto es obligatorio")));
        verifyNoInteractions(productoService);
    }

    // producto duplicado
        @Test
    void crear_SkuDuplicado_Retorna409() throws Exception {
        String body = """
                {
                  "sku": "SKU-7704219",
                  "ean13": "7801234567890",
                  "nombre": "Esmeril Angular 850W Dewalt",
                  "descripcion": "Herramientas electricas pesadas",
                  "unidadMedida": "UN",
                  "valor": 148420,
                  "stockMinimo": 10
                }
                """;
        when(productoService.crear(any(ProductoRequest.class)))
                .thenThrow(new RecursoDuplicadoException("Ya existe un producto con SKU: SKU-7704219"));
        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Ya existe un producto con SKU: SKU-7704219"))
                .andExpect(jsonPath("$.path").value("/api/productos"));
        verify(productoService).crear(any(ProductoRequest.class));
    }
}
