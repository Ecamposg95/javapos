package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad PriceList.
 */
@Repository
public interface PriceListRepository extends JpaRepository<PriceList, Long> {
}
