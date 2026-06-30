package com.ggstore.ms_catalogo.repository;

import com.ggstore.ms_catalogo.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ResenaRepository extends JpaRepository<Resena, UUID> {

    List<Resena> findByJuegoId(UUID juegoId);
}
