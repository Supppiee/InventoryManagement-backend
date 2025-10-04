package com.im.inventory.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI inventoryOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Management API")
                        .description("REST API documentation for the Inventory Management system.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Suprith H B")
                                .email("suprithhb54@gmail.com"))
                );
    }
}
