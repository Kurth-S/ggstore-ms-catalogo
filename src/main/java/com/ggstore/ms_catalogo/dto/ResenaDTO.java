package com.ggstore.ms_catalogo.dto;

import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
public class ResenaDTO {
    private UUID id;
    private UUID juegoId;
    private UUID usuarioId;
    private Integer puntuacion;
    private String comentario;
    private OffsetDateTime createdAt;
}