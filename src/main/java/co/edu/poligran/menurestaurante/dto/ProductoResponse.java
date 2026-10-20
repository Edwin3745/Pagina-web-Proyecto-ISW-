package co.edu.poligran.menurestaurante.dto;

import co.edu.poligran.menurestaurante.model.CategoriaProducto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representación de un producto que la API devuelve al cliente.
 */
public record ProductoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        CategoriaProducto categoria,
        boolean disponible,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion
) {
}
