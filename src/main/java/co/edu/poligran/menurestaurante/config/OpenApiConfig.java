package co.edu.poligran.menurestaurante.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Metadatos de la documentación OpenAPI que se muestra en Swagger UI
 * (http://localhost:8080/swagger-ui.html).
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI menuRestauranteOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API Menú Restaurante")
                .version("v1")
                .description("Servicio REST CRUD para la entidad Producto (ítems del menú). "
                        + "Proyecto de aula de Ingeniería de Software – Politécnico Grancolombiano."));
    }
}
