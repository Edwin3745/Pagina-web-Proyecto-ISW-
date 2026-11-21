package co.edu.poligran.menurestaurante.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Patrón Factory Method – participante «ConcreteProduct».
 * Producto de la categoría BEBIDA.
 */
@Entity
@DiscriminatorValue("BEBIDA")
public class Bebida extends Producto {

    private static final int TIEMPO_PREPARACION_MIN = 3;

    protected Bebida() {
        // Requerido por JPA
    }

    public Bebida(String nombre, String descripcion, BigDecimal precio, boolean disponible) {
        super(nombre, descripcion, precio, CategoriaProducto.BEBIDA, disponible);
    }

    @Override
    public int getTiempoPreparacionMin() {
        return TIEMPO_PREPARACION_MIN;
    }
}
