package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.PlatoFuerte;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Patrón Factory Method – participante «ConcreteCreator».
 * Crea productos de la categoría PLATO_FUERTE.
 */
@Component
public class PlatoFuerteCreador extends ProductoCreador {

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.PLATO_FUERTE;
    }

    @Override
    protected Producto crearProducto(ProductoRequest request) {
        return new PlatoFuerte(nombreNormalizado(request), request.descripcion(),
                request.precio(), disponibleOPorDefecto(request));
    }
}
