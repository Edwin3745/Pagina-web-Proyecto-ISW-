package co.edu.poligran.menurestaurante.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Patrón Factory Method – participante «ConcreteProduct».
 * Producto de la categoría POSTRE.
 */
@Entity
@DiscriminatorValue("POSTRE")
public class Postre extends Producto {

    private static final int TIEMPO_PREPARACION_MIN = 8;

    protected Postre() {
        // Requerido por JPA
    }

    public Postre(String nombre, String descripcion, BigDecimal precio, boolean disponible) {
        super(nombre, descripcion, precio, CategoriaProducto.POSTRE, disponible);
    }

    @Override
    public int getTiempoPreparacionMin() {
        return TIEMPO_PREPARACION_MIN;
    }
}
