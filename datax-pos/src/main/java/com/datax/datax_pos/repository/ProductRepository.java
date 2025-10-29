package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Product.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Búsqueda por SKU (para la caja)
    Optional<Product> findBySku(String sku);

    // Búsqueda por código de barras (para la caja)
    Optional<Product> findByBarcode(String barcode);

    // Búsqueda por nombre (para el frontend)
    // 'ContainingIgnoreCase' es como un "LIKE %nombre%" insensible a mayúsculas
    List<Product> findByNameContainingIgnoreCase(String name);
}
