package co.edu.poligran.menurestaurante.service.factory;

import co.edu.poligran.menurestaurante.dto.ProductoRequest;
import co.edu.poligran.menurestaurante.model.Acompanamiento;
import co.edu.poligran.menurestaurante.model.Bebida;
import co.edu.poligran.menurestaurante.model.CategoriaProducto;
import co.edu.poligran.menurestaurante.model.Entrada;
import co.edu.poligran.menurestaurante.model.PlatoFuerte;
import co.edu.poligran.menurestaurante.model.Postre;
import co.edu.poligran.menurestaurante.model.Producto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del patrón Factory Method: cada categoría debe producir su subclase concreta.
 */
class ProductoCreadorProviderTest {

    private final ProductoCreadorProvider provider = new ProductoCreadorProvider(List.of(
            new EntradaCreador(), new PlatoFuerteCreador(), new AcompanamientoCreador(),
            new PostreCreador(), new BebidaCreador()));

    private ProductoRequest request(CategoriaProducto categoria, Boolean disponible) {
        return new ProductoRequest("  Producto de prueba  ", "Descripción",
                new BigDecimal("12000.00"), categoria, disponible);
    }

    private Producto crear(CategoriaProducto categoria) {
        return provider.obtener(categoria).registrar(request(categoria, true));
    }

    @Test
    void cadaCategoriaCreaSuProductoConcreto() {
        assertInstanceOf(Entrada.class, crear(CategoriaProducto.ENTRADA));
        assertInstanceOf(PlatoFuerte.class, crear(CategoriaProducto.PLATO_FUERTE));
        assertInstanceOf(Acompanamiento.class, crear(CategoriaProducto.ACOMPANAMIENTO));
        assertInstanceOf(Postre.class, crear(CategoriaProducto.POSTRE));
        assertInstanceOf(Bebida.class, crear(CategoriaProducto.BEBIDA));
    }

    @Test
    void existeUnCreadorParaCadaValorDelEnum() {
        for (CategoriaProducto categoria : CategoriaProducto.values()) {
            assertEquals(categoria, provider.obtener(categoria).getCategoria());
            assertEquals(categoria, crear(categoria).getCategoria());
        }
    }

    @Test
    void elProductoCreadoConservaLosDatosYNormalizaElNombre() {
        Producto producto = crear(CategoriaProducto.PLATO_FUERTE);
        assertEquals("Producto de prueba", producto.getNombre());
        assertEquals("Descripción", producto.getDescripcion());
        assertEquals(new BigDecimal("12000.00"), producto.getPrecio());
        assertTrue(producto.isDisponible());
    }

    @Test
    void sinDisponibilidadElProductoNaceDisponible() {
        Producto producto = provider.obtener(CategoriaProducto.POSTRE)
                .registrar(request(CategoriaProducto.POSTRE, null));
        assertTrue(producto.isDisponible());
    }

    @Test
    void cadaProductoDefineSuTiempoDePreparacion() {
        assertEquals(10, crear(CategoriaProducto.ENTRADA).getTiempoPreparacionMin());
        assertEquals(25, crear(CategoriaProducto.PLATO_FUERTE).getTiempoPreparacionMin());
        assertEquals(10, crear(CategoriaProducto.ACOMPANAMIENTO).getTiempoPreparacionMin());
        assertEquals(8, crear(CategoriaProducto.POSTRE).getTiempoPreparacionMin());
        assertEquals(3, crear(CategoriaProducto.BEBIDA).getTiempoPreparacionMin());
    }

    @Test
    void unCreadorRechazaUnaCategoriaQueNoLeCorresponde() {
        ProductoCreador creador = provider.obtener(CategoriaProducto.BEBIDA);
        ProductoRequest ajena = request(CategoriaProducto.POSTRE, true);
        assertThrows(IllegalArgumentException.class, () -> creador.registrar(ajena));
    }

    @Test
    void sinCreadoresRegistradosElProviderFalla() {
        ProductoCreadorProvider vacio = new ProductoCreadorProvider(List.of());
        assertThrows(IllegalStateException.class, () -> vacio.obtener(CategoriaProducto.BEBIDA));
    }
}
