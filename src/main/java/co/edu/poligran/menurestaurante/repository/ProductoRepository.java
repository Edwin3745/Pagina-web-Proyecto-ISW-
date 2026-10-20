package co.edu.poligran.menurestaurante.repository;

import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Capa de acceso a datos. Spring Data JPA genera la implementación
 * (findAll, findById, save, deleteById, ...) en tiempo de ejecución.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoria(CategoriaProducto categoria);

    List<Producto> findByDisponibleTrue();

    boolean existsByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCaseAndIdNot(String nombre, Long id);
}
