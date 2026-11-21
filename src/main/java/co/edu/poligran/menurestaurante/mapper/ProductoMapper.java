package co.edu.poligran.menurestaurante.mapper;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.dto.ProductoResponse;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Convierte entre la entidad Producto y sus DTO, para que la entidad
 * de persistencia nunca se exponga directamente en la API.
 * <p>
 * La creación de la entidad a partir de la petición ya no vive aquí: la realizan los
 * creadores del patrón Factory Method (paquete {@code service.factory}).
 */
@Component
public class ProductoMapper {

    /** Actualiza los campos editables. La categoría no se modifica: la define la fábrica al crear. */
    public void updateEntity(Producto producto, ProductoRequest request) {
        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        if (request.disponible() != null) {
            producto.setDisponible(request.disponible());
        }
    }

    public ProductoResponse toResponse(Producto producto) {
        return new ProductoResponse(
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.isDisponible(),
                producto.getFechaCreacion(),
                producto.getFechaActualizacion(),
                producto.getTiempoPreparacionMin()
        );
    }
}
