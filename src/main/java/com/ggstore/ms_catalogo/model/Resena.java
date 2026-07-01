package com.ggstore.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "resenas")
public class Resena {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false)
    private Juego juego;

    @Column(name = "usuario_id", nullable = false)
    private UUID usuarioId;

    @Column(name = "puntuacion", nullable = false)
    private Integer puntuacion;

    private String comentario;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;
}