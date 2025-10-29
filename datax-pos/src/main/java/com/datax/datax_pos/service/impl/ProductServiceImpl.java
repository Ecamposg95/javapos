package com.datax.datax_pos.service.impl;

import com.datax.datax_pos.dto.ProductSearchResultDTO;
import com.datax.datax_pos.model.Product;
import com.datax.datax_pos.model.ProductPrice;
import com.datax.datax_pos.repository.ProductPriceRepository;
import com.datax.datax_pos.repository.ProductRepository;
import com.datax.datax_pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementación del Servicio de Productos.
 * Aquí va la lógica de negocio.
 */
@Service
public class ProductServiceImpl implements ProductService {

    // Inyectamos los repositorios que necesitamos
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductPriceRepository productPriceRepository;

    /**
     * Implementación del método de búsqueda unificado.
     * Busca por SKU, Barcode O Nombre.
     * @param query El término de búsqueda.
     * @return Una lista de DTOs con los resultados.
     */
    @Override
    public List<ProductSearchResultDTO> searchProductsBySkuOrName(String query) {
        // CORRECCIÓN: Usamos los métodos existentes del repositorio y combinamos los resultados.

        // Usamos LinkedHashSet para mantener el orden de inserción y evitar duplicados
        Set<Product> productSet = new LinkedHashSet<>();

        // 1. Buscar por SKU exacto
        productRepository.findBySku(query).ifPresent(productSet::add);

        // 2. Buscar por Barcode exacto
        productRepository.findByBarcode(query).ifPresent(productSet::add);

        // 3. Buscar por Nombre
        productSet.addAll(productRepository.findByNameContainingIgnoreCase(query));

        // 4. Convertimos el Set a Lista y luego a DTOs
        return productSet.stream()
                .map(this::mapToProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Metodo helper privado para convertir un Product a un DTO.
     * Busca el precio de menudeo (cantidad 1).
     */
    private ProductSearchResultDTO mapToProductDTO(Product product) {
        // Buscamos el precio de menudeo (cantidad mínima <= 1)
        BigDecimal menudeoPrice = productPriceRepository.findBestPriceForQuantity(product, BigDecimal.ONE)
                .map(ProductPrice::getUnitPrice)
                .orElse(BigDecimal.ZERO); // Si no hay precio, ponemos 0

        return new ProductSearchResultDTO(product, menudeoPrice);
    }
}
