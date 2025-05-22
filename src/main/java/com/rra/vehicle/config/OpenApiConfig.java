
package com.rra.vehicle.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenApiConfig {

    @Bean
    protected OpenAPI customOpenAPI() {
        return new OpenAPI()
            .components(new Components())
            .info(new Info()
                .title("Rwanda Revenue Authority (RRA) Vehicle Tracking API")
                .description("API for tracking vehicle ownership and transfers in Rwanda")
                .version("1.0")
                .contact(new Contact()
                    .name("RRA Support")
                    .email("support@rra.gov.rw")
                    .url("https://www.rra.gov.rw"))
                .license(new License()
                    .name("RRA License")
                    .url("https://www.rra.gov.rw/licenses")));
    }
}
