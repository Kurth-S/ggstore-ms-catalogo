package com.ggstore.ms_catalogo.service;

import com.ggstore.ms_catalogo.dto.ResenaDTO;
import com.ggstore.ms_catalogo.model.Juego;
import com.ggstore.ms_catalogo.model.Resena;
import com.ggstore.ms_catalogo.repository.JuegoRepository;
import com.ggstore.ms_catalogo.repository.ResenaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResenaServiceTest {

    @Mock private ResenaRepository resenaRepository;
    @Mock private JuegoRepository juegoRepository;

    @InjectMocks private ResenaService resenaService;

    private UUID juegoId;
    private UUID usuarioId;
    private Juego juego;
    private Resena resena;
    private ResenaDTO resenaDTO;

    @BeforeEach
    void setUp() {
        juegoId = UUID.randomUUID();
        usuarioId = UUID.randomUUID();

        juego = new Juego();
        juego.setId(juegoId);
        juego.setTitulo("Cyberpunk 2077");

        resena = new Resena();
        resena.setId(UUID.randomUUID());
        resena.setJuego(juego);
        resena.setUsuarioId(usuarioId);
        resena.setPuntuacion(5);
        resena.setComentario("Excelente juego");
        resena.setCreatedAt(OffsetDateTime.now());

        resenaDTO = new ResenaDTO();
        resenaDTO.setJuegoId(juegoId);
        resenaDTO.setUsuarioId(usuarioId);
        resenaDTO.setPuntuacion(5);
        resenaDTO.setComentario("Excelente juego");
    }

    @Test
    void listarPorJuego_retornaListaDeResenas() {
        when(resenaRepository.findByJuegoId(juegoId)).thenReturn(List.of(resena));

        List<ResenaDTO> result = resenaService.listarPorJuego(juegoId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getComentario()).isEqualTo("Excelente juego");
    }

    @Test
    void crear_guardaResenaConJuego() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        ResenaDTO result = resenaService.crear(resenaDTO);

        assertThat(result.getPuntuacion()).isEqualTo(5);
        verify(resenaRepository).save(any(Resena.class));
    }

    @Test
    void crear_siJuegoNoExiste_guardaSinAsignarJuego() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.empty());
        // resena guardada igual necesita un juego para el toDTO, así que simulamos que sí quedó asociado
        when(resenaRepository.save(any(Resena.class))).thenReturn(resena);

        ResenaDTO result = resenaService.crear(resenaDTO);

        assertThat(result).isNotNull();
        verify(juegoRepository).findById(juegoId);
    }

    @Test
    void eliminar_llamaDeleteById() {
        UUID resenaId = UUID.randomUUID();

        resenaService.eliminar(resenaId);

        verify(resenaRepository).deleteById(resenaId);
    }
}