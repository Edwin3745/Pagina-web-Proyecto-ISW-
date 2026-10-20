package co.edu.poligran.menurestaurante.mapper;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.dto.ProductoResponse;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Convierte entre la entidad Producto y sus DTO, para que la entidad
 * de persistencia nunca se exponga directamente en la API.
 */
@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequest request) {
        return new Producto(
                request.nombre().trim(),
                request.descripcion(),
                request.precio(),
                request.categoria(),
                request.disponible() == null || request.disponible()
        );
    }

    public void updateEntity(Producto producto, ProductoRequest request) {
        producto.setNombre(request.nombre().trim());
        producto.setDescripcion(request.descripcion());
        producto.setPrecio(request.precio());
        producto.setCategoria(request.categoria());
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
                producto.getFechaActualizacion()
        );
    }
}
