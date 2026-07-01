package com.ggstore.ms_catalogo.service;

import com.ggstore.ms_catalogo.model.Categoria;
import com.ggstore.ms_catalogo.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock private CategoriaRepository categoriaRepository;
    @InjectMocks private CategoriaService categoriaService;

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
    void listarTodas_retornaListaDeCategorias() {
        when(categoriaRepository.findAll()).thenReturn(List.of(categoria));

        List<Categoria> result = categoriaService.listarTodas();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNombre()).isEqualTo("RPG");
    }

    @Test
    void obtenerPorId_retornaCategoria() {
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));

        Categoria result = categoriaService.obtenerPorId(categoriaId);

        assertThat(result.getNombre()).isEqualTo("RPG");
    }

    @Test
    void obtenerPorId_lanzaExcepcionSiNoExiste() {
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.obtenerPorId(categoriaId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrada");
    }

    @Test
    void crear_guardaCategoria() {
        when(categoriaRepository.save(categoria)).thenReturn(categoria);

        Categoria result = categoriaService.crear(categoria);

        assertThat(result).isEqualTo(categoria);
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void eliminar_llamaDeleteById() {
        categoriaService.eliminar(categoriaId);

        verify(categoriaRepository).deleteById(categoriaId);
    }
}
