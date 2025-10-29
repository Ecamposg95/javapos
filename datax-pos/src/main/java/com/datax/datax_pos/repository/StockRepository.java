package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Branch;
import com.datax.datax_pos.model.Product;
import com.datax.datax_pos.model.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad Stock.
 */
@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    // Busca el stock de UN producto en UNA sucursal
    Optional<Stock> findByBranchAndProduct(Branch branch, Product product);
}
