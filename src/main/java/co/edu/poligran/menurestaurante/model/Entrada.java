package co.edu.poligran.menurestaurante.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Patrón Factory Method – participante «ConcreteProduct».
 * Producto de la categoría ENTRADA.
 */
@Entity
@DiscriminatorValue("ENTRADA")
public class Entrada extends Producto {

    private static final int TIEMPO_PREPARACION_MIN = 10;

    protected Entrada() {
        // Requerido por JPA
    }

    public Entrada(String nombre, String descripcion, BigDecimal precio, boolean disponible) {
        super(nombre, descripcion, precio, CategoriaProducto.ENTRADA, disponible);
    }

    @Override
    public int getTiempoPreparacionMin() {
        return TIEMPO_PREPARACION_MIN;
    }
}
