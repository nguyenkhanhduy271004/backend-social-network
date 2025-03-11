package com.project.social_network.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import java.util.List;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!prod")
public class OpenAPIConfig {

  @Bean
  public GroupedOpenApi publicApi(@Value("${openapi.service.api-docs}") String apiDocs) {
    return GroupedOpenApi.builder()
        .group(apiDocs)
        .packagesToScan("com.project.social_network.controller")
        .build();
  }

  @Bean
  public OpenAPI openAPI(
      @Value("${openapi.service.title}") String title,
      @Value("${openapi.service.version}") String version,
      @Value("${openapi.service.server}") String serverUrl) {

    return new OpenAPI()
        .servers(List.of(new Server().url(serverUrl)))
        .info(new Info()
            .title(title)
            .description("API documents")
            .version(version)
            .license(new License().name("Apache 2.0").url("https://springdoc.org")))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components()
            .addSecuritySchemes("bearerAuth",
                new SecurityScheme()
                    .name("bearerAuth")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")));
  }
}
