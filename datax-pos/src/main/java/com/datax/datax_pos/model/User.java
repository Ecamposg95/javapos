package com.datax.datax_pos.model;

import com.datax.datax_pos.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidad que representa un Usuario (User).
 * Basado en tu contexto: users(id, name, role, branch_id, pin_hash)
 */
@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING) // Guarda el Enum como texto (ej. "VENDEDOR")
    @Column(nullable = false)
    private UserRole role;

    @Column(nullable = false)
    private String pinHash; // Almacena el PIN encriptado

    // --- Relaciones ---

    /**
     * Relación Muchos-a-Uno: Muchos usuarios pertenecen a UNA sucursal.
     * Esto crea automáticamente la columna 'branch_id'.
     */
    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
}
