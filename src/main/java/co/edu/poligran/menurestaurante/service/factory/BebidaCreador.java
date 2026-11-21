package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Bebida;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Patrón Factory Method – participante «ConcreteCreator».
 * Crea productos de la categoría BEBIDA.
 */
@Component
public class BebidaCreador extends ProductoCreador {

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.BEBIDA;
    }

    @Override
    protected Producto crearProducto(ProductoRequest request) {
        return new Bebida(nombreNormalizado(request), request.descripcion(),
                request.precio(), disponibleOPorDefecto(request));
    }
}
