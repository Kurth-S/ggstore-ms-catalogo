package com.ggstore.ms_catalogo.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class JuegoDTO {
    private UUID id;
    private String titulo;
    private String descripcion;
    private Double precio;
    private Double precioFinal;
    private Integer stock;
    private String plataforma;
    private String imagenUrl;
    private Integer descuentoPorcentaje;
    private UUID categoriaId;
    private String categoriaNombre;
}
