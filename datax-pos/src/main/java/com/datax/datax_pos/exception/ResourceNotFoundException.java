package com.datax.datax_pos.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Excepción personalizada para cuando no se encuentra un recurso (ej. Producto, Orden).
 * Esto devolverá automáticamente un error 404 NOT FOUND.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
