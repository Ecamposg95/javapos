package com.datax.datax_pos.model.enums;

/**
 * Estados del Pedido (Orden)
 * Basado en tu contexto: DRAFT → READY_TO_PAY → PAID (↘ CANCELED)
 */
public enum OrderStatus {
    DRAFT,
    READY_TO_PAY,
    PAID,
    CANCELED
}
