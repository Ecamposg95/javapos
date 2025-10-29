package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Payment.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
