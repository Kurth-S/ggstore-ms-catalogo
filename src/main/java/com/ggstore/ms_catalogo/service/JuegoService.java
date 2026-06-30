package com.ggstore.ms_catalogo.service;

import com.ggstore.ms_catalogo.dto.JuegoDTO;
import com.ggstore.ms_catalogo.model.Juego;
import com.ggstore.ms_catalogo.repository.JuegoRepository;
import com.ggstore.ms_catalogo.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JuegoService {

    private final JuegoRepository juegoRepository;
    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public Page<JuegoDTO> listar(String titulo, UUID categoriaId, Pageable pageable) {
        Page<Juego> juegos;

        if (titulo != null && categoriaId != null) {
            juegos = juegoRepository.findByTituloContainingIgnoreCaseAndCategoriaId(titulo, categoriaId, pageable);
        } else if (titulo != null) {
            juegos = juegoRepository.findByTituloContainingIgnoreCase(titulo, pageable);
        } else if (categoriaId != null) {
            juegos = juegoRepository.findByCategoriaId(categoriaId, pageable);
        } else {
            juegos = juegoRepository.findAll(pageable);
        }

        return juegos.map(this::toDTO);
    }

    @Transactional(readOnly = true)
    public JuegoDTO obtenerPorId(UUID id) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        return toDTO(juego);
    }

    @Transactional
    public JuegoDTO crear(JuegoDTO dto) {
        Juego juego = new Juego();
        juego.setTitulo(dto.getTitulo());
        juego.setDescripcion(dto.getDescripcion());
        juego.setPrecio(dto.getPrecio());
        juego.setStock(dto.getStock());
        juego.setPlataforma(dto.getPlataforma());
        juego.setImagenUrl(dto.getImagenUrl());
        juego.setDescuentoPorcentaje(dto.getDescuentoPorcentaje());
        if (dto.getCategoriaId() != null) {
            categoriaRepository.findById(dto.getCategoriaId()).ifPresent(juego::setCategoria);
        }
        return toDTO(juegoRepository.save(juego));
    }

    @Transactional
    public JuegoDTO actualizar(UUID id, JuegoDTO dto) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));
        juego.setTitulo(dto.getTitulo());
        juego.setDescripcion(dto.getDescripcion());
        juego.setPrecio(dto.getPrecio());
        juego.setStock(dto.getStock());
        juego.setPlataforma(dto.getPlataforma());
        juego.setImagenUrl(dto.getImagenUrl());
        juego.setDescuentoPorcentaje(dto.getDescuentoPorcentaje());
        if (dto.getCategoriaId() != null) {
            categoriaRepository.findById(dto.getCategoriaId()).ifPresent(juego::setCategoria);
        }
        return toDTO(juegoRepository.save(juego));
    }

    @Transactional
    public void eliminar(UUID id) {
        juegoRepository.deleteById(id);
    }

    /**
     * Descuenta stock tras una compra confirmada en ms-pedidos.
     * Lanza excepción si no hay stock suficiente.
     */
    @Transactional
    public JuegoDTO descontarStock(UUID id, int cantidad) {
        Juego juego = juegoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Juego no encontrado"));

        if (juego.getStock() < cantidad) {
            throw new IllegalStateException("Stock insuficiente. Disponible: " + juego.getStock());
        }

        juego.setStock(juego.getStock() - cantidad);
        return toDTO(juegoRepository.save(juego));
    }

    private JuegoDTO toDTO(Juego juego) {
        JuegoDTO dto = new JuegoDTO();
        dto.setId(juego.getId());
        dto.setTitulo(juego.getTitulo());
        dto.setDescripcion(juego.getDescripcion());
        dto.setPrecio(juego.getPrecio());
        dto.setPrecioFinal(juego.getPrecioFinal());
        dto.setStock(juego.getStock());
        dto.setPlataforma(juego.getPlataforma());
        dto.setImagenUrl(juego.getImagenUrl());
        dto.setDescuentoPorcentaje(juego.getDescuentoPorcentaje());
        if (juego.getCategoria() != null) {
            dto.setCategoriaId(juego.getCategoria().getId());
            dto.setCategoriaNombre(juego.getCategoria().getNombre());
        }
        return dto;
    }
}
