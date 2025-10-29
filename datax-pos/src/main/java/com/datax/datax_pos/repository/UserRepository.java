package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad User.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Método para buscar un usuario por su PIN (para el login)
    Optional<User> findByPinHash(String pinHash);
}
