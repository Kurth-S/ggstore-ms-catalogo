package com.ggstore.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "juegos")
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String titulo;

    private String descripcion;

    @Column(nullable = false)
    private BigDecimal precio;

    private Integer stock;

    private String plataforma;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "descuento_porcentaje", precision = 5, scale = 2)
    private BigDecimal descuentoPorcentaje = BigDecimal.ZERO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Transient
    public BigDecimal getPrecioFinal() {
        if (descuentoPorcentaje != null && descuentoPorcentaje.compareTo(BigDecimal.ZERO) > 0) {
            return precio.multiply(BigDecimal.ONE.subtract(
                    descuentoPorcentaje.divide(BigDecimal.valueOf(100))));
        }
        return precio;
    }
}