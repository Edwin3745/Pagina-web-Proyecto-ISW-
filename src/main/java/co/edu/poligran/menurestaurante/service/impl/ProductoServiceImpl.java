package co.edu.poligran.menurestaurante.service.impl;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.dto.ProductoResponse;
import co.edu.poligran.menurestaurante.exception.ProductoDuplicadoException;
import co.edu.poligran.menurestaurante.exception.RecursoNoEncontradoException;
import co.edu.poligran.menurestaurante.mapper.ProductoMapper;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Producto;
import co.edu.poligran.menurestaurante.repository.ProductoRepository;
import co.edu.poligran.menurestaurante.service.ProductoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación de las reglas de negocio de Producto.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository repository;
    private final ProductoMapper mapper;

    public ProductoServiceImpl(ProductoRepository repository, ProductoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponse> listar(CategoriaProducto categoria, Boolean soloDisponibles) {
        List<Producto> productos;
        if (categoria != null) {
            productos = repository.findByCategoria(categoria);
        } else if (Boolean.TRUE.equals(soloDisponibles)) {
            productos = repository.findByDisponibleTrue();
        } else {
            productos = repository.findAll();
        }
        return productos.stream()
                .filter(p -> !Boolean.TRUE.equals(soloDisponibles) || p.isDisponible())
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponse obtenerPorId(Long id) {
        return mapper.toResponse(buscarOFallar(id));
    }

    @Override
    public ProductoResponse crear(ProductoRequest request) {
        if (repository.existsByNombreIgnoreCase(request.nombre().trim())) {
            throw new ProductoDuplicadoException(request.nombre());
        }
        Producto guardado = repository.save(mapper.toEntity(request));
        return mapper.toResponse(guardado);
    }

    @Override
    public ProductoResponse actualizar(Long id, ProductoRequest request) {
        Producto producto = buscarOFallar(id);
        if (repository.existsByNombreIgnoreCaseAndIdNot(request.nombre().trim(), id)) {
            throw new ProductoDuplicadoException(request.nombre());
        }
        mapper.updateEntity(producto, request);
        return mapper.toResponse(repository.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = buscarOFallar(id);
        repository.delete(producto);
    }

    private Producto buscarOFallar(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto", id));
    }
}
