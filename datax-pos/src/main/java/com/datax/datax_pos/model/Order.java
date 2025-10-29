package com.datax.datax_pos.model; // Asegúrate que el package sea el tuyo

import com.datax.datax_pos.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de Pedido (Orden).
 * Cabecera de la venta.
 * Corregida para incluir subtotal, taxAmount y total.
 */
@Data
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch; // Sucursal donde se crea

    @ManyToOne
    @JoinColumn(name = "seller_id") // Puede ser nulo si es venta de caja
    private User seller; // Vendedor que levantó el pedido

    private String customerName; // Nombre del cliente (para modo mayorista)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status; // DRAFT, READY_TO_PAY, etc.

    // Timestamps del ciclo de vida
    private LocalDateTime openedAt;
    private LocalDateTime readyAt;
    private LocalDateTime paidAt;

    // --- CAMPOS CORREGIDOS ---
    // Añadimos los campos que faltaban y que el DTO necesita

    @Column(precision = 10, scale = 2)
    private BigDecimal subtotal; // <-- AÑADIDO

    @Column(precision = 10, scale = 2)
    private BigDecimal taxAmount; // <-- AÑADIDO

    @Column(precision = 10, scale = 2)
    private BigDecimal total; // <-- RENOMBRADO (antes totalAmount)
    // --- FIN DE CAMPOS CORREGIDOS ---


    /**
     * Relación Uno-a-Muchos: Una orden tiene muchos items.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    // Constructor para inicializar
    public Order() {
        this.status = OrderStatus.DRAFT;
        this.openedAt = LocalDateTime.now();
        this.subtotal = BigDecimal.ZERO;
        this.taxAmount = BigDecimal.ZERO;
        this.total = BigDecimal.ZERO;
    }
}

