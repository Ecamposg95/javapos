package com.datax.datax_pos.controller;

import com.datax.datax_pos.dto.AddItemRequestDTO;
import com.datax.datax_pos.dto.CreateOrderRequestDTO;
import com.datax.datax_pos.dto.OrderDTO;
import com.datax.datax_pos.dto.PaymentRequestDTO;
import com.datax.datax_pos.service.OrderService;
import com.datax.datax_pos.service.PaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar Pedidos (Órdenes).
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService; // Inyectar servicio de pago

    /**
     * Endpoint para crear una nueva orden rápida (venta de caja).
     * POST /api/orders/quick
     */
    @PostMapping("/quick")
    public ResponseEntity<OrderDTO> createQuickOrder(@RequestBody CreateOrderRequestDTO request) {
        log.info("Recibida petición para crear orden rápida: {}", request);
        // FIX: Cambiado de createQuickSaleOrder a createQuickOrder para coincidir con la interfaz
        OrderDTO newOrder = orderService.createQuickOrder(request);
        return ResponseEntity.ok(newOrder);
    }

    /**
     * Endpoint para obtener los detalles de una orden por ID.
     * GET /api/orders/{orderId}
     */
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long orderId) {
        log.info("Recibida petición para obtener orden ID: {}", orderId);
        OrderDTO order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    /**
     * Endpoint para añadir un ítem a una orden existente.
     * POST /api/orders/{orderId}/items
     */
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderDTO> addItemToOrder(@PathVariable Long orderId, @RequestBody AddItemRequestDTO itemRequest) {
        log.info("Recibida petición para añadir item a orden {}: {}", orderId, itemRequest);
        OrderDTO updatedOrder = orderService.addItemToOrder(orderId, itemRequest);
        return ResponseEntity.ok(updatedOrder);
    }

    /**
     * Endpoint para eliminar un ítem de una orden.
     * DELETE /api/orders/{orderId}/items/{itemId}
     * Cambiado a DELETE para seguir convenciones REST
     */
    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<OrderDTO> removeItemFromOrder(@PathVariable Long orderId, @PathVariable Long itemId) {
        log.info("Recibida petición para eliminar item {} de orden {}", itemId, orderId);
        OrderDTO updatedOrder = orderService.removeItemFromOrder(orderId, itemId);
        return ResponseEntity.ok(updatedOrder);
    }


    /**
     * Endpoint para listar pedidos listos para cobro en una sucursal.
     * GET /api/orders/ready?branchId=1
     */
    @GetMapping("/ready")
    public ResponseEntity<List<OrderDTO>> getOrdersReadyForPayment(@RequestParam Long branchId) {
        log.info("Recibida petición para listar órdenes listas en sucursal {}", branchId);
        List<OrderDTO> orders = orderService.findOrdersReadyForPayment(branchId);
        return ResponseEntity.ok(orders);
    }

    /**
     * Endpoint para marcar una orden como lista para pagar.
     * POST /api/orders/{orderId}/ready
     */
    @PostMapping("/{orderId}/ready")
    public ResponseEntity<OrderDTO> markOrderReady(@PathVariable Long orderId) {
        log.info("Recibida petición para marcar orden {} como lista para pago", orderId);
        OrderDTO updatedOrder = orderService.markOrderAsReadyToPay(orderId);
        return ResponseEntity.ok(updatedOrder);
    }


    /**
     * Endpoint para registrar el pago de una orden.
     * POST /api/orders/{orderId}/pay
     */
    @PostMapping("/{orderId}/pay")
    public ResponseEntity<OrderDTO> payOrder(@PathVariable Long orderId, @RequestBody PaymentRequestDTO paymentRequest) {
        log.info("Recibida petición de pago para orden {}: {}", orderId, paymentRequest);
        // El PaymentService se encarga de cambiar el estado de la orden a PAID
        OrderDTO paidOrder = paymentService.processPayment(orderId, paymentRequest);
        return ResponseEntity.ok(paidOrder);
    }

    // Faltarían endpoints para:
    // - PUT /api/orders/{orderId}/customer (Actualizar cliente)
    // - POST /api/orders/{orderId}/cancel (Cancelar orden, requiere lógica de negocio)
    // - Otros endpoints según el flujo completo (ej. vendedor creando orden inicial)
}
