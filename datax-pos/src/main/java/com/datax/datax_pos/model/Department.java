package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad para agrupar productos (ej. "Abarrotes", "Lácteos", "Ferretería")
 */
@Data
@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
}
