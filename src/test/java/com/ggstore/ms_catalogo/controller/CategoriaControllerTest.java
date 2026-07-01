package com.ggstore.ms_catalogo.controller;

import com.ggstore.ms_catalogo.model.Categoria;
import com.ggstore.ms_catalogo.service.CategoriaService;
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
class CategoriaControllerTest {

    @Mock private CategoriaService categoriaService;
    @InjectMocks private CategoriaController categoriaController;

    private UUID categoriaId;
    private Categoria categoria;

    @BeforeEach
    void setUp() {
        categoriaId = UUID.randomUUID();
        categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setNombre("RPG");
    }

    @Test
    void listar_retornaListaDeCategorias() {
        when(categoriaService.listarTodas()).thenReturn(List.of(categoria));

        List<Categoria> result = categoriaController.listar();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("RPG");
    }

    @Test
    void obtener_retornaCategoria() {
        when(categoriaService.obtenerPorId(categoriaId)).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.obtener(categoriaId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getNombre()).isEqualTo("RPG");
    }

    @Test
    void crear_retornaCategoriaCreada() {
        when(categoriaService.crear(categoria)).thenReturn(categoria);

        ResponseEntity<Categoria> response = categoriaController.crear(categoria);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(categoriaService).crear(categoria);
    }

    @Test
    void eliminar_retornaNoContent() {
        ResponseEntity<Void> response = categoriaController.eliminar(categoriaId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(categoriaService).eliminar(categoriaId);
    }
}