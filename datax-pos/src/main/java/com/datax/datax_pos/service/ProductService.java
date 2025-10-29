package com.datax.datax_pos.service;

import com.datax.datax_pos.dto.ProductSearchResultDTO;
import java.util.List;

/**
 * Interfaz para la lógica de negocio relacionada con Productos.
 */
public interface ProductService {

    /**
     * Busca productos por SKU o Nombre.
     * Este es el método que tu controlador está buscando.
     *
     * @param query El término de búsqueda (SKU o parte del nombre).
     * @return Una lista de DTOs con los resultados de la búsqueda.
     */
    List<ProductSearchResultDTO> searchProductsBySkuOrName(String query);

}

