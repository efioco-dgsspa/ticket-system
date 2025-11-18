package com.efioco.ticketsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	
	@Bean
    public OpenAPI ticketSystemOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("SpringBoot Security API")
                        .version("1.0.0")
                        .description("API REST di test per Spring Security")
                        .contact(new Contact()
                                .name("Team anon")
                                .email("support@springsecurity.com")))
                // 1️⃣ Definizione della security scheme JWT
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                // 2️⃣ Applica la security scheme globalmente (opzionale)
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"));
    }
}
