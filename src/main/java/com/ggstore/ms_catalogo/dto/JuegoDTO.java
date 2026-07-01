package com.ggstore.ms_catalogo.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class JuegoDTO {
    private UUID id;
    private String titulo;
    private String descripcion;
    private BigDecimal precio;
    private BigDecimal precioFinal;
    private Integer stock;
    private String plataforma;
    private String imagenUrl;
    private BigDecimal descuentoPorcentaje;
    private UUID categoriaId;
    private String categoriaNombre;
}