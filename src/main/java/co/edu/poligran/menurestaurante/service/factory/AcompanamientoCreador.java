package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Acompanamiento;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.stereotype.Component;

/**
 * Patrón Factory Method – participante «ConcreteCreator».
 * Crea productos de la categoría ACOMPANAMIENTO.
 */
@Component
public class AcompanamientoCreador extends ProductoCreador {

    @Override
    public CategoriaProducto getCategoria() {
        return CategoriaProducto.ACOMPANAMIENTO;
    }

    @Override
    protected Producto crearProducto(ProductoRequest request) {
        return new Acompanamiento(nombreNormalizado(request), request.descripcion(),
                request.precio(), disponibleOPorDefecto(request));
    }
}
