package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Entrada;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Patrón Factory Method – participante «ConcreteCreator».
 * Crea productos de la categoría ENTRADA.
 */
@Component
public class EntradaCreador extends ProductoCreador {

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.ENTRADA;
    }

    @Override
    protected Producto crearProducto(ProductoRequest request) {
        return new Entrada(nombreNormalizado(request), request.descripcion(),
                request.precio(), disponibleOPorDefecto(request));
    }
}
