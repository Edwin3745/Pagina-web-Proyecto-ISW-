package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Postre;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Patrón Factory Method – participante «ConcreteCreator».
 * Crea productos de la categoría POSTRE.
 */
@Component
public class PostreCreador extends ProductoCreador {

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.POSTRE;
    }

    @Override
    protected Producto crearProducto(ProductoRequest request) {
        return new Postre(nombreNormalizado(request), request.descripcion(),
                request.precio(), disponibleOPorDefecto(request));
    }
}
