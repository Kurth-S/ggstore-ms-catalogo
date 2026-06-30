package com.ggstore.ms_catalogo.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ResenaDTO {
    private UUID id;
    private UUID juegoId;
    private UUID usuarioId;
    private Integer calificacion;
    private String comentario;
    private LocalDateTime createdAt;
}
