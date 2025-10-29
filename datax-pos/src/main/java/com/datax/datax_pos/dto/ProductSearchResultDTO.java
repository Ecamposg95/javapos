package com.datax.datax_pos.dto;

import com.datax.datax_pos.model.Product;
import lombok.Data;
import java.math.BigDecimal;

/**
 * DTO (Data Transfer Object) para devolver en la búsqueda de productos.
 * Contiene solo la info que el frontend (pos_cajero.html) necesita.
 */
@Data
public class ProductSearchResultDTO {
    private Long id;
    private String sku;
    private String name;
    private String unit;
    private BigDecimal price; // El precio de menudeo (precio1)

    // Constructor para mapear fácil
    public ProductSearchResultDTO(Product product, BigDecimal menudeoPrice) {
        this.id = product.getId();
        this.sku = product.getSku();
        this.name = product.getName();
        this.unit = product.getUnit();
        this.price = menudeoPrice;
    }
}
