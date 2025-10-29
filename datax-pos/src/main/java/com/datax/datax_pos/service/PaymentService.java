package com.datax.datax_pos.service;

import com.datax.datax_pos.dto.OrderDTO;
import com.datax.datax_pos.dto.PaymentRequestDTO;

public interface PaymentService {

    /**
     * Procesa el pago de una orden.
     *
     * @param orderId El ID de la orden a pagar.
     * @param request El DTO con los detalles del pago (metodo, cantidad, cajero).
     * @return La orden actualizada con estado PAID.
     */
    OrderDTO processPayment(Long orderId, PaymentRequestDTO request);
}
