package com.bexai.catalogservice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.catalogservice.dto.EstadoRequest;
import com.bexai.catalogservice.dto.PageResponse;
import com.bexai.catalogservice.dto.ProductoRequest;
import com.bexai.catalogservice.dto.ProductoResponse;
import com.bexai.catalogservice.dto.ProductoUpdateRequest;
import com.bexai.catalogservice.exception.RecursoDuplicadoException;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.model.Producto;
import com.bexai.catalogservice.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private static final int TAMANO_MAXIMO = 100;

    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public PageResponse<ProductoResponse> listar(String q, int page, int size) {
        String filtro = (q == null) ? "" : q.trim();
        int pagina = Math.max(page, 0);
        int tamano = Math.min(Math.max(size, 1), TAMANO_MAXIMO);

        Page<Producto> resultado = productoRepository.buscar(filtro, PageRequest.of(pagina, tamano, Sort.by("nombre")));
        return PageResponse.from(resultado.map(this::toResponse));
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorSku(String sku) {
        return toResponse(buscarSku(sku));
    }

    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorCodigo(String codigo) {
        return productoRepository.buscarPorCodigo(codigo.trim())
                .map(this::toResponse)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un producto activo con código: " + codigo));
    }

    @Transactional
    public ProductoResponse crear(ProductoRequest request) {
        String sku = request.getSku().trim();

        if (productoRepository.existsBySkuIgnoreCase(sku)) {
            throw new RecursoDuplicadoException("Ya existe un producto con SKU: " + sku);
        }
        if (request.getEan13() != null && productoRepository.existsByEan13(request.getEan13())) {
            throw new RecursoDuplicadoException("Ya existe un producto con EAN-13: " + request.getEan13());
        }

        Producto producto = new Producto();
        producto.setSku(sku);
        producto.setEan13(request.getEan13());
        producto.setNombre(request.getNombre().trim());
        producto.setDescripcion(request.getDescripcion());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setValor(request.getValor());
        producto.setStockMinimo(request.getStockMinimo());
        producto.setActivo(true);

        log.info("Producto creado: {}", sku);
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse actualizar(String sku, ProductoUpdateRequest request) {
        Producto producto = buscarSku(sku);

        if (request.getEan13() != null
                && productoRepository.existsByEan13AndIdNot(request.getEan13(), producto.getId())) {
            throw new RecursoDuplicadoException("Ya existe otro producto con EAN-13: " + request.getEan13());
        }

        producto.setEan13(request.getEan13());
        producto.setNombre(request.getNombre().trim());
        producto.setDescripcion(request.getDescripcion());
        producto.setUnidadMedida(request.getUnidadMedida());
        producto.setValor(request.getValor());
        producto.setStockMinimo(request.getStockMinimo());

        log.info("Producto actualizado: {}", producto.getSku());
        return toResponse(productoRepository.save(producto));
    }

    @Transactional
    public ProductoResponse cambiarEstado(String sku, EstadoRequest request) {
        Producto producto = buscarSku(sku);
        producto.setActivo(request.getActivo());

        log.info("Producto {} -> activo={}", producto.getSku(), request.getActivo());
        return toResponse(productoRepository.save(producto));
    }

    private Producto buscarSku(String sku) {
        return productoRepository.findBySkuIgnoreCase(sku.trim())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + sku));
    }

    private ProductoResponse toResponse(Producto p) {
        return ProductoResponse.builder()
                .sku(p.getSku())
                .ean13(p.getEan13())
                .nombre(p.getNombre())
                .descripcion(p.getDescripcion())
                .unidadMedida(p.getUnidadMedida())
                .valor(p.getValor())
                .stockMinimo(p.getStockMinimo())
                .activo(p.getActivo())
                .build();
    }
}