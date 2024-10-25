package com.ssafy.runit.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        Info info = new Info()
                .version("1.0.0")
                .title("RunIt Spring Server API Docs")
                .description("RunIt Spring Server API");
        return new OpenAPI().components(new Components())
                .info(info);
    }
}
