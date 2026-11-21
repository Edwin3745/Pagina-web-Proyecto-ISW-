package co.edu.poligran.menurestaurante.exception;

import co.edu.poligran.menurestaurante.model.CategoriaProducto;

/**
 * Regla de negocio derivada del patrón Factory Method: la categoría (y por tanto la clase
 * concreta del producto) se define al crearlo y no puede cambiarse después.
 */
public class CambioCategoriaNoPermitidoException extends RuntimeException {

    public CambioCategoriaNoPermitidoException(CategoriaProducto actual, CategoriaProducto solicitada) {
        super("No se puede cambiar la categoría de " + actual + " a " + solicitada
                + ". Elimine el producto y créelo de nuevo con la categoría deseada");
    }
}
