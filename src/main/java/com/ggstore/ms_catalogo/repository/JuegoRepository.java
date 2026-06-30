package com.ggstore.ms_catalogo.repository;

import com.ggstore.ms_catalogo.model.Juego;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface JuegoRepository extends JpaRepository<Juego, UUID> {

    Page<Juego> findByTituloContainingIgnoreCase(String titulo, Pageable pageable);

    Page<Juego> findByCategoriaId(UUID categoriaId, Pageable pageable);

    Page<Juego> findByTituloContainingIgnoreCaseAndCategoriaId(String titulo, UUID categoriaId, Pageable pageable);
}
