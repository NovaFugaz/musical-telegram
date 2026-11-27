package com.bexai.catalogservice.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bexai.catalogservice.dto.SucursalRequest;
import com.bexai.catalogservice.dto.SucursalResponse;
import com.bexai.catalogservice.exception.RecursoDuplicadoException;
import com.bexai.catalogservice.exception.RecursoNoEncontradoException;
import com.bexai.catalogservice.model.Sucursal;
import com.bexai.catalogservice.repository.SucursalRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SucursalService {

    private final SucursalRepository sucursalRepository;

    @Transactional(readOnly = true)
    public List<SucursalResponse> listar() {
        return sucursalRepository.findAll(Sort.by("idSucursal")).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SucursalResponse obtener(Long id) {
        return toResponse(buscar(id));
    }

    @Transactional
    public SucursalResponse crear(SucursalRequest request) {
        String nombre = request.getNombreSucursal().trim();

        if (sucursalRepository.existsByNombreSucursalIgnoreCase(nombre)) {
            throw new RecursoDuplicadoException("Ya existe una sucursal con nombre: " + nombre);
        }

        Sucursal sucursal = new Sucursal();
        sucursal.setIdSucursal(sucursalRepository.obtenerMaxId() + 1);
        aplicar(sucursal, request);

        log.info("Sucursal creada: {}", sucursal.getIdSucursal());
        return toResponse(sucursalRepository.save(sucursal));
    }

    @Transactional
    public SucursalResponse actualizar(Long id, SucursalRequest request) {
        Sucursal sucursal = buscar(id);
        String nombre = request.getNombreSucursal().trim();

        if (sucursalRepository.existsByNombreSucursalIgnoreCaseAndIdSucursalNot(nombre, id)) {
            throw new RecursoDuplicadoException("Ya existe otra sucursal con nombre: " + nombre);
        }

        aplicar(sucursal, request);

        log.info("Sucursal actualizada: {}", id);
        return toResponse(sucursalRepository.save(sucursal));
    }

    private Sucursal buscar(Long id) {
        return sucursalRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Sucursal no encontrada: " + id));
    }

    private void aplicar(Sucursal s, SucursalRequest r) {
        s.setNombreSucursal(r.getNombreSucursal().trim());
        s.setDireccion(r.getDireccion());
        s.setLatitud(r.getLatitud());
        s.setLongitud(r.getLongitud());
        s.setCiudad(r.getCiudad());
        s.setRegion(r.getRegion());
    }

    private SucursalResponse toResponse(Sucursal s) {
        return SucursalResponse.builder()
                .idSucursal(s.getIdSucursal())
                .nombreSucursal(s.getNombreSucursal())
                .direccion(s.getDireccion())
                .latitud(s.getLatitud())
                .longitud(s.getLongitud())
                .ciudad(s.getCiudad())
                .region(s.getRegion())
                .build();
    }
}