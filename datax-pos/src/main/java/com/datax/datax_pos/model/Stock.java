package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Entidad de Stock (Inventario).
 * Representa la cantidad de un producto en una sucursal específica.
 * Basado en tu contexto: stock(branch_id, product_id, qty_on_hand)
 */
@Data
@Entity
@Table(name = "stock", uniqueConstraints = {
        // Creamos una restricción para que no pueda haber dos entradas
        // del mismo producto en la misma sucursal.
        @UniqueConstraint(columnNames = {"branch_id", "product_id"})
})
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch; // La sucursal

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product; // El producto

    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal qtyOnHand; // Cantidad disponible
}
