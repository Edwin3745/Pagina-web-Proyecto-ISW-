package co.edu.poligran.menurestaurante.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Patrón Factory Method – participante «ConcreteProduct».
 * Producto de la categoría PLATO_FUERTE.
 */
@Entity
@DiscriminatorValue("PLATO_FUERTE")
public class PlatoFuerte extends Producto {

    private static final int TIEMPO_PREPARACION_MIN = 25;

    protected PlatoFuerte() {
        // Requerido por JPA
    }

    public PlatoFuerte(String nombre, String descripcion, BigDecimal precio, boolean disponible) {
        super(nombre, descripcion, precio, CategoriaProducto.PLATO_FUERTE, disponible);
    }

    @Override
    public int getTiempoPreparacionMin() {
        return TIEMPO_PREPARACION_MIN;
    }
}
