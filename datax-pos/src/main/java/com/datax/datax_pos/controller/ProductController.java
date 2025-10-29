package com.datax.datax_pos.controller;

import com.datax.datax_pos.dto.ProductSearchResultDTO;
import com.datax.datax_pos.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar Productos.
 * Expone los endpoints de la API relacionados con productos.
 */
@RestController
@RequestMapping("/api/products") // Todos los endpoints aquí empiezan con /api/products
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * Endpoint para buscar productos por SKU o Nombre.
     * Corresponde a la barra de búsqueda del POS.
     * GET /api/products/search?q=12345
     * GET /api/products/search?q=tornillo
     * @param query El término de búsqueda (SKU o nombre).
     * @return Lista de productos que coinciden.
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductSearchResultDTO>> searchProducts(@RequestParam("q") String query) {
        // Esta llamada ahora coincide perfectamente con la interfaz ProductService
        List<ProductSearchResultDTO> results = productService.searchProductsBySkuOrName(query);
        return ResponseEntity.ok(results);
    }
}

