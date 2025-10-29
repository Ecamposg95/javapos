package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Product;
import com.datax.datax_pos.model.ProductPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad ProductPrice.
 */
@Repository
public interface ProductPriceRepository extends JpaRepository<ProductPrice, Long> {

    // Encuentra todos los precios de un producto, ordenados por cantidad
    List<ProductPrice> findByProductOrderByMinQuantityAsc(Product product);

    /**
     * Esta es la consulta MÁS IMPORTANTE del POS.
     * Dado un producto y una cantidad, nos da el precio correcto.
     * Busca el precio más alto (ORDER BY minQuantity DESC)
     * donde la cantidad que pedimos (ej. 15) es MAYOR O IGUAL
     * a la cantidad mínima requerida (ej. 10 de mayoreo).
     * 'LIMIT 1' nos da solo el mejor precio.
     */
    @Query("SELECT pp FROM ProductPrice pp WHERE pp.product = ?1 AND pp.minQuantity <= ?2 ORDER BY pp.minQuantity DESC LIMIT 1")
    Optional<ProductPrice> findBestPriceForQuantity(Product product, BigDecimal quantity);
}
