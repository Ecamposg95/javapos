package com.datax.datax_pos.service.impl;

import com.datax.datax_pos.dto.AddItemRequestDTO;
import com.datax.datax_pos.dto.CreateOrderRequestDTO;
import com.datax.datax_pos.dto.OrderDTO;
import com.datax.datax_pos.exception.ResourceNotFoundException;
import com.datax.datax_pos.model.Order;
import com.datax.datax_pos.model.OrderItem;
import com.datax.datax_pos.model.Product;
import com.datax.datax_pos.model.ProductPrice;
import com.datax.datax_pos.model.enums.OrderStatus;
import com.datax.datax_pos.repository.*; // Importar todos los repositorios necesarios
import com.datax.datax_pos.service.OrderService;
import jakarta.transaction.Transactional; // Importar Transactional
import lombok.extern.slf4j.Slf4j; // Añadir Logger
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors; // Necesario para Collectors.toList()

/**
 * Implementación del Servicio de Pedidos (Carrito).
 */
@Service
@Slf4j // Añadir logger para depuración
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderItemRepository orderItemRepository; // Necesario para guardar ítems
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductPriceRepository productPriceRepository;
    @Autowired
    private UserRepository userRepository; // Para asignar cajero/vendedor si es necesario
    @Autowired
    private BranchRepository branchRepository; // Para asignar sucursal

    // Constante para la tasa de impuestos (ej. 16%)
    private static final BigDecimal TAX_RATE = new BigDecimal("0.16");

    @Override
    @Transactional // Asegura que toda la operación sea atómica
    public OrderDTO createQuickOrder(CreateOrderRequestDTO requestDTO) {
        log.info("Creating quick order for branch: {}", requestDTO.getBranchId());
        Order newOrder = new Order();

        // Asignar sucursal (asumimos ID 1 por ahora, deberías obtenerla del usuario logueado)
        newOrder.setBranch(branchRepository.findById(requestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id: " + requestDTO.getBranchId())));

        // Asignar vendedor/cajero (opcional, podría venir del request o del usuario logueado)
        if (requestDTO.getSellerId() != null) {
            log.info("Assigning seller with ID: {}", requestDTO.getSellerId());
            newOrder.setSeller(userRepository.findById(requestDTO.getSellerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + requestDTO.getSellerId())));
        } else {
            log.warn("No seller ID provided for new order.");
        }


        newOrder.setCustomerName(requestDTO.getCustomerName() != null ? requestDTO.getCustomerName() : "Client");
        newOrder.setStatus(OrderStatus.DRAFT); // Siempre empieza como DRAFT
        newOrder.setOpenedAt(LocalDateTime.now());
        newOrder.setSubtotal(BigDecimal.ZERO);
        newOrder.setTaxAmount(BigDecimal.ZERO);
        newOrder.setTotal(BigDecimal.ZERO);

        Order savedOrder = orderRepository.save(newOrder);
        log.info("Quick order created successfully with ID: {}", savedOrder.getId());
        return new OrderDTO(savedOrder); // Devuelve el DTO de la orden recién creada
    }

    @Override
    @Transactional
    public OrderDTO addItemToOrder(Long orderId, AddItemRequestDTO itemRequest) {
        log.info("Adding item '{}' (qty: {}) to order ID: {}", itemRequest.getProductIdentifier(), itemRequest.getQuantity(), orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // **CORRECCIÓN:** Usar el método unificado findBySkuOrBarcode
        Product product = productRepository.findBySkuOrBarcode(itemRequest.getProductIdentifier())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with identifier: " + itemRequest.getProductIdentifier()));
        log.debug("Product found: {}", product.getName());

        // Calcular la cantidad total de este producto ya en el carrito
        BigDecimal currentQuantityInCart = order.getItems().stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(product.getId()))
                .map(OrderItem::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("Current quantity in cart for product {}: {}", product.getId(), currentQuantityInCart);

        BigDecimal newTotalQuantity = currentQuantityInCart.add(itemRequest.getQuantity());
        log.debug("New total quantity for product {}: {}", product.getId(), newTotalQuantity);

        // Buscar el mejor precio para la *nueva cantidad total*
        BigDecimal unitPrice = productPriceRepository.findBestPriceForQuantity(product, newTotalQuantity)
                .map(ProductPrice::getUnitPrice)
                .orElseThrow(() -> new ResourceNotFoundException("No price found for product " + product.getName() + " and quantity " + newTotalQuantity));
        log.debug("Best unit price found: {}", unitPrice);

        // Verificar si el producto ya existe en la orden
        Optional<OrderItem> existingItemOpt = order.getItems().stream()
                .filter(item -> item.getProduct() != null && item.getProduct().getId().equals(product.getId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            log.debug("Product {} already exists in order, updating quantity and price.", product.getId());
            // Actualizar cantidad y precio (si cambió por la nueva cantidad total)
            OrderItem existingItem = existingItemOpt.get();
            existingItem.setQuantity(newTotalQuantity);
            existingItem.setUnitPrice(unitPrice); // Actualizar precio unitario
            existingItem.setLineTotal(unitPrice.multiply(newTotalQuantity).setScale(2, RoundingMode.HALF_UP)); // Recalcular total de línea
            // Nota: Aquí podrías añadir lógica de descuento si aplica
        } else {
            log.debug("Product {} is new to the order, creating new item.", product.getId());
            // Crear nuevo ítem
            OrderItem newItem = new OrderItem();
            newItem.setOrder(order);
            newItem.setProduct(product);
            newItem.setQuantity(itemRequest.getQuantity()); // Cantidad a añadir
            newItem.setUnitPrice(unitPrice);
            newItem.setDiscountAmount(BigDecimal.ZERO); // Inicialmente sin descuento
            newItem.setTaxRate(TAX_RATE); // Asignar tasa de impuesto
            newItem.setLineTotal(unitPrice.multiply(itemRequest.getQuantity()).setScale(2, RoundingMode.HALF_UP));
            order.getItems().add(newItem); // Añadir a la lista de la orden
        }

        // Recalcular totales de la orden completa
        recalculateOrderTotals(order);

        Order updatedOrder = orderRepository.save(order); // Guardar la orden actualizada (incluye ítems nuevos/modificados por Cascade)
        log.info("Item added/updated successfully for order ID: {}", orderId);
        return new OrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO removeItemFromOrder(Long orderId, Long itemId) {
        log.info("Removing item ID: {} from order ID: {}", itemId, orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        boolean removed = order.getItems().removeIf(item -> item.getId() != null && item.getId().equals(itemId));

        if (!removed) {
            log.warn("Item ID: {} not found in order ID: {}", itemId, orderId);
            throw new ResourceNotFoundException("Item not found with id: " + itemId + " in order " + orderId);
        }

        recalculateOrderTotals(order); // Recalcular totales después de eliminar
        Order updatedOrder = orderRepository.save(order);
        log.info("Item removed successfully from order ID: {}", orderId);
        return new OrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO updateCustomerName(Long orderId, String customerName) {
        log.info("Updating customer name to '{}' for order ID: {}", customerName, orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        order.setCustomerName(customerName);
        Order updatedOrder = orderRepository.save(order);
        log.info("Customer name updated successfully for order ID: {}", orderId);
        return new OrderDTO(updatedOrder);
    }

    @Override
    @Transactional
    public OrderDTO markOrderAsReadyToPay(Long orderId) {
        log.info("Marking order ID: {} as READY_TO_PAY", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        if (order.getStatus() == OrderStatus.DRAFT) {
            order.setStatus(OrderStatus.READY_TO_PAY);
            order.setReadyAt(LocalDateTime.now());
            Order updatedOrder = orderRepository.save(order);
            log.info("Order ID: {} marked as READY_TO_PAY successfully.", orderId);
            return new OrderDTO(updatedOrder);
        } else {
            log.warn("Order ID: {} is not in DRAFT status, cannot mark as ready. Current status: {}", orderId, order.getStatus());
            // Opcional: lanzar una excepción si la orden no está en DRAFT
            // throw new IllegalStateException("Order " + orderId + " is not in DRAFT status.");
            return new OrderDTO(order); // Devolver el DTO sin cambios
        }
    }

    // Método helper para recalcular subtotal, impuestos y total
    private void recalculateOrderTotals(Order order) {
        log.debug("Recalculating totals for order ID: {}", order.getId());
        BigDecimal subtotal = BigDecimal.ZERO;
        for (OrderItem item : order.getItems()) {
            // Asegurarse que lineTotal no sea nulo
            BigDecimal lineTotal = item.getLineTotal() != null ? item.getLineTotal() : BigDecimal.ZERO;
            subtotal = subtotal.add(lineTotal);
        }

        BigDecimal taxAmount = subtotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(taxAmount).setScale(2, RoundingMode.HALF_UP);

        order.setSubtotal(subtotal.setScale(2, RoundingMode.HALF_UP));
        order.setTaxAmount(taxAmount);
        order.setTotal(total);
        log.debug("Order ID: {} totals recalculated - Subtotal: {}, Tax: {}, Total: {}", order.getId(), order.getSubtotal(), order.getTaxAmount(), order.getTotal());
    }

    // --- Métodos que aún faltan implementar ---

    @Override
    public List<OrderDTO> findOrdersByStatus(Long branchId, OrderStatus status) {
        log.info("Finding orders for branch ID: {} with status: {}", branchId, status);
        List<Order> orders = orderRepository.findByBranchIdAndStatus(branchId, status);
        log.info("Found {} orders.", orders.size());
        // Convertir la lista de entidades Order a una lista de OrderDTO
        return orders.stream()
                .map(OrderDTO::new) // Usa el constructor que creamos en OrderDTO
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        log.info("Getting order by ID: {}", orderId);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
        log.info("Order found: {}", order.getId());
        return new OrderDTO(order);
    }
}
