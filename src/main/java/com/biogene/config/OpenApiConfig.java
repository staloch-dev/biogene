package com.biogene.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BioGene • Laboratory Management API")
                        .version("1.0.0")
                        .description("API REST para gerenciamento de exames laboratoriais clínicos. " +
                                "Oferece cadastro e gerenciamento completo de laboratórios, pacientes e exames, " +
                                "com suporte a mais de 100 tipos de exames organizados por categoria clínica, " +
                                "validação automática de amostras biológicas compatíveis e controle do ciclo de vida dos exames.")
                        .contact(new Contact()
                                .name("Jaison Staloch Junior")
                                .email("staloch.dev@gmail.com")
                                .url("https://github.com/staloch-dev")));
    }

}
