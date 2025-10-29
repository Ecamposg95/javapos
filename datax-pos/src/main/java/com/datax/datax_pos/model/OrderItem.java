package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode; // Para evitar recursión en toString/equals

import java.math.BigDecimal;

/**
 * Entidad que representa un ítem (línea) dentro de un Pedido.
 * Basado en tu contexto: order_items(id, order_id, product_id, qty, unit_price, ...)
 */
@Data
@Entity
@Table(name = "order_items")
@EqualsAndHashCode(exclude = "order") // Evita recursión infinita con Order
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Relación Muchos-a-Uno: Muchos ítems pertenecen a una Orden.
     * fetch = FetchType.LAZY -> No cargar la Orden completa al cargar un ítem.
     * nullable = false -> Un ítem siempre debe pertenecer a una orden.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    /**
     * Relación Muchos-a-Uno: Muchos ítems pueden referirse al mismo Producto.
     * nullable = false -> Un ítem siempre debe referirse a un producto.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity; // Cantidad vendida

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice; // Precio unitario al momento de la venta

    @Column(precision = 10, scale = 2)
    private BigDecimal discountAmount; // Monto de descuento aplicado a esta línea

    @Column(precision = 5, scale = 4) // Ej: 0.1600 para 16%
    private BigDecimal taxRate; // Tasa de impuesto aplicada (puede variar por producto)

    /**
     * **NUEVO CAMPO:** Total calculado para esta línea (cantidad * precio_unitario - descuento)
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal lineTotal;

    // Constructor vacío requerido por JPA
    public OrderItem() {}

    // Podrías añadir un constructor con parámetros si lo necesitas
}
