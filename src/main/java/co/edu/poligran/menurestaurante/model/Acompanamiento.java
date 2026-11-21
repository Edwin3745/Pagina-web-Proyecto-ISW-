package co.edu.poligran.menurestaurante.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;

/**
 * Patrón Factory Method – participante «ConcreteProduct».
 * Producto de la categoría ACOMPANAMIENTO.
 */
@Entity
@DiscriminatorValue("ACOMPANAMIENTO")
public class Acompanamiento extends Producto {

    private static final int TIEMPO_PREPARACION_MIN = 10;

    protected Acompanamiento() {
        // Requerido por JPA
    }

    public Acompanamiento(String nombre, String descripcion, BigDecimal precio, boolean disponible) {
        super(nombre, descripcion, precio, CategoriaProducto.ACOMPANAMIENTO, disponible);
    }

    @Override
    public int getTiempoPreparacionMin() {
        return TIEMPO_PREPARACION_MIN;
    }
}
