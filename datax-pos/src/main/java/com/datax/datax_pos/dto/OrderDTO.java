package com.datax.datax_pos.dto;

import com.datax.datax_pos.model.Order;
import com.datax.datax_pos.model.OrderItem;
import com.datax.datax_pos.model.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para representar una Orden completa, incluyendo sus ítems.
 */
@Data
@NoArgsConstructor // Necesario para Jackson y uso general
public class OrderDTO {

    private Long id;
    private Long branchId; // ID de la sucursal
    private String sellerName; // Nombre del vendedor
    private String customerName;
    private OrderStatus status;
    private LocalDateTime openedAt;
    private LocalDateTime readyAt;
    private LocalDateTime paidAt;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private List<OrderItemDTO> items;

    /**
     * **NUEVO:** Constructor que acepta una entidad Order.
     * Mapea los campos de la entidad a este DTO.
     */
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.branchId = order.getBranch() != null ? order.getBranch().getId() : null;
        this.sellerName = order.getSeller() != null ? order.getSeller().getName() : "N/A"; // Manejar vendedor nulo
        this.customerName = order.getCustomerName();
        this.status = order.getStatus();
        this.openedAt = order.getOpenedAt();
        this.readyAt = order.getReadyAt();
        this.paidAt = order.getPaidAt();
        this.subtotal = order.getSubtotal();
        this.taxAmount = order.getTaxAmount();
        this.total = order.getTotal();
        this.items = order.getItems().stream()
                .map(item -> new OrderItemDTO( // Asegúrate que OrderItemDTO tenga un constructor adecuado o mapea aquí
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getSku(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getUnitPrice(),
                        item.getDiscountAmount(),
                        item.getLineTotal()
                ))
                .collect(Collectors.toList());
    }
}
