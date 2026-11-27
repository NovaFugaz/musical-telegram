package com.bexai.inventoryservice.controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bexai.inventoryservice.dto.JornadaRequest;
import com.bexai.inventoryservice.dto.JornadaResponse;
import com.bexai.inventoryservice.service.JornadaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping({"/api/jornadas", "/jornadas"})
@RequiredArgsConstructor
public class JornadaController {

    private final JornadaService jornadaService;

    @GetMapping("/activa")
    public ResponseEntity<JornadaResponse> obtenerActivaActual(Principal principal) {
        return ResponseEntity.ok(jornadaService.obtenerActiva(principal.getName()));
    }

    @GetMapping("/activa/{operadorCodigo}")
    public ResponseEntity<JornadaResponse> obtenerActivaPorCodigo(@PathVariable String operadorCodigo) {
        return ResponseEntity.ok(jornadaService.obtenerActiva(operadorCodigo));
    }

    @PostMapping("/iniciar")
    public ResponseEntity<JornadaResponse> iniciar(
            @Valid @RequestBody JornadaRequest request,
            Principal principal) {
        String operador = (principal != null) ? principal.getName() : null;
        return ResponseEntity.status(HttpStatus.CREATED).body(jornadaService.iniciar(request, operador));
    }

    @PostMapping("/terminar")
    public ResponseEntity<JornadaResponse> terminar(
            @RequestParam(required = false) String operadorCodigo,
            Principal principal) {
        String codigo = (operadorCodigo != null && !operadorCodigo.isBlank())
                ? operadorCodigo
                : principal.getName();
        return ResponseEntity.ok(jornadaService.terminar(codigo));
    }
}
