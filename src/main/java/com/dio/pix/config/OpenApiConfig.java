package com.dio.pix.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Value("${API_URL:http://localhost:8080}")
    private String apiUrl;

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API REST PIX")
                        .version("1.0.0")
                        .description("API REST para geracao de pagamentos PIX no padrao EMV.")
                        .contact(new Contact()
                                .name("DIO")
                                .email("contato@dio.me")
                                .url("https://dio.me"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url(apiUrl)
                                .description("Servidor")
                ));
    }
}
