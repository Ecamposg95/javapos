package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;
// Asegúrate de que este import esté presente
import jakarta.persistence.CascadeType;

/**
 * Entidad de Producto (Versión Completa).
 * Incluye campos básicos, costo, control de inventario y relación con Departamento.
 * Los precios se manejan a través de ProductPrice.
 */
@Data
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku; // Código único interno

    @Column(unique = true) // Puede ser nulo si no se usa
    private String barcode; // Código de barras

    @Column(nullable = false)
    private String name; // Nombre del producto

    private String description; // Descripción más detallada

    /**
     * Relación Muchos-a-Uno con Departamento.
     * cascade = CascadeType.MERGE: Le dice a JPA que si el Departamento
     * ya existe en la BD (como en nuestro DataLoader), simplemente lo
     * "fusione" o actualice al guardar el Producto, en lugar de tratarlo
     * como una entidad nueva o desconectada.
     */
    @ManyToOne(cascade = CascadeType.MERGE) // <-- CAMBIO CLAVE AQUÍ
    @JoinColumn(name = "department_id") // Nombre de la columna de clave foránea
    private Department department;

    @Column(nullable = false)
    private String unit; // Unidad de medida (Pza, Kg, Lt)

    @Column(precision = 10, scale = 2)
    private BigDecimal cost; // Costo del producto

    private boolean usesInventory = true; // ¿Este producto maneja inventario?

    @Column(precision = 10, scale = 2)
    private BigDecimal minStock; // Inventario mínimo deseado

    @Column(precision = 10, scale = 2)
    private BigDecimal maxStock; // Inventario máximo deseado

    private String imageUrl; // URL de la imagen (opcional)

    // Relación Uno-a-Muchos con ProductPrice (para los precios)
    // No necesitamos Cascade aquí, los precios se manejan por separado.
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductPrice> prices;

    // Relación Uno-a-Muchos con Stock (para la existencia por sucursal)
    // No necesitamos Cascade aquí.
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Stock> stockEntries;

    // Constructores, Getters, Setters, etc., generados por Lombok (@Data)
}
