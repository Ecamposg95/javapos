package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Department.
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
}
