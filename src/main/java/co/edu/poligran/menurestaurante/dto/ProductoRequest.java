package co.edu.poligran.menurestaurante.dto;

import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * Datos de entrada para crear o actualizar un producto.
 */
@Schema(description = "Datos para crear o actualizar un producto del menú")
public record ProductoRequest(

        @Schema(example = "Bandeja paisa")
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Schema(example = "Fríjoles, arroz, chicharrón, carne molida, huevo, plátano y arepa")
        @Size(max = 500, message = "La descripción no puede superar 500 caracteres")
        String descripcion,

        @Schema(example = "32000.00")
        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser mayor que cero")
        @Digits(integer = 8, fraction = 2, message = "Formato de precio inválido")
        BigDecimal precio,

        @Schema(example = "PLATO_FUERTE")
        @NotNull(message = "La categoría es obligatoria")
        CategoriaProducto categoria,

        @Schema(example = "true")
        Boolean disponible
) {
}
