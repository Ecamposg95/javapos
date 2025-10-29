package com.datax.datax_pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor; // Necesario para Jackson
import lombok.AllArgsConstructor; // Opcional
import java.math.BigDecimal;

/**
 * DTO para representar un Ítem dentro de una Orden.
 */
@Data
@NoArgsConstructor // Asegurar constructor vacío
@AllArgsConstructor // Opcional
public class OrderItemDTO {

    private Long id; // Puede ser null si es un ítem nuevo
    private Long productId;
    private String sku;
    private String productName; // Nombre corregido para coincidir
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal discountAmount; // Nombre corregido para coincidir
    private BigDecimal lineTotal; // Nombre corregido para coincidir

}

