package com.ggstore.ms_catalogo.controller;

import com.ggstore.ms_catalogo.dto.JuegoDTO;
import com.ggstore.ms_catalogo.service.JuegoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/juegos")
@RequiredArgsConstructor
public class JuegoController {

    private final JuegoService juegoService;

    @GetMapping
    public Page<JuegoDTO> listar(
            @RequestParam(required = false) String titulo,
            @RequestParam(required = false) UUID categoriaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return juegoService.listar(titulo, categoriaId, PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoDTO> obtener(@PathVariable UUID id) {
        return ResponseEntity.ok(juegoService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<JuegoDTO> crear(@RequestBody JuegoDTO dto) {
        return ResponseEntity.ok(juegoService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoDTO> actualizar(@PathVariable UUID id, @RequestBody JuegoDTO dto) {
        return ResponseEntity.ok(juegoService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<JuegoDTO> descontarStock(
            @PathVariable UUID id,
            @RequestParam int cantidad) {
        return ResponseEntity.ok(juegoService.descontarStock(id, cantidad));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        juegoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}