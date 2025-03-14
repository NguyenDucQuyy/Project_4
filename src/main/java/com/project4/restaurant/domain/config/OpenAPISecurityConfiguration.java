package com.project4.restaurant.domain.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Restaurant Service",
        version = "v1",
        contact = @Contact(
            name = "project4",
            email = "project4@project4.tv",
            url = "https://project4-apt/"
        ),
        license = @License(
            name = "", url = ""
        ),
        description = """
            Tài liệu này mô tả các API cho end-user
            """
    ),
    servers = {
        @Server(url = "https://project4-apt/", description = "Development Server")

    },
    security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class OpenAPISecurityConfiguration {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI();
  }
}