package com.example.eLibrary.security;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Moonlight Hotel&Spa",
                        email = "moonlight_hotel&spa@gmail.com",
                        url = "https://moonlighthotel&spa.com"
                ),
                description = "OpenApi documentation for moonlight hotel and spa",
                title = "OpenApi specification",
                version = "1.0",
                license = @License(
                        name = "License name",
                        url = "https://some-license.com"
                ),
                termsOfService = "Terms of service"
        ),
        servers = {
                @Server(
                        description = "LOCAL ENV",
                        url = "http://localhost:8080"
                ),
                @Server(
                        description = "PROD ENV",
                        url = "http://office.nasbg.com:33062"
                )
        }
)
public class OpenApiConfig {
}
