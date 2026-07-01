package com.ggstore.ms_catalogo.controller;

import com.ggstore.ms_catalogo.dto.JuegoDTO;
import com.ggstore.ms_catalogo.service.JuegoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoControllerTest {

    @Mock private JuegoService juegoService;
    @InjectMocks private JuegoController juegoController;

    private UUID juegoId;
    private UUID categoriaId;
    private JuegoDTO juegoDTO;

    @BeforeEach
    void setUp() {
        juegoId = UUID.randomUUID();
        categoriaId = UUID.randomUUID();

        juegoDTO = new JuegoDTO();
        juegoDTO.setId(juegoId);
        juegoDTO.setTitulo("Cyberpunk 2077");
        juegoDTO.setPrecio(new BigDecimal("39990"));
        juegoDTO.setStock(50);
        juegoDTO.setCategoriaId(categoriaId);
    }

    @Test
    void listar_retornaPaginaDeJuegos() {
        Pageable pageable = PageRequest.of(0, 12);
        Page<JuegoDTO> page = new PageImpl<>(List.of(juegoDTO));

        when(juegoService.listar(null, null, pageable)).thenReturn(page);

        Page<JuegoDTO> result = juegoController.listar(null, null, 0, 12);

        assertThat(result.getContent()).hasSize(1);
        verify(juegoService).listar(null, null, pageable);
    }

    @Test
    void listar_conTituloYCategoria_pasaFiltrosCorrectamente() {
        Pageable pageable = PageRequest.of(1, 20);
        Page<JuegoDTO> page = new PageImpl<>(List.of(juegoDTO));

        when(juegoService.listar("Cyberpunk", categoriaId, pageable)).thenReturn(page);

        Page<JuegoDTO> result = juegoController.listar("Cyberpunk", categoriaId, 1, 20);

        assertThat(result.getContent()).hasSize(1);
        verify(juegoService).listar("Cyberpunk", categoriaId, pageable);
    }

    @Test
    void obtener_retornaJuego() {
        when(juegoService.obtenerPorId(juegoId)).thenReturn(juegoDTO);

        ResponseEntity<JuegoDTO> response = juegoController.obtener(juegoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(juegoDTO);
    }

    @Test
    void crear_retornaJuegoCreado() {
        when(juegoService.crear(juegoDTO)).thenReturn(juegoDTO);

        ResponseEntity<JuegoDTO> response = juegoController.crear(juegoDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(juegoService).crear(juegoDTO);
    }

    @Test
    void actualizar_retornaJuegoActualizado() {
        when(juegoService.actualizar(juegoId, juegoDTO)).thenReturn(juegoDTO);

        ResponseEntity<JuegoDTO> response = juegoController.actualizar(juegoId, juegoDTO);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(juegoService).actualizar(juegoId, juegoDTO);
    }

    @Test
    void descontarStock_retornaJuegoConStockActualizado() {
        when(juegoService.descontarStock(juegoId, 5)).thenReturn(juegoDTO);

        ResponseEntity<JuegoDTO> response = juegoController.descontarStock(juegoId, 5);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(juegoService).descontarStock(juegoId, 5);
    }

    @Test
    void eliminar_retornaNoContent() {
        ResponseEntity<Void> response = juegoController.eliminar(juegoId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(juegoService).eliminar(juegoId);
    }
}