package com.datax.datax_pos.repository;

import com.datax.datax_pos.model.Order;
import com.datax.datax_pos.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Order.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Busca pedidos por sucursal y estado.
     * Útil para que la caja vea los pedidos READY_TO_PAY.
     * Spring Data JPA infiere la consulta por el nombre del metodo.
     * @param branchId El ID de la sucursal.
     * @param status El estado del pedido a buscar.
     * @return Lista de pedidos que coinciden.
     */
    List<Order> findByBranchIdAndStatus(Long branchId, OrderStatus status);

    // Puedes añadir más métodos de búsqueda aquí si los necesitas, por ejemplo:
    // List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
}

