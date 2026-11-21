package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Selecciona el creador concreto que corresponde a una categoría. Es el punto de entrada del
 * patrón para la capa de servicio: Spring inyecta todos los creadores registrados como
 * {@code @Component}, de modo que agregar una categoría nueva solo exige agregar su creador.
 */
@Component
public class ProductoCreadorProvider {

    private final List<ProductoCreador> creadores;

    public ProductoCreadorProvider(List<ProductoCreador> creadores) {
        this.creadores = List.copyOf(creadores);
    }

    public ProductoCreador obtener(CategoriaProducto categoria) {
        return creadores.stream()
                .filter(c -> c.getCategoria() == categoria)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(
                        "No hay un creador registrado para la categoría " + categoria));
    }
}
