package com.ggstore.ms_catalogo.controller;

import com.ggstore.ms_catalogo.dto.ResenaDTO;
import com.ggstore.ms_catalogo.service.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    @GetMapping("/juego/{juegoId}")
    public List<ResenaDTO> listarPorJuego(@PathVariable UUID juegoId) {
        return resenaService.listarPorJuego(juegoId);
    }

    @PostMapping
    public ResponseEntity<ResenaDTO> crear(@RequestBody ResenaDTO dto) {
        return ResponseEntity.ok(resenaService.crear(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        resenaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
