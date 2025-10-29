package com.datax.datax_pos.dto;

import com.datax.datax_pos.model.enums.PaymentMethod;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO para la solicitud de pago.
 * Esto es lo que el frontend (HTML) enviará en el body del POST.
 */
@Data
public class PaymentRequestDTO {
    private Long cashierId; // ID del cajero que procesa el pago
    private PaymentMethod method; // EFECTIVO, TARJETA, TRANSFERENCIA
    private BigDecimal amountReceived; // Cantidad recibida (ej. billete de $500)
}
