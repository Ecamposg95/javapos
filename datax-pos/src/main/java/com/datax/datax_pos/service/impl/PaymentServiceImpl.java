package com.datax.datax_pos.service.impl;

import com.datax.datax_pos.dto.OrderDTO;
import com.datax.datax_pos.dto.PaymentRequestDTO;
import com.datax.datax_pos.exception.ResourceNotFoundException;
import com.datax.datax_pos.model.Order;
import com.datax.datax_pos.model.Payment;
import com.datax.datax_pos.model.User;
import com.datax.datax_pos.model.enums.OrderStatus;
import com.datax.datax_pos.repository.OrderRepository;
import com.datax.datax_pos.repository.PaymentRepository;
import com.datax.datax_pos.repository.UserRepository;
import com.datax.datax_pos.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public OrderDTO processPayment(Long orderId, PaymentRequestDTO request) {
        // 1. Buscar la Orden
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada: " + orderId));

        // 2. Buscar al Cajero
        User cashier = userRepository.findById(request.getCashierId())
                .orElseThrow(() -> new ResourceNotFoundException("Cajero no encontrado: " + request.getCashierId()));

        // 3. Validar estado de la orden
        if (order.getStatus() != OrderStatus.READY_TO_PAY) {
            throw new IllegalStateException("La orden no está lista para ser pagada. Estado actual: " + order.getStatus());
        }

        // 4. Validar monto
        if (request.getAmountReceived().compareTo(order.getTotal()) < 0) {
            throw new IllegalStateException("El monto recibido (" + request.getAmountReceived() +
                    ") es menor al total de la orden (" + order.getTotal() + ")");
        }

        // 5. Calcular el cambio
        BigDecimal change = request.getAmountReceived().subtract(order.getTotal());

        // 6. Crear y guardar la entidad de Pago
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setCashier(cashier);
        payment.setMethod(request.getMethod());
        payment.setAmount(request.getAmountReceived()); // Guardamos lo que se recibió
        payment.setChange(change); // Guardamos el cambio
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // 7. Actualizar la Orden a "PAGADA"
        order.setStatus(OrderStatus.PAID);
        order.setPaidAt(LocalDateTime.now());
        Order savedOrder = orderRepository.save(order);

        // 8. Devolver la orden actualizada
        return new OrderDTO(savedOrder);
    }
}
