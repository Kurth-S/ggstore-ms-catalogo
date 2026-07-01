package com.ggstore.ms_catalogo.controller;

import com.ggstore.ms_catalogo.dto.ResenaDTO;
import com.ggstore.ms_catalogo.service.ResenaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaControllerTest {

    @Mock private ResenaService resenaService;
    @InjectMocks private ResenaController resenaController;

    private UUID juegoId;
    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {
        juegoId = UUID.randomUUID();

        resenaDTO = new ResenaDTO();
        resenaDTO.setJuegoId(juegoId);
        resenaDTO.setUsuarioId(UUID.randomUUID());
        resenaDTO.setPuntuacion(5);
        resenaDTO.setComentario("Excelente juego");
    }

    @Test
    void listarPorJuego_retornaListaDeResenas() {
        when(resenaService.listarPorJuego(juegoId)).thenReturn(List.of(resenaDTO));

        List<ResenaDTO> result = resenaController.listarPorJuego(juegoId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getComentario()).isEqualTo("Excelente juego");
    }

    @Test
    void crear_retornaResenaCreada() {
        when(resenaService.crear(resenaDTO)).thenReturn(resenaDTO);

        ResponseEntity<ResenaDTO> response = resenaController.crear(resenaDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(resenaService).crear(resenaDTO);
    }

    @Test
    void eliminar_retornaNoContent() {
        UUID resenaId = UUID.randomUUID();

        ResponseEntity<Void> response = resenaController.eliminar(resenaId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(resenaService).eliminar(resenaId);
    }
}
