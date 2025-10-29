package com.datax.datax_pos.dto;

import lombok.Data;

/**
 * DTO para la solicitud de "Venta Rápida" desde caja.
 */
@Data
public class CreateOrderRequestDTO {
    private Long cashierId; // ID del cajero que crea la venta
    private Long branchId;  // ID de la sucursal
    private String customerName; // Nombre inicial (ej. "Cliente Mostrador")
}
