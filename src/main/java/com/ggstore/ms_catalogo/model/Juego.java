package com.ggstore.ms_catalogo.model;

import jakarta.persistence.*;
import lombok.Data;
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
    private Double precio;

    private Integer stock;

    private String plataforma;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "descuento_porcentaje")
    private Integer descuentoPorcentaje = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Transient
    public Double getPrecioFinal() {
        if (descuentoPorcentaje != null && descuentoPorcentaje > 0) {
            return precio * (1 - descuentoPorcentaje / 100.0);
        }
        return precio;
    }
}
