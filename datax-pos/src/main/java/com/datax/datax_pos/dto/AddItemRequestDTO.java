package com.datax.datax_pos.dto;

import lombok.Data;
import lombok.NoArgsConstructor; // Añadir constructor sin argumentos para deserialización JSON
import lombok.AllArgsConstructor; // Añadir constructor con todos los argumentos (opcional)
import java.math.BigDecimal;

/**
 * DTO para la solicitud de añadir un ítem a una orden.
 * Se envía desde el frontend al backend.
 */
@Data // Lombok genera getters, setters, etc.
@NoArgsConstructor // Necesario para Jackson (deserialización JSON)
@AllArgsConstructor // Conveniente para crear instancias
public class AddItemRequestDTO {

    // El identificador puede ser SKU o Barcode
    private String productIdentifier; // Lombok debe generar getProductIdentifier()

    private BigDecimal quantity;

    // Podríamos añadir aquí campos opcionales como descuento aplicado por el cajero
}

