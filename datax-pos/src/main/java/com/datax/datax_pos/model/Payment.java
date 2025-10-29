package com.datax.datax_pos.model;

import com.datax.datax_pos.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad de Pago.
 * Registra CÓMO se pagó una orden.
 * Basado en tu contexto: payments(id, order_id, method, amount, change, ...)
 */
@Data
@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // La orden que se está pagando

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod method; // CASH, CARD, TRANSFER

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount; // Monto pagado con este método

    @Column(precision = 10, scale = 2)
    private BigDecimal change; // Cambio devuelto (usualmente solo en CASH)

    @ManyToOne
    @JoinColumn(name = "cashier_id", nullable = false)
    private User cashier; // Cajero que registró el pago

    private LocalDateTime createdAt; // Cuándo se registró el pago

    public Payment() {
        this.createdAt = LocalDateTime.now();
    }
}