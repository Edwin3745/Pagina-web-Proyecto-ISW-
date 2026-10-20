package co.edu.poligran.menurestaurante.exception;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Formato uniforme de los errores que devuelve la API.
 */
public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String ruta,
        Map<String, String> campos
) {
}
