package co.edu.poligran.menurestaurante.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Patrón Factory Method – participante «Product».
 * <p>
 * Entidad de dominio abstracta: un producto (plato o ítem) del menú del restaurante.
 * Sus subclases concretas (Entrada, PlatoFuerte, Acompanamiento, Postre y Bebida) se crean
 * mediante los creadores del paquete {@code service.factory}.
 * <p>
 * Persistencia: herencia {@code SINGLE_TABLE}. Todas las subclases se guardan en la tabla
 * "productos" y la columna "categoria" actúa como discriminador, por lo que el esquema de la
 * base de datos no cambia y los registros existentes siguen siendo válidos.
 */
@Entity
@Table(name = "productos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "categoria", discriminatorType = DiscriminatorType.STRING, length = 30)
public abstract class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    /**
     * Columna discriminadora expuesta como propiedad de solo lectura: permite consultar por
     * categoría (findByCategoria) sin que el valor se escriba dos veces.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", length = 30, insertable = false, updatable = false)
    private CategoriaProducto categoria;

    @Column(nullable = false)
    private boolean disponible = true;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    protected Producto() {
        // Requerido por JPA
    }

    protected Producto(String nombre, String descripcion, BigDecimal precio,
                       CategoriaProducto categoria, boolean disponible) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    /**
     * Tiempo estimado de preparación en minutos. Cada producto concreto define el suyo
     * (comportamiento polimórfico); no se guarda en la base de datos.
     */
    public abstract int getTiempoPreparacionMin();

    @PrePersist
    void alCrear() {
        LocalDateTime ahora = LocalDateTime.now();
        this.fechaCreacion = ahora;
        this.fechaActualizacion = ahora;
    }

    @PreUpdate
    void alActualizar() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public CategoriaProducto getCategoria() { return categoria; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
}
