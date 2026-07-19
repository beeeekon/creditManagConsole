package ru.barkalova.loanManagConsole.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bank Loan Management System API")
                        .description("REST API для управления клиентами и кредитами банка")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Ekaterina Barkalova")
                                .email("barkekvl@mail.ru")
                                .url("https://github.com/beeeekon")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Local server")));
    }
}