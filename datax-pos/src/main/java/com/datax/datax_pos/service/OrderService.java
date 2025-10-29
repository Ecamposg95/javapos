package com.datax.datax_pos.service;

import com.datax.datax_pos.dto.AddItemRequestDTO;
import com.datax.datax_pos.dto.CreateOrderRequestDTO;
import com.datax.datax_pos.dto.OrderDTO;
import java.util.List;

/**
 * Interfaz para el Servicio de Pedidos (Órdenes).
 * Define la lógica de negocio del carrito de compras.
 */
public interface OrderService {

    /**
     * Crea un nuevo pedido rápido (venta de mostrador).
     * @param request Datos iniciales (sucursal, cajero, cliente opcional).
     * @return El DTO del pedido recién creado en estado DRAFT.
     */
    OrderDTO createQuickOrder(CreateOrderRequestDTO request);

    /**
     * Añade un ítem (producto) a un pedido existente.
     * Si el producto ya existe, actualiza la cantidad.
     * Calcula el precio correcto según la cantidad.
     * Recalcula los totales del pedido.
     * @param orderId El ID del pedido a modificar.
     * @param itemRequest DTO con el identificador del producto y la cantidad.
     * @return El DTO del pedido actualizado.
     */
    OrderDTO addItemToOrder(Long orderId, AddItemRequestDTO itemRequest);

    /**
     * Elimina un ítem de un pedido.
     * Recalcula los totales del pedido.
     * @param orderId El ID del pedido a modificar.
     * @param orderItemId El ID del ítem de pedido a eliminar.
     * @return El DTO del pedido actualizado.
     */
    OrderDTO removeItemFromOrder(Long orderId, Long orderItemId);

    /**
     * Obtiene los detalles de un pedido por su ID.
     * @param orderId El ID del pedido.
     * @return El DTO del pedido.
     */
    OrderDTO getOrderById(Long orderId);

    /**
     * Busca pedidos que están listos para ser cobrados en una sucursal específica.
     * @param branchId ID de la sucursal.
     * @return Lista de DTOs de pedidos en estado READY_TO_PAY.
     */
    List<OrderDTO> findOrdersReadyForPayment(Long branchId);

    /**
     * Marca un pedido como listo para ser cobrado.
     * Cambia el estado de DRAFT a READY_TO_PAY.
     * @param orderId El ID del pedido a marcar.
     * @return El DTO del pedido actualizado.
     */
    OrderDTO markOrderAsReadyToPay(Long orderId);

    // Aquí irían los métodos para actualizar cliente, cancelar orden, etc. si fueran necesarios.
}
