package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Branch.
 * JpaRepository nos da métodos CRUD (Create, Read, Update, Delete) gratis.
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    // Aquí podemos añadir búsquedas personalizadas si las necesitamos
    // ej. findByName(String name);
}
