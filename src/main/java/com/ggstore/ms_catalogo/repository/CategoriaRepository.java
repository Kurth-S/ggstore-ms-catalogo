package com.ggstore.ms_catalogo.repository;

import com.ggstore.ms_catalogo.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, UUID> {
}
