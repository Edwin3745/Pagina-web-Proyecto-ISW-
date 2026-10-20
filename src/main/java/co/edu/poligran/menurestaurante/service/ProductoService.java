package co.edu.poligran.menurestaurante.service;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.dto.ProductoResponse;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;

import java.util.List;

/**
 * Contrato de la capa de negocio para la gestión de productos.
 */
public interface ProductoService {

    List<ProductoResponse> listar(CategoriaProducto categoria, Boolean soloDisponibles);

    ProductoResponse obtenerPorId(Long id);

    ProductoResponse crear(ProductoRequest request);

    ProductoResponse actualizar(Long id, ProductoRequest request);

    void eliminar(Long id);
}
