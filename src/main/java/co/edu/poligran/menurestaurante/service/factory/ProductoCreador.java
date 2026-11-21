package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Producto;

/**
 * Patrón Factory Method – participante «Creator».
 * <p>
 * Declara el método fábrica {@link #crearProducto(ProductoRequest)}, que cada creador concreto
 * sobrescribe para decidir qué subclase de {@link Producto} instanciar. La operación
 * {@link #registrar(ProductoRequest)} contiene la lógica común y usa el método fábrica sin
 * conocer la clase concreta que se construye.
 */
public abstract class ProductoCreador {

    /** Categoría de la que es responsable este creador. */
    public abstract CategoriaProducto getCategoria();

    /** Método fábrica: construye el producto concreto de la categoría. */
    protected abstract Producto crearProducto(ProductoRequest request);

    /**
     * Crea un producto a partir de la petición, verificando antes que la categoría solicitada
     * corresponda a este creador.
     */
    public Producto registrar(ProductoRequest request) {
        if (request.categoria() != getCategoria()) {
            throw new IllegalArgumentException("El creador de " + getCategoria()
                    + " no puede crear productos de la categoría " + request.categoria());
        }
        return crearProducto(request);
    }

    /** Nombre sin espacios sobrantes (regla común a todas las categorías). */
    protected String nombreNormalizado(ProductoRequest request) {
        return request.nombre().trim();
    }

    /** Si la petición no indica disponibilidad, el producto nace disponible. */
    protected boolean disponibleOPorDefecto(ProductoRequest request) {
        return request.disponible() == null || request.disponible();
    }
}
