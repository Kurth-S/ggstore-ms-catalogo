package com.ggstore.ms_catalogo.service;

import com.ggstore.ms_catalogo.dto.JuegoDTO;
import com.ggstore.ms_catalogo.model.Categoria;
import com.ggstore.ms_catalogo.model.Juego;
import com.ggstore.ms_catalogo.repository.CategoriaRepository;
import com.ggstore.ms_catalogo.repository.JuegoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JuegoServiceTest {

    @Mock private JuegoRepository juegoRepository;
    @Mock private CategoriaRepository categoriaRepository;

    @InjectMocks private JuegoService juegoService;

    private UUID juegoId;
    private UUID categoriaId;
    private Juego juego;
    private Categoria categoria;
    private JuegoDTO juegoDTO;

    @BeforeEach
    void setUp() {
        juegoId = UUID.randomUUID();
        categoriaId = UUID.randomUUID();

        categoria = new Categoria();
        categoria.setId(categoriaId);
        categoria.setNombre("RPG");

        juego = new Juego();
        juego.setId(juegoId);
        juego.setTitulo("Cyberpunk 2077");
        juego.setDescripcion("RPG futurista");
        juego.setPrecio(new BigDecimal("39990"));
        juego.setStock(50);
        juego.setPlataforma("PC");
        juego.setImagenUrl("https://img.url");
        juego.setDescuentoPorcentaje(new BigDecimal("20"));
        juego.setCategoria(categoria);

        juegoDTO = new JuegoDTO();
        juegoDTO.setTitulo("Cyberpunk 2077");
        juegoDTO.setDescripcion("RPG futurista");
        juegoDTO.setPrecio(new BigDecimal("39990"));
        juegoDTO.setStock(50);
        juegoDTO.setPlataforma("PC");
        juegoDTO.setImagenUrl("https://img.url");
        juegoDTO.setDescuentoPorcentaje(new BigDecimal("20"));
        juegoDTO.setCategoriaId(categoriaId);
    }

    @Test
    void listar_sinFiltros_retornaTodos() {
        Pageable pageable = Pageable.unpaged();
        Page<Juego> page = new PageImpl<>(List.of(juego));

        when(juegoRepository.findAll(pageable)).thenReturn(page);

        Page<JuegoDTO> result = juegoService.listar(null, null, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("Cyberpunk 2077");
    }

    @Test
    void listar_conTituloYCategoria_filtraCorrectamente() {
        Pageable pageable = Pageable.unpaged();
        Page<Juego> page = new PageImpl<>(List.of(juego));

        when(juegoRepository.findByTituloContainingIgnoreCaseAndCategoriaId("Cyberpunk", categoriaId, pageable))
                .thenReturn(page);

        Page<JuegoDTO> result = juegoService.listar("Cyberpunk", categoriaId, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(juegoRepository).findByTituloContainingIgnoreCaseAndCategoriaId("Cyberpunk", categoriaId, pageable);
    }

    @Test
    void listar_soloConTitulo_filtraCorrectamente() {
        Pageable pageable = Pageable.unpaged();
        Page<Juego> page = new PageImpl<>(List.of(juego));

        when(juegoRepository.findByTituloContainingIgnoreCase("Cyberpunk", pageable)).thenReturn(page);

        Page<JuegoDTO> result = juegoService.listar("Cyberpunk", null, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(juegoRepository).findByTituloContainingIgnoreCase("Cyberpunk", pageable);
    }

    @Test
    void listar_soloConCategoria_filtraCorrectamente() {
        Pageable pageable = Pageable.unpaged();
        Page<Juego> page = new PageImpl<>(List.of(juego));

        when(juegoRepository.findByCategoriaId(categoriaId, pageable)).thenReturn(page);

        Page<JuegoDTO> result = juegoService.listar(null, categoriaId, pageable);

        assertThat(result.getContent()).hasSize(1);
        verify(juegoRepository).findByCategoriaId(categoriaId, pageable);
    }

    @Test
    void obtenerPorId_retornaJuego() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));

        JuegoDTO result = juegoService.obtenerPorId(juegoId);

        assertThat(result.getTitulo()).isEqualTo("Cyberpunk 2077");
        assertThat(result.getCategoriaNombre()).isEqualTo("RPG");
    }

    @Test
    void obtenerPorId_lanzaExcepcionSiNoExiste() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> juegoService.obtenerPorId(juegoId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    void crear_guardaJuegoConCategoria() {
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(juegoRepository.save(any(Juego.class))).thenReturn(juego);

        JuegoDTO result = juegoService.crear(juegoDTO);

        assertThat(result.getTitulo()).isEqualTo("Cyberpunk 2077");
        verify(juegoRepository).save(any(Juego.class));
    }

    @Test
    void crear_sinCategoria_guardaSinAsignarCategoria() {
        juegoDTO.setCategoriaId(null);
        Juego juegoSinCategoria = new Juego();
        juegoSinCategoria.setId(juegoId);
        juegoSinCategoria.setTitulo("Juego sin categoria");
        juegoSinCategoria.setPrecio(new BigDecimal("1000"));

        when(juegoRepository.save(any(Juego.class))).thenReturn(juegoSinCategoria);

        JuegoDTO result = juegoService.crear(juegoDTO);

        assertThat(result).isNotNull();
        verify(categoriaRepository, never()).findById(any());
    }

    @Test
    void actualizar_modificaJuegoExistente() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));
        when(categoriaRepository.findById(categoriaId)).thenReturn(Optional.of(categoria));
        when(juegoRepository.save(any(Juego.class))).thenReturn(juego);

        JuegoDTO result = juegoService.actualizar(juegoId, juegoDTO);

        assertThat(result.getTitulo()).isEqualTo("Cyberpunk 2077");
        verify(juegoRepository).save(any(Juego.class));
    }

    @Test
    void actualizar_lanzaExcepcionSiNoExiste() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> juegoService.actualizar(juegoId, juegoDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }

    @Test
    void eliminar_llamaDeleteById() {
        juegoService.eliminar(juegoId);

        verify(juegoRepository).deleteById(juegoId);
    }

    @Test
    void descontarStock_descuentaCorrectamente() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));
        when(juegoRepository.save(any(Juego.class))).thenReturn(juego);

        JuegoDTO result = juegoService.descontarStock(juegoId, 5);

        assertThat(juego.getStock()).isEqualTo(45);
        verify(juegoRepository).save(juego);
    }

    @Test
    void descontarStock_lanzaExcepcionSiStockInsuficiente() {
        juego.setStock(2);
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.of(juego));

        assertThatThrownBy(() -> juegoService.descontarStock(juegoId, 5))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Stock insuficiente");
    }

    @Test
    void descontarStock_lanzaExcepcionSiJuegoNoExiste() {
        when(juegoRepository.findById(juegoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> juegoService.descontarStock(juegoId, 1))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no encontrado");
    }
}