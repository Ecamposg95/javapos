package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa una Sucursal (Branch).
 * Basado en tu contexto: branches(id, name)
 */
@Data // Genera getters, setters, toString, equals, hashCode
@Entity // Le dice a JPA que esta clase es una tabla de BD
@Table(name = "branches") // Nombre de la tabla en la BD
public class Branch {

    @Id // Marca este campo como la Llave Primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Autoincremental
    private Long id;

    @Column(nullable = false, unique = true) // No puede ser nulo, debe ser único
    private String name;
}
