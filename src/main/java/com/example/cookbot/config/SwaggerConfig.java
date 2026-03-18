package com.example.cookbot.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Конфигурация SpringDoc OpenAPI (Swagger).
 *
 * @author dev
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.application.name:cook-bot-app}")
    private String applicationName;

    @Value("${app.version:1.0.0}")
    private String applicationVersion;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName + " API")
                        .version(applicationVersion)
                        .description("API для поиска рецептов по ингредиентам через Telegram бота. " +
                                "Поддерживает поиск как по простому списку ингредиентов, " +
                                "так и по запросу на естественном языке.")
                        .contact(new Contact()
                                .name("Development Team")
                                .email("dev@cookbot.example.com")
                                .url("https://github.com/yagitpa/cookbot"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Локальный сервер разработки")
                ));
    }
}