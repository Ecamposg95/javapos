package com.datax.datax_pos.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

/**
 * Entidad que vincula un Producto con una Lista de Precios y un precio.
 * Basado en tu contexto: product_prices(product_id, price_list_id, unit_price)
 * ACTUALIZADO: con minQuantity para tu lógica de mayoreo.
 */
@Data
@Entity
@Table(name = "product_prices")
public class ProductPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "price_list_id", nullable = false)
    private PriceList priceList;

    @Column(nullable = false, precision = 10, scale = 2) // 10 dígitos, 2 decimales para dinero
    private BigDecimal unitPrice;

    /**
     * Campo clave para tu lógica de mayoreo.
     * Si es "Menudeo", minQuantity = 0
     * Si es "Mayoreo", minQuantity = 10 (ej. "precio 2 a partir de 10 piezas")
     * Si es "Precio Caja", minQuantity = 50 (ej. "precio 3 a partir de 50 piezas")
     */
    @Column(nullable = false, precision = 10, scale = 3, columnDefinition = "DECIMAL(10,3) DEFAULT 0.0")
    private BigDecimal minQuantity;
}
