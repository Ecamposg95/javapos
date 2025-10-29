package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que define los tipos de precio.
 * Basado en tu contexto: price_lists(id, name)
 * Aquí irían nombres como "Menudeo", "Mayoreo", "Precio Caja", "Precio 4", "Precio 5"
 */
@Data
@Entity
@Table(name = "price_lists")
public class PriceList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // ej. "Menudeo", "Mayoreo"
}
