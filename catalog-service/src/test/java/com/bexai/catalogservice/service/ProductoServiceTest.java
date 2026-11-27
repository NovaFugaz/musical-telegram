package com.bexai.catalogservice.service;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.bexai.catalogservice.dto.EstadoRequest;
import com.bexai.catalogservice.dto.PageResponse;
import com.bexai.catalogservice.dto.ProductoRequest;
import com.bexai.catalogservice.dto.ProductoResponse;
import com.bexai.catalogservice.dto.ProductoUpdateRequest;
import com.bexai.catalogservice.enums.UnidadMedida;
import com.bexai.catalogservice.exception.RecursoDuplicadoException;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.model.Producto;
import com.bexai.catalogservice.repository.ProductoRepository;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto productoValido;

    @BeforeEach
    void setUp() {
        productoValido = new Producto();
        productoValido.setId(1L);
        productoValido.setSku("SKU-7704219");
        productoValido.setEan13("7801234567890");
        productoValido.setNombre("Esmeril Angular 850W");
        productoValido.setDescripcion("Herramienta eléctrica");
        productoValido.setUnidadMedida(UnidadMedida.UN);
        productoValido.setValor(148420.0);
        productoValido.setStockMinimo(10);
        productoValido.setActivo(true);
    }

    @Test
    void listar_Exitoso() {
        Page<Producto> page = new PageImpl<>(List.of(productoValido));
        when(productoRepository.buscar(eq("Esmeril"), any(Pageable.class))).thenReturn(page);

        PageResponse<ProductoResponse> response = productoService.listar("Esmeril", 0, 20);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("SKU-7704219", response.getContent().get(0).getSku());
    }

    @Test
    void obtenerPorCodigo_Exitoso() {
        when(productoRepository.buscarPorCodigo("7801234567890")).thenReturn(Optional.of(productoValido));

        ProductoResponse response = productoService.obtenerPorCodigo("7801234567890");

        assertNotNull(response);
        assertEquals("SKU-7704219", response.getSku());
        assertEquals("Esmeril Angular 850W", response.getNombre());
    }

    @Test
    void obtenerPorCodigo_NoExiste_LanzaRecursoNoEncontrado() {
        when(productoRepository.buscarPorCodigo("NO-EXISTE")).thenReturn(Optional.empty());

        assertThrows(RecursoNoEncontradoException.class, () -> productoService.obtenerPorCodigo("NO-EXISTE"));
    }

    @Test
    void crear_Exitoso() {
        ProductoRequest request = new ProductoRequest();
        request.setSku("SKU-9999");
        request.setEan13("7809999999999");
        request.setNombre("Producto Nuevo");
        request.setDescripcion("Descripción");
        request.setUnidadMedida(UnidadMedida.UN);
        request.setValor(10000.0);
        request.setStockMinimo(5);

        when(productoRepository.existsBySkuIgnoreCase("SKU-9999")).thenReturn(false);
        when(productoRepository.existsByEan13("7809999999999")).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoResponse response = productoService.crear(request);

        assertNotNull(response);
        assertEquals("SKU-9999", response.getSku());
        assertTrue(response.getActivo());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void crear_SkuDuplicado_LanzaRecursoDuplicado() {
        ProductoRequest request = new ProductoRequest();
        request.setSku("SKU-7704219");

        when(productoRepository.existsBySkuIgnoreCase("SKU-7704219")).thenReturn(true);

        assertThrows(RecursoDuplicadoException.class, () -> productoService.crear(request));
    }

    @Test
    void actualizar_Exitoso() {
        ProductoUpdateRequest request = new ProductoUpdateRequest();
        request.setEan13("7801234567890");
        request.setNombre("Esmeril Actualizado");
        request.setDescripcion("Nueva desc");
        request.setUnidadMedida(UnidadMedida.UN);
        request.setValor(150000.0);
        request.setStockMinimo(12);

        when(productoRepository.findBySkuIgnoreCase("SKU-7704219")).thenReturn(Optional.of(productoValido));
        when(productoRepository.existsByEan13AndIdNot("7801234567890", 1L)).thenReturn(false);
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoResponse response = productoService.actualizar("SKU-7704219", request);

        assertEquals("Esmeril Actualizado", response.getNombre());
        assertEquals(150000.0, response.getValor());
    }

    @Test
    void cambiarEstado_Exitoso() {
        EstadoRequest request = new EstadoRequest();
        request.setActivo(false);

        when(productoRepository.findBySkuIgnoreCase("SKU-7704219")).thenReturn(Optional.of(productoValido));
        when(productoRepository.save(any(Producto.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductoResponse response = productoService.cambiarEstado("SKU-7704219", request);

        assertFalse(response.getActivo());
    }
}
